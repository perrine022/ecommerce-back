package com.tradeall.tradefood.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.tradeall.tradefood.entity.Cart;
import com.tradeall.tradefood.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

/**
 * Service gérant les interactions avec l'API Stripe pour les paiements.
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Service
public class StripeService {

    private static final Logger log = LoggerFactory.getLogger(StripeService.class);

    @Value("${stripe.api.key}")
    private String secretKey;

    /**
     * Initialise la clé API Stripe après la construction du bean.
     */
    @PostConstruct
    public void init() {
        log.debug("Initialisation de la clé API Stripe");
        Stripe.apiKey = secretKey;
    }

    /**
     * Crée une intention de paiement (PaymentIntent) auprès de Stripe pour une commande donnée.
     * @param order La commande pour laquelle créer le paiement.
     * @return L'objet PaymentIntent créé.
     * @throws StripeException en cas d'erreur lors de l'appel à l'API Stripe.
     */
    public PaymentIntent createPaymentIntent(Order order) throws StripeException {
        log.debug("Création d'un PaymentIntent pour la commande ID: {}", order.getId());
        
        long amount = (long) (order.getTotalAmount() * 100); // Stripe utilise les centimes
        log.debug("Montant calculé pour Stripe: {} cents", amount);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("eur")
                .putMetadata("order_id", order.getId().toString())
                .putMetadata("user_id", order.getUser().getId().toString())
                .build();

        PaymentIntent intent = PaymentIntent.create(params);
        log.info("PaymentIntent créé avec succès: {} pour la commande ID: {}", intent.getId(), order.getId());
        return intent;
    }

    /**
     * Crée une intention de paiement (PaymentIntent) auprès de Stripe pour un panier donné.
     * @param cart Le panier pour lequel créer le paiement.
     * @return L'objet PaymentIntent créé.
     * @throws StripeException en cas d'erreur lors de l'appel à l'API Stripe.
     * @deprecated Utiliser createPaymentIntent(Order) à la place.
     */
    @Deprecated
    public PaymentIntent createPaymentIntent(Cart cart) throws StripeException {
        log.debug("Création d'un PaymentIntent pour le panier ID: {}", cart.getId());
        
        long amount = (long) (calculateTotal(cart) * 100); // Stripe utilise les centimes
        log.debug("Montant calculé pour Stripe: {} cents", amount);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("eur")
                .putMetadata("cart_id", cart.getId().toString())
                .putMetadata("user_id", cart.getUser().getId().toString())
                .build();

        PaymentIntent intent = PaymentIntent.create(params);
        log.info("PaymentIntent créé avec succès: {}", intent.getId());
        return intent;
    }

    /**
     * Calcule le montant total du panier pour Stripe.
     * @param cart Le panier.
     * @return Le montant total.
     */
    private Double calculateTotal(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> {
                    Double price = Double.parseDouble(item.getProduct().getReferencePrice() != null ? item.getProduct().getReferencePrice() : "0");
                    return price * item.getQuantity();
                })
                .sum();
    }
}
