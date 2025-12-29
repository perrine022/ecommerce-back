package com.tradeall.tradefood.controller;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.tradeall.tradefood.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur gérant les webhooks Stripe pour confirmer les paiements et finaliser les commandes.
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/api/v1/webhooks")
public class StripeWebhookController {

    private static final Logger log = LoggerFactory.getLogger(StripeWebhookController.class);
    private final OrderService orderService;

    public StripeWebhookController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    /**
     * Endpoint recevant les événements Stripe.
     * @param payload Le corps de la requête Stripe.
     * @param sigHeader L'en-tête de signature Stripe pour vérification.
     * @return Réponse HTTP.
     */
    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        log.info("Réception d'un webhook Stripe");
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            log.debug("Événement Stripe construit avec succès: {}", event.getType());
        } catch (Exception e) {
            log.error("Erreur lors de la construction de l'événement Stripe: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Webhook Error");
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
            if (paymentIntent != null) {
                log.info("Paiement réussi pour PaymentIntent ID: {}", paymentIntent.getId());
                String orderIdStr = paymentIntent.getMetadata().get("order_id");
                
                if (orderIdStr != null) {
                    java.util.UUID orderId = java.util.UUID.fromString(orderIdStr);
                    log.info("Finalisation de la commande ID: {} suite au paiement Stripe", orderId);
                    try {
                        orderService.finalizeOrder(orderId);
                        log.info("Commande ID: {} finalisée avec succès", orderId);
                    } catch (Exception e) {
                        log.error("Erreur lors de la finalisation de la commande ID: {}: {}", orderId, e.getMessage());
                        // On retourne quand même un 200 à Stripe pour éviter les retries si on considère l'erreur comme gérée
                    }
                } else {
                    log.error("Aucun order_id trouvé dans les métadonnées du PaymentIntent ID: {}", paymentIntent.getId());
                }
            }
        } else {
            log.debug("Événement Stripe ignoré: {}", event.getType());
        }

        return ResponseEntity.ok().build();
    }
}
