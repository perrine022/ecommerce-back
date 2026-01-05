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

    @Value("${sirene.api.url:https://api.insee.fr/api-sirene/3.11/siret}")
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

    public SireneCompanyInfoDTO validateSirene(String id) {
        log.info("Validation SIREN/SIRET: {}", id);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-INSEE-Api-Key-Integration", apiKey);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Si l'id fait 9 caractères, c'est un SIREN, sinon on considère que c'est un SIRET (14 caractères)
        String baseUrl = (id != null && id.length() == 9) ? apiUrlSiren : apiUrlSiret;
        String url = String.format("%s/%s?masquerValeursNulles=false", baseUrl, id);
        log.debug("URL d'appel SIRENE : {}", url);

        try {
            ResponseEntity<SireneResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    SireneResponseDTO.class
            );

            SireneResponseDTO body = response.getBody();
            if (body == null || (body.getUniteLegale() == null && body.getEtablissement() == null)) {
                throw new RuntimeException("Aucune donnée trouvée pour cet identifiant.");
            }

            Map<String, Object> data = body.getUniteLegale() != null ? body.getUniteLegale() : body.getEtablissement();

            // Si c'est un établissement (SIRET), l'unité légale est souvent imbriquée
            if (body.getEtablissement() != null && data.containsKey("uniteLegale")) {
                data = (Map<String, Object>) data.get("uniteLegale");
            }

            if (!isValidCompany(data)) {
                throw new RuntimeException("L'entreprise ne respecte pas les critères de validation (active, > 2 ans, etc.).");
            }

            return extractCompanyInfo(data, id);
        } catch (Exception e) {
            log.error("Erreur lors de l'appel à l'API SIRENE pour {}", id, e);
            throw new RuntimeException("Erreur lors de la validation : " + e.getMessage());
        }
    }

    private boolean isValidCompany(Map<String, Object> ul) {
        // Validation basée sur les périodes (historique)
        List<Map<String, Object>> periodes = (List<Map<String, Object>>) ul.get("periodesUniteLegale");
        if (periodes != null && !periodes.isEmpty()) {
            // On prend la période actuelle (la première de la liste généralement)
            Map<String, Object> periodeActuelle = periodes.get(0);
            
            // 2. etatAdministratifUniteLegale == "A" (Active)
            String etatAdministratif = (String) periodeActuelle.get("etatAdministratifUniteLegale");
            if (!"A".equals(etatAdministratif)) {
                log.warn("Entreprise non active: etatAdministratifUniteLegale = {}", etatAdministratif);
                return false;
            }

            // 5. categorieJuridiqueUniteLegale != "1000" (Entrepreneurs individuels)
            String categorieJuridique = (String) periodeActuelle.get("categorieJuridiqueUniteLegale");
            if ("1000".equals(categorieJuridique)) {
                log.warn("Entreprise de type entrepreneur individuel (1000) non autorisée");
                return false;
            }
        }

        // 3. trancheEffectifsUniteLegale != "00" ou "01"
        String trancheEffectifs = (String) ul.get("trancheEffectifsUniteLegale");
        if ("00".equals(trancheEffectifs) || "01".equals(trancheEffectifs)) {
            log.warn("Tranche d'effectifs trop basse: {}", trancheEffectifs);
            return false;
        }

        // 4. supprimmes les entreprises qui ont moins de deux ans (dateCreationUniteLegale)
        String dateCreationStr = (String) ul.get("dateCreationUniteLegale");
        if (dateCreationStr != null) {
            try {
                LocalDate dateCreation = LocalDate.parse(dateCreationStr);
                LocalDate now = LocalDate.now();
                if (Period.between(dateCreation, now).getYears() < 2) {
                    log.warn("Entreprise trop récente: créée le {}", dateCreationStr);
                    return false;
                }
            } catch (Exception e) {
                log.warn("Impossible de parser la date de création : {}", dateCreationStr);
            }
        }

        return true;
    }

    private SireneCompanyInfoDTO extractCompanyInfo(Map<String, Object> data, String id) {
        String name = "";
        String apeCode = "";

        // Si c'est un établissement, on cherche dans activitePrincipaleNAF25Etablissement
        if (data.containsKey("activitePrincipaleNAF25Etablissement")) {
            apeCode = (String) data.get("activitePrincipaleNAF25Etablissement");
        }

        // Si c'est une unité légale, on cherche dans activitePrincipaleUniteLegale
        if ((apeCode == null || apeCode.isEmpty()) && data.containsKey("activitePrincipaleUniteLegale")) {
            apeCode = (String) data.get("activitePrincipaleUniteLegale");
        }

        // Extraction du nom depuis les périodes (denominationUniteLegale)
        List<Map<String, Object>> periodes = (List<Map<String, Object>>) data.get("periodesUniteLegale");
        if (periodes != null && !periodes.isEmpty()) {
            Map<String, Object> periodeActuelle = periodes.get(0);
            name = (String) periodeActuelle.get("denominationUniteLegale");
        }

        // Fallback si denominationUniteLegale est vide dans la période
        if (name == null || name.isEmpty()) {
            name = (String) data.get("denominationUniteLegale");
        }

        // Fallback si toujours vide (cas des personnes physiques)
        if (name == null || name.isEmpty()) {
            String nom = (String) data.get("nomUniteLegale");
            String prenom = (String) data.get("prenom1UniteLegale");
            name = (prenom != null ? prenom + " " : "") + (nom != null ? nom : "");
        }

        return new SireneCompanyInfoDTO(name.trim(), "Adresse non disponible via endpoint SIREN", id, apeCode);
    }
}
