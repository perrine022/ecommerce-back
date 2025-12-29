package com.tradeall.tradefood.controller;

import com.tradeall.tradefood.entity.Cart;
import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * Contrôleur gérant le panier d'achat de l'utilisateur connecté.
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Récupère le panier de l'utilisateur authentifié.
     * @param user L'utilisateur authentifié.
     * @return Le panier de l'utilisateur.
     */
    @GetMapping
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal User user) {
        log.info("Requête pour récupérer le panier de l'utilisateur: {}", user.getEmail());
        return ResponseEntity.ok(cartService.getCartByUser(user));
    }

    /**
     * Ajoute un produit au panier de l'utilisateur authentifié.
     * @param user L'utilisateur authentifié.
     * @param productId L'identifiant du produit.
     * @param quantity La quantité à ajouter.
     * @return Réponse vide.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(
            @AuthenticationPrincipal User user,
            @RequestParam UUID productId,
            @RequestParam Integer quantity
    ) {
        log.info("Requête pour ajouter au panier - Utilisateur: {}, Produit ID: {}, Quantité: {}", user.getEmail(), productId, quantity);
        cartService.addToCart(user, productId, quantity);
        return ResponseEntity.ok().build();
    }
}
