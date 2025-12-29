package com.tradeall.tradefood.client;

import com.tradeall.tradefood.dto.sellsy.SellsyContact;
import com.tradeall.tradefood.dto.sellsy.SellsyOrder;
import com.tradeall.tradefood.dto.sellsy.SellsyProduct;
import com.tradeall.tradefood.dto.sellsy.SellsyResponse;
import com.tradeall.tradefood.service.SellsyAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client HTTP pour interagir avec l'API v2 de Sellsy.
 * Fournit des méthodes pour gérer les produits, contacts et commandes.
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Component
public class SellsyClient {

    private static final Logger log = LoggerFactory.getLogger(SellsyClient.class);
    private final WebClient sellsyWebClient;
    private final SellsyAuthService authService;

    public SellsyClient(WebClient sellsyWebClient, SellsyAuthService authService) {
        this.sellsyWebClient = sellsyWebClient;
        this.authService = authService;
    }

    /**
     * Récupère la liste des produits depuis Sellsy.
     * @param limit Nombre maximum de produits à récupérer.
     * @param offset Décalage pour la pagination.
     * @return Un Mono contenant la réponse paginée de Sellsy.
     */
    public Mono<SellsyResponse<SellsyProduct>> getItems(int limit, int offset) {
        log.debug("Appel Sellsy GET /items (limit: {}, offset: {})", limit, offset);
        return sellsyWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/items")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SellsyResponse<SellsyProduct>>() {});
    }

    /**
     * Récupère la liste des contacts depuis Sellsy.
     * @param limit Nombre maximum de contacts à récupérer.
     * @param offset Décalage pour la pagination.
     * @return Un Mono contenant la réponse paginée de Sellsy.
     */
    public Mono<SellsyResponse<SellsyContact>> getContacts(int limit, int offset) {
        log.debug("Appel Sellsy GET /contacts (limit: {}, offset: {})", limit, offset);
        return sellsyWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/contacts")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SellsyResponse<SellsyContact>>() {});
    }

    /**
     * Récupère un contact spécifique depuis Sellsy par son ID.
     * @param contactId L'identifiant Sellsy du contact.
     * @return Un Mono contenant le contact.
     */
    public Mono<SellsyContact> getContact(Long contactId) {
        log.debug("Appel Sellsy GET /contacts/{}", contactId);
        return sellsyWebClient.get()
                .uri("/contacts/" + contactId)
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .retrieve()
                .bodyToMono(SellsyContact.class);
    }

    /**
     * Crée un nouveau contact dans Sellsy.
     * @param contact Les données du contact à créer.
     * @return Un Mono contenant le contact créé.
     */
    public Mono<SellsyContact> createContact(SellsyContact contact) {
        log.debug("Appel Sellsy POST /contacts pour: {}", contact.getEmail());
        return sellsyWebClient.post()
                .uri("/contacts")
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .bodyValue(contact)
                .retrieve()
                .bodyToMono(SellsyContact.class);
    }
    
    /**
     * Met à jour un contact existant dans Sellsy.
     * @param contactId L'identifiant Sellsy du contact.
     * @param contact Les nouvelles données.
     * @return Un Mono contenant le contact mis à jour.
     */
    public Mono<SellsyContact> updateContact(Long contactId, SellsyContact contact) {
        log.debug("Appel Sellsy PUT /contacts/{}", contactId);
        return sellsyWebClient.put()
                .uri("/contacts/" + contactId)
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .bodyValue(contact)
                .retrieve()
                .bodyToMono(SellsyContact.class);
    }

    /**
     * Crée une commande dans Sellsy.
     * @param order Les données de la commande.
     * @return Un Mono contenant la commande créée.
     */
    public Mono<SellsyOrder> createOrder(SellsyOrder order) {
        log.debug("Appel Sellsy POST /orders");
        return sellsyWebClient.post()
                .uri("/orders")
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .bodyValue(order)
                .retrieve()
                .bodyToMono(SellsyOrder.class);
    }

    /**
     * Récupère la liste des commandes depuis Sellsy.
     * @param limit Nombre maximum.
     * @param offset Décalage.
     * @return Un Mono contenant la réponse paginée.
     */
    public Mono<SellsyResponse<SellsyOrder>> getOrders(int limit, int offset) {
        log.debug("Appel Sellsy GET /orders (limit: {}, offset: {})", limit, offset);
        return sellsyWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/orders")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SellsyResponse<SellsyOrder>>() {});
    }
}
