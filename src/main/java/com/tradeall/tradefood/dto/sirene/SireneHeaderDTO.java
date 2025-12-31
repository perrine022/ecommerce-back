package com.tradeall.tradefood.dto.sirene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Header de la réponse de l'API Sirene.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SireneHeaderDTO {
    private int statut;
    private String message;
    private int total;
    private int debut;
    private int nombre;
    private String curseur;
    private String curseurSuivant;

    public SireneHeaderDTO() {}

    public int getStatut() { return statut; }
    public void setStatut(int statut) { this.statut = statut; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public int getDebut() { return debut; }
    public void setDebut(int debut) { this.debut = debut; }

    public int getNombre() { return nombre; }
    public void setNombre(int nombre) { this.nombre = nombre; }

    public String getCurseur() { return curseur; }
    public void setCurseur(String curseur) { this.curseur = curseur; }

    public String getCurseurSuivant() { return curseurSuivant; }
    public void setCurseurSuivant(String curseurSuivant) { this.curseurSuivant = curseurSuivant; }
}
