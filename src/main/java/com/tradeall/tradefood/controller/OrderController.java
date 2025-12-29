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
        if (user.getRole() == User.Role.ROLE_ADMIN) {
            return ResponseEntity.ok(orderService.getAllOrders());
        }
        return ResponseEntity.ok(orderService.getOrdersByUser(user));
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
     * Initie le processus de commande en créant un objet Order et une intention de paiement Stripe.
     * @param user L'utilisateur authentifié.
     * @return Le Client Secret Stripe et l'ID de la commande.
     * @throws StripeException en cas d'erreur avec l'API Stripe.
     */
    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> checkout(@AuthenticationPrincipal User user) throws StripeException {
        log.info("Début du processus de checkout pour l'utilisateur: {}", user.getEmail());
        Cart cart = cartService.getCartByUser(user);
        if (cart.getItems().isEmpty()) {
            log.warn("Tentative de checkout avec un panier vide pour {}", user.getEmail());
            return ResponseEntity.badRequest().build();
        }

        Order order = orderService.createOrderFromCart(cart);
        
        // On crée le PaymentIntent en passant l'order pour le récupérer dans le webhook
        PaymentIntent paymentIntent = stripeService.createPaymentIntent(order);
        
        log.info("Checkout initialisé. Commande ID: {}, PaymentIntent ID: {}", order.getId(), paymentIntent.getId());
        return ResponseEntity.ok(Map.of(
                "clientSecret", paymentIntent.getClientSecret(),
                "orderId", order.getId().toString()
        ));
    }
}
