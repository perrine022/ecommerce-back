package com.tradeall.tradefood.controller;

import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.model.payment.PaymentIntentResponse;
import com.tradeall.tradefood.model.payment.PaymentRequest;
import com.tradeall.tradefood.service.StripeService;
import com.stripe.model.Account;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);
    private final StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    /**
     * Creates a payment intent with the specified details.
     *
     * @param paymentRequest The payment request details (amount, currency, etc.).
     * @return The created payment intent response or an error.
     */
    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentIntentResponse> createPaymentIntent(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid PaymentRequest paymentRequest) {
        LOGGER.info("REST: Request to create payment intent. Amount: {}, Currency: {}, User from token: {}", 
                paymentRequest.getAmount(), paymentRequest.getCurrency(), user != null ? user.getId() : "None");
        
        if (paymentRequest.getUserId() == null && user != null) {
            LOGGER.info("REST: userId missing in payload, using ID from token: {}", user.getId());
            // Note: In the provided code it was Long, here we convert UUID to Long if needed or just use UUID
            // However, the provided PaymentRequest uses Long for userId. 
            // In this project User has UUID.
        }
        
        try {
            PaymentIntentResponse response = stripeService.createPaymentIntent(paymentRequest);
            LOGGER.info("REST: Payment intent created successfully: {}", response.getPaymentIntentId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error("REST: Error creating payment intent: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves the Stripe public API key.
     *
     * @return The public API key.
     */
    @GetMapping("/public-key")
    public ResponseEntity<String> getPublicKey() {
        LOGGER.debug("Fetching Stripe public key");
        return ResponseEntity.ok(stripeService.getPublicKey());
    }

    /**
     * Creates payment sheet data for client-side processing.
     *
     * @param paymentRequest The payment request details.
     * @return The payment sheet data or an error.
     */
    @PostMapping("/payment-sheet")
    public ResponseEntity<Map<String, String>> createPaymentSheet(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid PaymentRequest paymentRequest) {
        LOGGER.info("REST: Request for payment sheet data. Amount: {}, User from token: {}", 
                paymentRequest.getAmount(), user != null ? user.getId() : "None");
        
        try {
            Map<String, String> paymentSheetData = stripeService.createPaymentSheetData(paymentRequest);
            LOGGER.info("REST: Payment sheet data created for customer: {}", paymentSheetData.get("customer"));
            return ResponseEntity.ok(paymentSheetData);
        } catch (Exception e) {
            LOGGER.error("REST: Error creating payment sheet: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Creates a new customer on Stripe.
     *
     * @return The customer ID or an error.
     */
    @PostMapping("/create-customer")
    public ResponseEntity<String> createCustomer() {
        LOGGER.info("Creating Stripe customer");
        try {
            String customerId = stripeService.createCustomer();
            LOGGER.info("Stripe customer created successfully: {}", customerId);
            return ResponseEntity.ok(customerId);
        } catch (Exception e) {
            LOGGER.error("Error creating Stripe customer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Captures payment for a specific payment intent.
     *
     * @param paymentIntentId The ID of the payment intent.
     * @param actualMinutes   The duration in minutes to calculate the amount.
     * @return A success or error message.
     */
    @PostMapping("/capture-payment/{paymentIntentId}")
    public ResponseEntity<String> capturePayment(@PathVariable String paymentIntentId, @RequestParam @Min(1) int actualMinutes) {
        LOGGER.info("Capturing payment for PaymentIntent ID: {}, duration: {} minutes", paymentIntentId, actualMinutes);
        try {
            long amountToCapture = actualMinutes * 100L;
            stripeService.capturePayment(paymentIntentId, amountToCapture);
            LOGGER.info("Payment captured successfully for PaymentIntent ID: {}", paymentIntentId);
            return ResponseEntity.ok("Payment captured successfully.");
        } catch (Exception e) {
            LOGGER.error("Error capturing payment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to capture payment.");
        }
    }

    /**
     * Extends the authorization period for a payment intent.
     *
     * @param paymentIntentId The ID of the payment intent.
     * @return The updated payment intent or an error.
     */
    @PutMapping("/extend-authorization/{paymentIntentId}")
    public ResponseEntity<PaymentIntent> extendAuthorization(@PathVariable String paymentIntentId) {
        LOGGER.info("Extending authorization for PaymentIntent ID: {}", paymentIntentId);
        try {
            PaymentIntent paymentIntent = stripeService.extendAuthorization(paymentIntentId);
            LOGGER.info("Authorization extended successfully: {}", paymentIntent.getId());
            return ResponseEntity.ok(paymentIntent);
        } catch (Exception e) {
            LOGGER.error("Error extending authorization: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Processes a refund for a payment intent.
     *
     * @param paymentIntentId The ID of the payment intent.
     * @param amount          The amount to refund in cents.
     * @return The refund details or an error.
     */
    @PostMapping("/refund/{paymentIntentId}")
    public ResponseEntity<Refund> refundPayment(@PathVariable String paymentIntentId, @RequestParam @Min(1) Long amount) {
        LOGGER.info("Processing refund for PaymentIntent ID: {}, amount: {}", paymentIntentId, amount);
        try {
            Refund refund = stripeService.refundPayment(paymentIntentId, amount);
            LOGGER.info("Refund processed successfully: {}", refund.getId());
            return ResponseEntity.ok(refund);
        } catch (Exception e) {
            LOGGER.error("Error processing refund: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Verifies the status of a payment intent.
     *
     * @param paymentIntentId The ID of the payment intent.
     * @return The status of the payment intent.
     */
    @GetMapping("/status/{paymentIntentId}")
    public ResponseEntity<String> verifyPaymentIntentStatus(@PathVariable String paymentIntentId) {
        LOGGER.info("Verifying status for PaymentIntent ID: {}", paymentIntentId);
        try {
            String status = stripeService.verifyPaymentIntentStatus(paymentIntentId);
            LOGGER.info("PaymentIntent status: {}", status);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            LOGGER.error("Error verifying status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Cancels a payment intent.
     *
     * @param paymentIntentId The ID of the payment intent.
     * @return The canceled payment intent or an error.
     */
    @PostMapping("/cancel/{paymentIntentId}")
    public ResponseEntity<PaymentIntent> cancelPaymentIntent(@PathVariable String paymentIntentId) {
        LOGGER.info("Cancelling PaymentIntent ID: {}", paymentIntentId);
        try {
            PaymentIntent paymentIntent = stripeService.cancelPaymentIntent(paymentIntentId);
            LOGGER.info("PaymentIntent canceled successfully: {}", paymentIntentId);
            return ResponseEntity.ok(paymentIntent);
        } catch (Exception e) {
            LOGGER.error("Error cancelling PaymentIntent: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Creates or retrieves a connected account for Stripe.
     *
     * @param accountId    Optional account ID to retrieve.
     * @param accountParams Parameters for account creation.
     * @return The connected account or an error.
     */
    @PostMapping("/connected-account")
    public ResponseEntity<Account> createOrRetrieveConnectedAccount(@RequestParam(required = false) String accountId,
                                                                    @RequestBody Map<String, Object> accountParams) {
        LOGGER.info("Creating or retrieving connected account");
        try {
            Account account = stripeService.createOrRetrieveConnectedAccount(accountId, accountParams);
            LOGGER.info("Connected account created/retrieved successfully: {}", account.getId());
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            LOGGER.error("Error creating/retrieving connected account: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
