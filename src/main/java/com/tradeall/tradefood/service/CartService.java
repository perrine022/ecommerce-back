package com.tradeall.tradefood.service;

import com.tradeall.tradefood.entity.Cart;
import com.tradeall.tradefood.entity.CartItem;
import com.tradeall.tradefood.entity.Product;
import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.repository.CartItemRepository;
import com.tradeall.tradefood.repository.CartRepository;
import com.tradeall.tradefood.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service gérant le panier d'achat des utilisateurs et les calculs de totaux (HT, TVA, TTC).
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Service
public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    /**
     * Récupère le panier d'un utilisateur. En crée un nouveau s'il n'existe pas.
     * @param user L'utilisateur.
     * @return Le panier avec les totaux calculés.
     */
    public Cart getCartByUser(User user) {
        log.debug("Récupération du panier pour l'utilisateur: {}", user.getEmail());
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    log.debug("Aucun panier trouvé, création d'un nouveau panier pour {}", user.getEmail());
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });
        calculateTotals(cart);
        return cart;
    }

    /**
     * Calcule les totaux HT, TVA et TTC du panier en fonction des produits et quantités.
     * @param cart Le panier à calculer.
     */
    private void calculateTotals(Cart cart) {
        log.debug("Calcul des totaux pour le panier ID: {}", cart.getId());
        double totalHT = 0.0;
        double totalTVA = 0.0;

        for (CartItem item : cart.getItems()) {
            Double price = Double.parseDouble(item.getProduct().getReferencePrice() != null ? item.getProduct().getReferencePrice() : "0");
            double itemHT = price * item.getQuantity();
            // Defaulting tax rate to 20.0 for now as it's not directly in Product anymore
            double itemTVA = itemHT * (20.0 / 100.0);
            totalHT += itemHT;
            totalTVA += itemTVA;
        }

        cart.setTotalHT(totalHT);
        cart.setTotalTVA(totalTVA);
        cart.setTotalTTC(totalHT + totalTVA);
        log.debug("Totaux calculés - HT: {}, TVA: {}, TTC: {}", totalHT, totalTVA, cart.getTotalTTC());
    }

    /**
     * Ajoute un produit au panier de l'utilisateur.
     * @param user L'utilisateur.
     * @param productId L'identifiant du produit à ajouter.
     * @param quantity La quantité à ajouter.
     */
    @Transactional
    public void addToCart(User user, UUID productId, Integer quantity) {
        log.debug("Ajout au panier - Utilisateur: {}, Produit ID: {}, Quantité: {}", user.getEmail(), productId, quantity);
        Cart cart = getCartByUser(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Produit non trouvé ID: {}", productId);
                    return new RuntimeException("Product not found");
                });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
            log.info("Quantité mise à jour pour le produit {} dans le panier de {}", product.getName(), user.getEmail());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
            log.info("Nouveau produit {} ajouté au panier de {}", product.getName(), user.getEmail());
        }
    }
}
