package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO pour les catégories Sellsy.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyCategory {
    private Long id;
    private String label;
    private String color;
    private String icon;
    private Boolean is_default;
    private SellsyCategoryEmbed _embed;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyCategoryEmbed {
        private java.util.List<Object> sources;

        public java.util.List<Object> getSources() {
            return sources;
        }

        public void setSources(java.util.List<Object> sources) {
            this.sources = sources;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getIs_default() {
        return is_default;
    }

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }

    public SellsyCategoryEmbed get_embed() {
        return _embed;
    }

    public void set_embed(SellsyCategoryEmbed _embed) {
        this._embed = _embed;
    }
}
