package com.tradeall.tradefood.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.persistence.*;
import java.util.UUID;

/**
 * Entité représentant une catégorie de produits synchronisée depuis Sellsy.
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "sellsy_id", unique = true)
    private Long sellsyId;

    @Column(name = "sellsy_id_v1", unique = true)
    private String sellsyIdV1;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "logo")
    private String logo;

    @Column(name = "rank_val")
    private String rank;

    @Column(name = "label")
    private String label;

    @Column(name = "color")
    private String color;

    @Column(name = "icon")
    private String icon;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "sources", columnDefinition = "TEXT")
    private String sources;

    public Category() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getSellsyId() {
        return sellsyId;
    }

    public void setSellsyId(Long sellsyId) {
        this.sellsyId = sellsyId;
    }

    public String getSellsyIdV1() {
        return sellsyIdV1;
    }

    public void setSellsyIdV1(String sellsyIdV1) {
        this.sellsyIdV1 = sellsyIdV1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
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

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @JsonRawValue
    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }
}
