package com.tradeall.tradefood.dto.auth;


/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
public class AuthenticationResponse {
    private String token;
    private Long sellsyId;

    public AuthenticationResponse() {}

    public AuthenticationResponse(String token, Long sellsyId) {
        this.token = token;
        this.sellsyId = sellsyId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getSellsyId() { return sellsyId; }
    public void setSellsyId(Long sellsyId) { this.sellsyId = sellsyId; }

    public static AuthenticationResponseBuilder builder() {
        return new AuthenticationResponseBuilder();
    }

    public static class AuthenticationResponseBuilder {
        private String token;
        private Long sellsyId;

        public AuthenticationResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthenticationResponseBuilder sellsyId(Long sellsyId) {
            this.sellsyId = sellsyId;
            return this;
        }

        public AuthenticationResponse build() {
            return new AuthenticationResponse(token, sellsyId);
        }
    }
}
