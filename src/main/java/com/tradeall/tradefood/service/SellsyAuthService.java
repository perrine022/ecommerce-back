package com.tradeall.tradefood.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Service gérant l'authentification OAuth2 auprès de l'API Sellsy.
 * Gère la récupération et le rafraîchissement automatique des jetons d'accès.
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Service
public class SellsyAuthService {

    private static final Logger log = LoggerFactory.getLogger(SellsyAuthService.class);

    @Value("${sellsy.client-id}")
    private String clientId;

    @Value("${sellsy.client-secret}")
    private String clientSecret;

    @Value("${sellsy.auth-url}")
    private String authUrl;

    @Value("${sellsy.refresh-token:}")
    private String refreshToken;

    private volatile String accessToken;
    private volatile String currentRefreshToken;
    private volatile LocalDateTime expiryTime;

    private final WebClient webClient;

    public SellsyAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Récupère un jeton d'accès valide. Si le jeton actuel est expiré ou inexistant, il est rafraîchi.
     * @return Le jeton d'accès OAuth2.
     */
    public synchronized String getAccessToken() {
        if (accessToken != null && expiryTime != null && LocalDateTime.now().isBefore(expiryTime)) {
            return accessToken;
        }
        log.info("Jeton d'accès Sellsy expiré ou inexistant, rafraîchissement...");
        return refreshAccessToken().block();
    }

    /**
     * Appelle l'endpoint de Sellsy pour obtenir un nouveau jeton d'accès.
     * @return Un Mono contenant le nouveau jeton d'accès.
     */
    private Mono<String> refreshAccessToken() {
        log.debug("Appel de l'URL d'authentification Sellsy: {}", authUrl);
        
        AuthRequest request;
        if (currentRefreshToken != null || (refreshToken != null && !refreshToken.isEmpty())) {
            String tokenToUse = currentRefreshToken != null ? currentRefreshToken : refreshToken;
            log.debug("Tentative de rafraîchissement via refresh_token");
            request = new AuthRequest("refresh_token", clientId, clientSecret, null, tokenToUse);
        } else {
            log.debug("Tentative d'authentification via client_credentials");
            request = new AuthRequest("client_credentials", clientId, clientSecret, null, null);
        }

        return webClient.post()
                .uri(authUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .map(response -> {
                    if (response.getAccessToken() == null) {
                        throw new RuntimeException("L'API Sellsy n'a pas renvoyé d'access_token");
                    }
                    log.info("Nouveau jeton d'accès Sellsy obtenu. Expire dans {} secondes", response.getExpiresIn());
                    this.accessToken = response.getAccessToken();
                    if (response.getRefreshToken() != null) {
                        this.currentRefreshToken = response.getRefreshToken();
                    }
                    this.expiryTime = LocalDateTime.now().plusSeconds(response.getExpiresIn() - 60);
                    return this.accessToken;
                })
                .doOnError(error -> log.error("Erreur lors du rafraîchissement du jeton Sellsy: {}", error.getMessage()));
    }

    private static class AuthRequest {
        @JsonProperty("grant_type")
        private String grantType;
        @JsonProperty("client_id")
        private String clientId;
        @JsonProperty("client_secret")
        private String clientSecret;
        @JsonProperty("code")
        private String code;
        @JsonProperty("refresh_token")
        private String refreshToken;

        public AuthRequest(String grantType, String clientId, String clientSecret, String code, String refreshToken) {
            this.grantType = grantType;
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.code = code;
            this.refreshToken = refreshToken;
        }

        public String getGrantType() { return grantType; }
        public String getClientId() { return clientId; }
        public String getClientSecret() { return clientSecret; }
        public String getCode() { return code; }
        public String getRefreshToken() { return refreshToken; }
    }

    private static class AuthResponse {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("refresh_token")
        private String refreshToken;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("expires_in")
        private Long expiresIn;

        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }
        public Long getExpiresIn() { return expiresIn; }
        public void setExpiresIn(Long expiresIn) { this.expiresIn = expiresIn; }
    }
}
