package com.tradeall.tradefood.service;

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

    private String accessToken;
    private LocalDateTime expiryTime;

    private final WebClient webClient;

    public SellsyAuthService() {
        this.webClient = WebClient.builder().build();
    }

    /**
     * Récupère un jeton d'accès valide. Si le jeton actuel est expiré ou inexistant, il est rafraîchi.
     * @return Le jeton d'accès OAuth2.
     */
    public String getAccessToken() {
        if (accessToken != null && expiryTime != null && LocalDateTime.now().isBefore(expiryTime)) {
            log.debug("Utilisation du jeton d'accès Sellsy existant");
            return accessToken;
        }
        log.info("Jeton d'accès Sellsy expiré ou inexistant, rafraîchissement...");
        return refreshAccessToken().block();
    }

    /**
     * Appelle l'endpoint de Sellsy pour obtenir un nouveau jeton d'accès via client_credentials.
     * @return Un Mono contenant le nouveau jeton d'accès.
     */
    private Mono<String> refreshAccessToken() {
        log.debug("Appel de l'URL d'authentification Sellsy: {}", authUrl);
        return webClient.post()
                .uri(authUrl)
                .bodyValue(new AuthRequest("client_credentials", clientId, clientSecret))
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .map(response -> {
                    log.info("Nouveau jeton d'accès Sellsy obtenu. Expire dans {} secondes", response.getExpires_in());
                    this.accessToken = response.getAccess_token();
                    this.expiryTime = LocalDateTime.now().plusSeconds(response.getExpires_in() - 60); // 60s de marge
                    return this.accessToken;
                })
                .doOnError(error -> log.error("Erreur lors du rafraîchissement du jeton Sellsy: {}", error.getMessage()));
    }

    private static class AuthRequest {
        private String grant_type;
        private String client_id;
        private String client_secret;

        public AuthRequest(String grant_type, String client_id, String client_secret) {
            this.grant_type = grant_type;
            this.client_id = client_id;
            this.client_secret = client_secret;
        }

        public String getGrant_type() { return grant_type; }
        public String getClient_id() { return client_id; }
        public String getClient_secret() { return client_secret; }
    }

    private static class AuthResponse {
        private String access_token;
        private String token_type;
        private Long expires_in;

        public String getAccess_token() { return access_token; }
        public void setAccess_token(String access_token) { this.access_token = access_token; }
        public Long getExpires_in() { return expires_in; }
        public void setExpires_in(Long expires_in) { this.expires_in = expires_in; }
    }
}
