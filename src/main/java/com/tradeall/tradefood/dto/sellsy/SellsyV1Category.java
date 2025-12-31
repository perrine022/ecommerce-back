package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

/**
 * DTO pour les catégories Sellsy API v1.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyV1Category {
    private String id;
    private String name;
    private String description;
    private String parentid;
    private String logo;
    private String corpid;
    private String rank;
    private Object public_path; // Peut être false ou String
    private List<SellsyV1Category> children;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getParentid() { return parentid; }
    public void setParentid(String parentid) { this.parentid = parentid; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public String getCorpid() { return corpid; }
    public void setCorpid(String corpid) { this.corpid = corpid; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public Object getPublic_path() { return public_path; }
    public void setPublic_path(Object public_path) { this.public_path = public_path; }
    public List<SellsyV1Category> getChildren() { return children; }
    public void setChildren(List<SellsyV1Category> children) { this.children = children; }
}
