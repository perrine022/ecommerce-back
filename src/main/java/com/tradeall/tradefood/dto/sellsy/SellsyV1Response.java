package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

/**
 * Enveloppe de réponse pour l'API Sellsy v1.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyV1Response<T> {
    private T response;
    private String status;
    private String error;

    public T getResponse() { return response; }
    public void setResponse(T response) { this.response = response; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
