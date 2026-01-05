package com.tradeall.tradefood.dto.sirene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

/**
 * DTO racine pour la réponse de l'API Sirene.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SireneResponseDTO {
    private SireneHeaderDTO header;
    private List<Map<String, Object>> etablissements;
    private Map<String, Object> etablissement;
    private Map<String, Object> uniteLegale;

    public SireneResponseDTO() {}

    public SireneHeaderDTO getHeader() { return header; }
    public void setHeader(SireneHeaderDTO header) { this.header = header; }

    public List<Map<String, Object>> getEtablissements() { return etablissements; }
    public void setEtablissements(List<Map<String, Object>> etablissements) { this.etablissements = etablissements; }

    public Map<String, Object> getEtablissement() { return etablissement; }
    public void setEtablissement(Map<String, Object> etablissement) { this.etablissement = etablissement; }

    public Map<String, Object> getUniteLegale() { return uniteLegale; }
    public void setUniteLegale(Map<String, Object> uniteLegale) { this.uniteLegale = uniteLegale; }
}
