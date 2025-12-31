package com.tradeall.tradefood.service;

import com.tradeall.tradefood.dto.sirene.SireneCompanyInfoDTO;
import com.tradeall.tradefood.dto.sirene.SireneResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Service
public class SireneService {

    private static final Logger log = LoggerFactory.getLogger(SireneService.class);

    @Value("${sirene.api.key:2b90ec68-758c-471d-90ec-68758cf71dd4}")
    private String apiKey;

    @Value("${sirene.api.url.siret:https://api.insee.fr/api-sirene/3.11/siret}")
    private String apiUrlSiret;

    @Value("${sirene.api.url.siren:https://api.insee.fr/api-sirene/3.11/siren}")
    private String apiUrlSiren;

    private final RestTemplate restTemplate;

    public SireneService() {
        this.restTemplate = new RestTemplate();
    }

    public SireneResponseDTO fetchCompanies(String customQuery, String activitePrincipale, Integer nombre, String curseur) {
        log.info("Appel API Sirene. CustomQuery: {}, activitePrincipale: {}, nombre: {}, curseur: {}", customQuery, activitePrincipale, nombre, curseur);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-INSEE-Api-Key-Integration", apiKey);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String query;
        if (customQuery != null && !customQuery.isEmpty()) {
            query = customQuery;
        } else {
            query = String.format("activitePrincipaleUniteLegale:'%s'", activitePrincipale);
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrlSiret)
                .queryParam("q", query)
                .queryParam("nombre", nombre)
                .queryParam("curseur", curseur != null ? curseur : "");

        String url = builder.toUriString();
        log.debug("URL d'appel Sirene : {}", url);

        try {
            ResponseEntity<SireneResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    SireneResponseDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erreur lors de l'appel à l'API Sirene", e);
            throw new RuntimeException("Erreur lors de la récupération des données Sirene", e);
        }
    }

    public SireneCompanyInfoDTO validateSirene(String siren) {
        log.info("Validation SIREN: {}", siren);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-INSEE-Api-Key-Integration", apiKey);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = String.format("%s/%s?masquerValeursNulles=false", apiUrlSiren, siren);
        log.debug("URL d'appel SIREN : {}", url);

        try {
            ResponseEntity<SireneResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    SireneResponseDTO.class
            );

            SireneResponseDTO body = response.getBody();
            if (body == null || body.getUniteLegale() == null) {
                throw new RuntimeException("Aucune unité légale trouvée pour ce SIREN.");
            }

            Map<String, Object> uniteLegale = body.getUniteLegale();

            if (!isValidCompany(uniteLegale)) {
                throw new RuntimeException("L'entreprise ne respecte pas les critères de validation (active, > 2 ans, etc.).");
            }

            return extractCompanyInfo(uniteLegale, siren);
        } catch (Exception e) {
            log.error("Erreur lors de l'appel à l'API SIREN pour {}", siren, e);
            throw new RuntimeException("Erreur lors de la validation du SIREN: " + e.getMessage());
        }
    }

    private boolean isValidCompany(Map<String, Object> ul) {
        // 2. etatAdministratifUniteLegale == "A"
        String etatAdministratifUniteLegale = (String) ul.get("etatAdministratifUniteLegale");
        if (!"A".equals(etatAdministratifUniteLegale)) {
            return false;
        }

        // 3. trancheEffectifsUniteLegale != "00" ou "01"
        String trancheEffectifsUniteLegale = (String) ul.get("trancheEffectifsUniteLegale");
        if ("00".equals(trancheEffectifsUniteLegale) || "01".equals(trancheEffectifsUniteLegale)) {
            return false;
        }

        // 5. categorieJuridiqueUniteLegale != "1000"
        String categorieJuridiqueUniteLegale = (String) ul.get("categorieJuridiqueUniteLegale");
        if ("1000".equals(categorieJuridiqueUniteLegale)) {
            return false;
        }

        // 4. supprimmes les entreprises qui ont moins de deux ans (dateCreationUniteLegale)
        String dateCreationUniteLegaleStr = (String) ul.get("dateCreationUniteLegale");
        if (dateCreationUniteLegaleStr != null) {
            try {
                LocalDate dateCreation = LocalDate.parse(dateCreationUniteLegaleStr);
                LocalDate now = LocalDate.now();
                if (Period.between(dateCreation, now).getYears() < 2) {
                    return false;
                }
            } catch (Exception e) {
                log.warn("Impossible de parser la date de création : {}", dateCreationUniteLegaleStr);
            }
        }

        return true;
    }

    private SireneCompanyInfoDTO extractCompanyInfo(Map<String, Object> ul, String siren) {
        String name = (String) ul.get("denominationUniteLegale");
        if (name == null || name.isEmpty()) {
            String nom = (String) ul.get("nomUniteLegale");
            String prenom = (String) ul.get("prenom1UniteLegale");
            name = (prenom != null ? prenom + " " : "") + (nom != null ? nom : "");
        }

        // Dans l'endpoint SIREN, on n'a pas l'adresse complète directement comme dans l'etablissement siege
        // Habituellement, l'adresse est liée à un établissement. 
        // L'API SIREN 3.11 renvoie l'unité légale, mais pas forcément l'adresse du siège directement sans passer par les établissements.
        // Toutefois, on peut essayer de récupérer le siège s'il est inclus ou laisser vide/SIREN si absent.
        
        return new SireneCompanyInfoDTO(name, "Adresse non disponible via endpoint SIREN", siren);
    }
}
