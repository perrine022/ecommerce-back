package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO pour les requêtes vers l'API Sellsy v1.
 */
public class SellsyV1Request {
    private String method;
    private java.util.Map<String, Object> params;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public java.util.Map<String, Object> getParams() {
        return params;
    }

    public void setParams(java.util.Map<String, Object> params) {
        this.params = params;
    }
}
