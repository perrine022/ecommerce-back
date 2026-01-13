package com.tradeall.tradefood.client;

import com.tradeall.tradefood.dto.sellsy.SellsyCategory;
import com.tradeall.tradefood.dto.sellsy.SellsyContact;
import com.tradeall.tradefood.dto.sellsy.SellsyAddressDTO;
import com.tradeall.tradefood.dto.sellsy.SellsyAddressRequest;
import com.tradeall.tradefood.dto.sellsy.SellsyCompany;
import com.tradeall.tradefood.dto.sellsy.SellsyCompanyRequest;
import com.tradeall.tradefood.dto.sellsy.SellsyIndividual;
import com.tradeall.tradefood.dto.sellsy.SellsyInvoice;
import com.tradeall.tradefood.dto.sellsy.SellsyOrder;
import com.tradeall.tradefood.dto.sellsy.SellsyOrderRequest;
import com.tradeall.tradefood.dto.sellsy.SellsyProduct;
import com.tradeall.tradefood.dto.sellsy.SellsyResponse;
import com.tradeall.tradefood.dto.sellsy.SellsyStaff;
import com.tradeall.tradefood.dto.sellsy.SellsyPaymentRequest;
import com.tradeall.tradefood.dto.sellsy.SellsyPaymentResponse;
import com.tradeall.tradefood.dto.sellsy.SellsyLinkPaymentRequest;
import com.tradeall.tradefood.dto.sellsy.SellsyPaymentMethod;
import com.tradeall.tradefood.service.SellsyAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

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
    private final WebClient sellsyV1WebClient;
    private final SellsyAuthService authService;

    public SellsyClient(WebClient webClient, 
                        @org.springframework.beans.factory.annotation.Qualifier("sellsyV1WebClient") WebClient sellsyV1WebClient,
                        SellsyAuthService authService) {
        // Injection du WebClient principal (Primary) configuré avec sellsy.base-url
        this.sellsyWebClient = webClient;
        this.sellsyV1WebClient = sellsyV1WebClient;
        this.authService = authService;
    }

    /**
     * Récupère la liste des catégories de produits depuis Sellsy API v1.
     */
    public Mono<com.tradeall.tradefood.dto.sellsy.SellsyV1Response<java.util.List<com.tradeall.tradefood.dto.sellsy.SellsyV1Category>>> getCategoriesV1(String includeImages) {
        log.debug("Appel Sellsy v1 Catalogue.getCategories (includeImages: {})", includeImages);
        
        com.tradeall.tradefood.dto.sellsy.SellsyV1Request v1Request = new com.tradeall.tradefood.dto.sellsy.SellsyV1Request();
        v1Request.setMethod("Catalogue.getCategories");
        v1Request.setParams(java.util.Map.of("includeImages", includeImages));

        return sellsyV1WebClient.post()
                .uri("")
                .headers(headers -> {
                    headers.setBearerAuth(authService.getAccessToken());
                    headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
                })
                .body(org.springframework.web.reactive.function.BodyInserters.fromFormData("request", "1")
                        .with("io_mode", "json")
                        .with("do_in", serializeToJson(v1Request)))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<com.tradeall.tradefood.dto.sellsy.SellsyV1Response<java.util.List<com.tradeall.tradefood.dto.sellsy.SellsyV1Category>>>() {})
                .doOnNext(response -> log.debug("Réponse Sellsy v1 Catégories: {}", response.getResponse()));
    }

    public String serializeToJson(Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Erreur sérialisation JSON pour Sellsy v1: {}", e.getMessage());
            return "{}";
        }
    }

    /**
     * Récupère la liste des catégories de produits depuis Sellsy.
     * @param limit Nombre maximum.
     * @param offset Décalage.
     * @return Un Mono contenant la réponse paginée.
     */
    public Mono<SellsyResponse<SellsyCategory>> getCategories(int limit, int offset) {
        log.debug("Appel Sellsy GET /purchase/purOrder (limit: {}, offset: {})", limit, offset);
        return sellsyWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/opportunities/categories")
                        .queryParam("enable-beta", true)
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SellsyResponse<SellsyCategory>>() {});
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
     * Récupère la liste des individus depuis Sellsy.
     * @param limit Nombre maximum.
     * @param offset Décalage.
     * @return Un Mono contenant la réponse paginée.
     */
    public Mono<SellsyResponse<SellsyIndividual>> getIndividuals(int limit, int offset) {
        log.debug("Appel Sellsy GET /individuals (limit: {}, offset: {})", limit, offset);
        return sellsyWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/individuals")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SellsyResponse<SellsyIndividual>>() {});
    }

    /**
     * Récupère la liste des compagnies depuis Sellsy.
     * @param limit Nombre maximum.
     * @param offset Décalage.
     * @return Un Mono contenant la réponse paginée.
     */
    public Mono<SellsyResponse<SellsyCompany>> getCompanies(int limit, int offset) {
        log.debug("Appel Sellsy GET /companies (limit: {}, offset: {})", limit, offset);
        return sellsyWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/companies")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SellsyResponse<SellsyCompany>>() {});
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
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy getContact (contactId: {}): {}", contactId, body);
                        return Mono.error(new RuntimeException("Sellsy API Error getContact (contactId: " + contactId + "): " + body));
                    })
                )
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
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy updateContact (contactId: {}): {}", contactId, body);
                        return Mono.error(new RuntimeException("Sellsy API Error updateContact (contactId: " + contactId + "): " + body));
                    })
                )
                .bodyToMono(SellsyContact.class);
    }

    /**
     * Crée un paiement pour une compagnie.
     */
    public Mono<SellsyPaymentResponse> createPaymentForCompany(Long companyId, SellsyPaymentRequest payment) {
        log.debug("Appel Sellsy POST /companies/{}/payments", companyId);
        return sellsyWebClient.post()
                .uri("/companies/" + companyId + "/payments")
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .bodyValue(payment)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy createPaymentForCompany (companyId: {}): {}", companyId, body);
                        return Mono.error(new RuntimeException("Sellsy API Error createPaymentForCompany (companyId: " + companyId + "): " + body));
                    })
                )
                .bodyToMono(SellsyPaymentResponse.class);
    }

    /**
     * Crée un paiement pour un particulier.
     */
    public Mono<SellsyPaymentResponse> createPaymentForIndividual(Long individualId, SellsyPaymentRequest payment) {
        log.debug("Appel Sellsy POST /individuals/{}/payments", individualId);
        return sellsyWebClient.post()
                .uri("/individuals/" + individualId + "/payments")
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .bodyValue(payment)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy createPaymentForIndividual (individualId: {}): {}", individualId, body);
                        return Mono.error(new RuntimeException("Sellsy API Error createPaymentForIndividual (individualId: " + individualId + "): " + body));
                    })
                )
                .bodyToMono(SellsyPaymentResponse.class);
    }

    /**
     * Lie un paiement à une facture.
     */
    public Mono<Void> linkPaymentToInvoice(Long invoiceId, Long paymentId, SellsyLinkPaymentRequest request) {
        log.debug("Appel Sellsy POST /invoices/{}/payments/{}", invoiceId, paymentId);
        return sellsyWebClient.post()
                .uri("/invoices/" + invoiceId + "/payments/" + paymentId)
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy linkPaymentToInvoice (invoiceId: {}, paymentId: {}): {}", invoiceId, paymentId, body);
                        return Mono.error(new RuntimeException("Sellsy API Error linkPaymentToInvoice (invoiceId: " + invoiceId + ", paymentId: " + paymentId + "): " + body));
                    })
                )
                .bodyToMono(Void.class);
    }

    /**
     * Récupère la liste des méthodes de paiement.
     */
    public Mono<SellsyResponse<SellsyPaymentMethod>> getPaymentMethods() {
        log.debug("Appel Sellsy GET /payments/methods");
        return sellsyWebClient.get()
                .uri("/payments/methods")
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SellsyResponse<SellsyPaymentMethod>>() {});
    }

    /**
     * Crée une commande dans Sellsy.
     */
    public Mono<SellsyOrder> createOrder(SellsyOrderRequest order) {
        log.debug("Appel Sellsy POST /orders");
        return sellsyWebClient.post()
                .uri("/orders")
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .bodyValue(order)
                .retrieve()
                .bodyToMono(SellsyOrder.class);
    }

    /**
     * Crée un nouvel individu dans Sellsy.
     * @param individual Les données de l'individu à créer.
     * @return Un Mono contenant l'individu créé.
     */
    public Mono<SellsyIndividual> createIndividual(SellsyIndividual individual) {
        log.debug("Appel Sellsy POST /individuals pour: {}", individual.getEmail());
        return sellsyWebClient.post()
                .uri("/individuals")
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .bodyValue(individual)
                .retrieve()
                .bodyToMono(SellsyIndividual.class);
    }

    /**
     * Crée une nouvelle compagnie dans Sellsy.
     * @param company Les données de la compagnie à créer.
     * @return Un Mono contenant la compagnie créée.
     */
    public Mono<SellsyCompany> createCompany(SellsyCompanyRequest company) {
        log.debug("Appel Sellsy POST /companies pour: {}", company.getName());
        return sellsyWebClient.post()
                .uri("/companies")
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .bodyValue(company)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy: {}", body);
                        return Mono.error(new RuntimeException("Sellsy API Error createCompany: " + body));
                    })
                )
                .bodyToMono(SellsyCompany.class)
                .retryWhen(reactor.util.retry.Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof java.io.IOException || throwable instanceof io.netty.channel.ConnectTimeoutException));
    }

    /**
     * Récupère les adresses d'une entité (compagnie ou individu).
     */
    public Mono<SellsyResponse<SellsyAddressDTO>> getAddresses(String type, Long id, int limit, int offset) {
        String pathPrefix = "client".equals(type) || "company".equals(type) ? "/companies/" : "/individuals/";
        log.debug("Appel Sellsy GET {}{}/addresses", pathPrefix, id);
        return sellsyWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(pathPrefix + id + "/addresses")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy getAddresses (type: {}, id: {}): {}", type, id, body);
                        return Mono.error(new RuntimeException("Sellsy API Error getAddresses (type: " + type + ", id: " + id + "): " + body));
                    })
                )
                .bodyToMono(new ParameterizedTypeReference<SellsyResponse<SellsyAddressDTO>>() {});
    }

    /**
     * Crée une adresse pour une entité (compagnie ou individu).
     */
    public Mono<SellsyAddressDTO> createAddress(String type, Long id, SellsyAddressRequest address) {
        String pathPrefix = "client".equals(type) || "company".equals(type) ? "/companies/" : "/individuals/";
        log.debug("Appel Sellsy POST {}{}/addresses", pathPrefix, id);
        return sellsyWebClient.post()
                .uri(pathPrefix + id + "/addresses")
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .bodyValue(address)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy createAddress (type: {}, id: {}): {}", type, id, body);
                        return Mono.error(new RuntimeException("Sellsy API Error createAddress (type: " + type + ", id: " + id + "): " + body));
                    })
                )
                .bodyToMono(SellsyAddressDTO.class);
    }

    /**
     * Met à jour une adresse d'une entité.
     */
    public Mono<SellsyAddressDTO> updateAddress(String type, Long id, Long addressId, SellsyAddressDTO address) {
        String pathPrefix = "client".equals(type) || "company".equals(type) ? "/companies/" : "/individuals/";
        log.debug("Appel Sellsy PUT {}{}/addresses/{}", pathPrefix, id, addressId);
        return sellsyWebClient.put()
                .uri(pathPrefix + id + "/addresses/" + addressId)
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .bodyValue(address)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy updateAddress (type: {}, id: {}, addressId: {}): {}", type, id, addressId, body);
                        return Mono.error(new RuntimeException("Sellsy API Error updateAddress (type: " + type + ", id: " + id + ", addressId: " + addressId + "): " + body));
                    })
                )
                .bodyToMono(SellsyAddressDTO.class);
    }

    /**
     * Supprime une adresse d'une entité.
     */
    public Mono<Void> deleteAddress(String type, Long id, Long addressId) {
        String pathPrefix = "client".equals(type) || "company".equals(type) ? "/companies/" : "/individuals/";
        log.debug("Appel Sellsy DELETE {}{}/addresses/{}", pathPrefix, id, addressId);
        return sellsyWebClient.delete()
                .uri(pathPrefix + id + "/addresses/" + addressId)
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy deleteAddress (type: {}, id: {}, addressId: {}): {}", type, id, addressId, body);
                        return Mono.error(new RuntimeException("Sellsy API Error deleteAddress (type: " + type + ", id: " + id + ", addressId: " + addressId + "): " + body));
                    })
                )
                .bodyToMono(Void.class);
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

    /**
     * Recherche des commandes dans Sellsy selon des filtres.
     * @param searchBody Le corps de la requête de recherche.
     * @return Un Mono contenant la réponse paginée.
     */
    public Mono<SellsyResponse<SellsyOrder>> searchOrders(Object searchBody) {
        log.debug("Appel Sellsy POST /orders/search");
        return sellsyWebClient.post()
                .uri("/orders/search")
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .bodyValue(searchBody)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy Search: {}", body);
                        return Mono.error(new RuntimeException("Sellsy API Search Error: " + body));
                    })
                )
                .bodyToMono(new ParameterizedTypeReference<SellsyResponse<SellsyOrder>>() {});
    }

    /**
     * Récupère la liste des membres du staff depuis Sellsy.
     * @param limit Nombre maximum.
     * @param offset Décalage.
     * @return Un Mono contenant la réponse paginée.
     */
    public Mono<SellsyResponse<SellsyStaff>> getStaffs(int limit, int offset) {
        log.debug("Appel Sellsy GET /staffs (limit: {}, offset: {})", limit, offset);
        return sellsyWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/staffs")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy getStaffs (limit: {}, offset: {}): {}", limit, offset, body);
                        return Mono.error(new RuntimeException("Sellsy API Error getStaffs (limit: " + limit + ", offset: " + offset + "): " + body));
                    })
                )
                .bodyToMono(new ParameterizedTypeReference<SellsyResponse<SellsyStaff>>() {});
    }

    /**
     * Récupère la liste des factures depuis Sellsy.
     * @param limit Nombre maximum.
     * @param offset Décalage.
     * @return Un Mono contenant la réponse paginée.
     */
    public Mono<SellsyResponse<SellsyInvoice>> getInvoices(int limit, int offset) {
        log.debug("Appel Sellsy GET /invoices (limit: {}, offset: {})", limit, offset);
        return sellsyWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/invoices")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .headers(headers -> headers.setBearerAuth(authService.getAccessToken()))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> 
                    response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Erreur 4xx Sellsy getInvoices (limit: {}, offset: {}): {}", limit, offset, body);
                        return Mono.error(new RuntimeException("Sellsy API Error getInvoices (limit: " + limit + ", offset: " + offset + "): " + body));
                    })
                )
                .bodyToMono(new ParameterizedTypeReference<SellsyResponse<SellsyInvoice>>() {});
    }
}
