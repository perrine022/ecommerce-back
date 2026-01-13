package com.tradeall.tradefood.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.tradeall.tradefood.entity.Cart;
import com.tradeall.tradefood.entity.Order;
import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.service.CartService;
import com.tradeall.tradefood.service.OrderService;
import com.tradeall.tradefood.service.StripeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur gérant les commandes : consultation de l'historique et processus de paiement (checkout).
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final CartService cartService;
    private final StripeService stripeService;

    public OrderController(OrderService orderService, CartService cartService, StripeService stripeService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.stripeService = stripeService;
    }

    /**
     * Récupère la liste des commandes. Les administrateurs voient tout, les utilisateurs voient les leurs.
     * @param user L'utilisateur authentifié.
     * @return Liste des commandes.
     */
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@AuthenticationPrincipal User user) {
        log.info("Récupération des commandes pour l'utilisateur: {} (Rôle: {})", user.getEmail(), user.getRole());
        if (user.getRole() != User.Role.ROLE_ADMIN) {
            orderService.syncOrdersFromSellsy(user);
        }
        
        if (user.getRole() == User.Role.ROLE_ADMIN) {
            return ResponseEntity.ok(orderService.getAllOrders());
        }
        return ResponseEntity.ok(orderService.getOrdersByUser(user));
    }

    /**
     * Récupère toutes les commandes pour un client donné (Admin seulement).
     * @param userId L'identifiant de l'utilisateur.
     * @return Liste des commandes de l'utilisateur spécifié.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable java.util.UUID userId) {
        log.info("Récupération des commandes pour l'utilisateur ID: {}", userId);
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    /**
     * Récupère le détail d'une commande spécifique.
     * @param id L'identifiant de la commande.
     * @param user L'utilisateur authentifié.
     * @return La commande si autorisée.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable java.util.UUID id, @AuthenticationPrincipal User user) {
        log.info("Récupération de la commande ID: {} par l'utilisateur: {}", id, user.getEmail());
        Order order = orderService.getOrderById(id);
        if (user.getRole() != User.Role.ROLE_ADMIN && !order.getUser().getId().equals(user.getId())) {
            log.warn("Tentative d'accès non autorisée à la commande ID: {} par {}", id, user.getEmail());
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(order);
    }

    /**
     * Recherche des commandes par entreprise dans Sellsy.
     * @param companyId L'identifiant Sellsy de l'entreprise.
     * @return Résultat de la recherche Sellsy.
     */
    @PostMapping("/search/company/{companyId}")
    public reactor.core.publisher.Mono<ResponseEntity<?>> searchOrdersByCompany(@PathVariable Long companyId) {
        log.info("Recherche de commandes Sellsy pour l'entreprise ID: {}", companyId);
        return orderService.searchOrdersByCompany(companyId)
                .map(ResponseEntity::ok);
    }

    /**
     * Crée manuellement une commande à partir du panier de l'utilisateur (sans Stripe).
     * @param user L'utilisateur authentifié.
     * @param request Détails de la commande (adresses).
     * @return La commande créée.
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@AuthenticationPrincipal User user, @RequestBody com.tradeall.tradefood.dto.OrderCreateRequest request) {
        log.info("Réception d'une requête de création de commande pour l'utilisateur: {}", user.getEmail());
        try {
            String requestJson = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(request);
            log.info("[API] Payload reçu pour création de commande: {}", requestJson);
        } catch (Exception e) {
            log.warn("[API] Impossible de loguer le payload de la requête: {}", e.getMessage());
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            log.warn("Requête de création de commande rejetée : la liste des articles est vide pour l'utilisateur {}", user.getEmail());
            return ResponseEntity.badRequest().build();
        }
        Order createdOrder = orderService.createManualOrder(user, request);
        // On s'assure que l'ordre renvoyé est "frais" pour éviter les proxies Hibernate dans la réponse JSON
        Order responseOrder = orderService.getOrderById(createdOrder.getId());
        log.info("Commande créée avec succès via API. ID commande: {}, Utilisateur: {}", responseOrder.getId(), user.getEmail());
        return ResponseEntity.ok(responseOrder);
    }

    /**
     * Initie le processus de commande via Stripe (Optionnel).
     */
    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> checkout(@AuthenticationPrincipal User user) throws StripeException {
        // Logique Stripe commentée ou conservée comme alternative
        log.info("Processus checkout Stripe (Alternative)");
        // ...
        return ResponseEntity.status(501).build(); // Not implemented pour le moment
    }

    /**
     * Met à jour les adresses de livraison et de facturation d'une commande.
     * @param id L'identifiant de la commande.
     * @param addresses Les adresses à mettre à jour.
     * @return La commande mise à jour.
     */
    @PutMapping("/{id}/addresses")
    public ResponseEntity<Order> updateAddresses(@PathVariable java.util.UUID id, @RequestBody com.tradeall.tradefood.dto.AddressRequestDTO addresses) {
        log.info("Mise à jour des adresses pour la commande ID: {}", id);
        return ResponseEntity.ok(orderService.updateOrderAddresses(id, addresses));
    }

    /**
     * Valide une commande avec un code à 4 chiffres pour un utilisateur donné.
     * Accessible par les logisticiens ou admins.
     * @param id L'identifiant de la commande.
     * @param userId L'identifiant de l'utilisateur.
     * @param payload Map contenant le code "code".
     * @return La commande validée.
     */
    @PostMapping("/{id}/user/{userId}/validate")
    public ResponseEntity<Order> validateOrderForUser(@PathVariable java.util.UUID id, @PathVariable java.util.UUID userId, @RequestBody Map<String, String> payload) {
        String code = payload.get("code");
        log.info("Validation de la commande ID: {} pour l'utilisateur ID: {} avec le code: {}", id, userId, code);
        try {
            Order validatedOrder = orderService.validateOrderWithCode(id, userId, code);
            return ResponseEntity.ok(validatedOrder);
        } catch (RuntimeException e) {
            log.warn("Échec de la validation pour la commande ID: {}. Raison: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
