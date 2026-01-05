package com.tradeall.tradefood.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.ApiResource;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import com.stripe.param.*;
import com.tradeall.tradefood.entity.Payment;
import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.model.payment.PaymentIntentResponse;
import com.tradeall.tradefood.model.payment.PaymentRequest;
import com.tradeall.tradefood.model.payment.PaymentStatusEnum;
import com.tradeall.tradefood.repository.PaymentRepository;
import com.tradeall.tradefood.repository.UserRepository;
import com.tradeall.tradefood.utils.IdempotencyKeyUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class StripeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StripeService.class);

    @Value("${stripe.secretKey}")
    private String stripeSecretKey;

    @Value("${stripe.publicKey}")
    private String stripePublishableKey;

    @Value("${stripe.webhookSecret}")
    private String webhookSecret;

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public StripeService(PaymentRepository paymentRepository, 
                         UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    /**
     * Dynamically switch the Stripe API key.
     *
     * @param apiKey The new API key to set.
     */
    public void setApiKey(String apiKey) {
        Stripe.apiKey = apiKey;
    }

    /**
     * Creates a new Payment Intent based on the provided payment request.
     * Handles both automatic and manual capture methods.
     *
     * @param paymentRequest The details of the payment request.
     * @return The response containing the Payment Intent details.
     */
    public PaymentIntentResponse createPaymentIntent(PaymentRequest paymentRequest) {
        String idempotencyKey = IdempotencyKeyUtil.generate("create_payment_intent", paymentRequest.getUserId(), paymentRequest.getAmount());
        LOGGER.info("Stripe: Creating PaymentIntent for user: {}, amount: {}, currency: {}", 
                paymentRequest.getUserId(), paymentRequest.getAmount(), paymentRequest.getCurrency());
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(paymentRequest.getAmount())
                    .setCurrency(paymentRequest.getCurrency())
                    .setDescription(paymentRequest.getDescription())
                    .setReceiptEmail(paymentRequest.getReceiptEmail())
                    .setCaptureMethod(paymentRequest.isCaptureImmediately() ? PaymentIntentCreateParams.CaptureMethod.AUTOMATIC : PaymentIntentCreateParams.CaptureMethod.MANUAL)
                    .setCustomer(paymentRequest.getCustomerId())
                    .putAllMetadata(paymentRequest.getMetadata() != null ? paymentRequest.getMetadata() : new HashMap<>())
                    .build();

            RequestOptions requestOptions = RequestOptions.builder()
                    .setIdempotencyKey(idempotencyKey)
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params, requestOptions);

            LOGGER.info("Stripe: PaymentIntent created successfully. ID: {}, status: {}", paymentIntent.getId(), paymentIntent.getStatus());
            long totalAmount = paymentRequest.getAmount();
            long transferAmount = Math.round(totalAmount * 0.80);

            Payment payment = new Payment(paymentRequest, paymentIntent, idempotencyKey);
            payment.setAmount(totalAmount);
            payment.setTransferAmount(transferAmount);
            payment.setCreatedAt(System.currentTimeMillis());
            
            if (paymentRequest.getUserId() != null) {
                userRepository.findById(paymentRequest.getUserId()).ifPresent(user -> {
                    payment.setUser(user);
                    payment.setUserId(user.getId());
                });
            }
            
            paymentRepository.save(payment);
            LOGGER.info("Stripe: Local payment record saved for ID: {}", payment.getId());

            String ephemeralKeySecret = generateEphemeralKey(paymentRequest.getCustomerId());
            return new PaymentIntentResponse(paymentIntent.getId(), paymentIntent.getClientSecret(), ephemeralKeySecret, stripePublishableKey);
        } catch (StripeException e) {
            LOGGER.error("Stripe: Failed to create PaymentIntent. Idempotency Key: {}. Error: {}", idempotencyKey, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Stripe API error", e);
        }
    }

    public void markPaymentAsPaidForUser(UUID userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        if (!payments.isEmpty()) {
            // On prend le dernier paiement créé
            Payment payment = payments.get(payments.size() - 1);
            long now = System.currentTimeMillis();
            payment.setPaidAt(now);
            payment.setNextPayoutDate(now + 14 * 24 * 60 * 60 * 1000L);
            payment.setPaymentStatusEnum(PaymentStatusEnum.PAID_SCHEDULED);
            paymentRepository.save(payment);
            LOGGER.info("\u2705 Payment marked as paid for userId {}, payout at {}", userId, payment.getNextPayoutDate());
        } else {
            LOGGER.warn("\u26A0 No payment found for userId {}", userId);
        }
    }

    public String createCustomer() throws StripeException {
        CustomerCreateParams customerParams = CustomerCreateParams.builder().build();
        Customer customer = Customer.create(customerParams);
        return customer.getId();
    }

    public String getPublicKey() {
        return stripePublishableKey;
    }

    public Map<String, String> createPaymentSheetData(PaymentRequest paymentRequest) throws StripeException {
        LOGGER.info("Stripe: Preparing PaymentSheet data for user: {}, amount: {}", 
                paymentRequest.getUserId(), paymentRequest.getAmount());
        
        // 1. Crée un nouveau Customer si aucun ID n'est fourni ou utilise celui fourni
        String customerId = paymentRequest.getCustomerId();
        if (customerId == null || customerId.isEmpty()) {
            LOGGER.info("Stripe: customerId is null, creating a new Stripe Customer...");
            CustomerCreateParams customerParams = CustomerCreateParams.builder().build();
            Customer customer = Customer.create(customerParams);
            customerId = customer.getId();
            LOGGER.info("Stripe: New Customer created with ID: {}", customerId);
        } else {
            LOGGER.info("Stripe: Using existing customerId: {}", customerId);
        }

        // 2. Génère une EphemeralKey pour le Customer
        LOGGER.info("Stripe: Generating EphemeralKey for customer: {}", customerId);
        EphemeralKeyCreateParams ephemeralKeyParams = EphemeralKeyCreateParams.builder()
                .setStripeVersion("2024-09-30.acacia")
                .setCustomer(customerId)
                .build();
        EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyParams);

        // 3. Crée un PaymentIntent avec les méthodes de paiement automatiques
        LOGGER.info("Stripe: Creating PaymentIntent for PaymentSheet...");
        PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                .setAmount(paymentRequest.getAmount())
                .setCurrency(paymentRequest.getCurrency())
                .setCustomer(customerId)
                .addPaymentMethodType("card")
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);
        LOGGER.info("Stripe: PaymentIntent created for PaymentSheet. ID: {}", paymentIntent.getId());

        // 4. Retourne les informations nécessaires pour le PaymentSheet
        Map<String, String> responseData = new HashMap<>();
        responseData.put("paymentIntent", paymentIntent.getClientSecret());
        responseData.put("ephemeralKey", ephemeralKey.getSecret());
        responseData.put("customer", customerId);
        responseData.put("publishableKey", stripePublishableKey);

        LOGGER.info("Stripe: PaymentSheet data generation complete.");
        return responseData;
    }

    private String generateEphemeralKey(String customerId) {
        if (customerId == null || customerId.isEmpty()) return null;
        try {
            EphemeralKeyCreateParams params = EphemeralKeyCreateParams.builder()
                    .setCustomer(customerId)
                    .build();

            RequestOptions requestOptions = RequestOptions.builder()
                    .setApiKey(stripeSecretKey)
                    .build();

            EphemeralKey ephemeralKey = EphemeralKey.create(params, requestOptions);

            return ephemeralKey.getSecret();
        } catch (StripeException e) {
            LOGGER.error("Failed to create ephemeral key for customer ID {}: {}", customerId, e.getMessage(), e);
            throw new RuntimeException("Failed to create ephemeral key", e);
        }
    }

    public PaymentIntent capturePayment(String paymentIntentId, long amount) {
        String idempotencyKey = IdempotencyKeyUtil.generate("capture_payment", paymentIntentId, amount);
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            PaymentIntentCaptureParams params = PaymentIntentCaptureParams.builder()
                    .setAmountToCapture(amount)
                    .build();

            RequestOptions requestOptions = RequestOptions.builder()
                    .setIdempotencyKey(idempotencyKey)
                    .build();

            PaymentIntent capturedIntent = paymentIntent.capture(params, requestOptions);

            updatePaymentStatus(paymentIntentId, PaymentStatusEnum.CAPTURED);

            LOGGER.info("PaymentIntent captured successfully with ID: {} and Idempotency Key: {}", paymentIntentId, idempotencyKey);
            return capturedIntent;
        } catch (StripeException e) {
            LOGGER.error("Failed to capture PaymentIntent with Idempotency Key: {}. Error: {}", idempotencyKey, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Stripe API error", e);
        }
    }

    private void updatePaymentStatus(String paymentIntentId, PaymentStatusEnum status) {
        Optional<Payment> payment = paymentRepository.findById(paymentIntentId);
        if (payment.isPresent()) {
            Payment payment1 = payment.get();
            payment1.setPaymentStatusEnum(status);
            paymentRepository.save(payment1);
            LOGGER.info("Payment status updated to {} for PaymentIntent ID: {}", status, paymentIntentId);
        } else {
            LOGGER.warn("Payment not found for PaymentIntent ID: {}", paymentIntentId);
        }
    }

    public PaymentIntent extendAuthorization(String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            PaymentIntentUpdateParams params = PaymentIntentUpdateParams.builder()
                    .putMetadata("extended", "true")
                    .build();

            String idempotencyKey = IdempotencyKeyUtil.generate("extend_authorization", paymentIntentId);
            RequestOptions requestOptions = RequestOptions.builder()
                    .setIdempotencyKey(idempotencyKey)
                    .build();

            PaymentIntent update = paymentIntent.update(params, requestOptions);
            updatePaymentStatus(paymentIntentId, PaymentStatusEnum.AUTHORIZED_EXTENDED);
            return update;
        } catch (StripeException e) {
            LOGGER.error("Failed to extend authorization for PaymentIntent ID {}: {}", paymentIntentId, e.getMessage(), e);
            throw new RuntimeException("Failed to extend authorization", e);
        }
    }

    public Refund refundPayment(String paymentIntentId, Long amount) {
        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setPaymentIntent(paymentIntentId)
                    .setAmount(amount)
                    .build();

            String idempotencyKey = IdempotencyKeyUtil.generate("refund_payment", paymentIntentId, amount);
            RequestOptions requestOptions = RequestOptions.builder()
                    .setIdempotencyKey(idempotencyKey)
                    .build();

            Refund refund = Refund.create(params, requestOptions);
            updatePaymentStatus(paymentIntentId, PaymentStatusEnum.REFUNDED);
            return refund;
        } catch (StripeException e) {
            LOGGER.error("Failed to refund PaymentIntent ID {}: {}", paymentIntentId, e.getMessage(), e);
            throw new RuntimeException("Failed to process refund", e);
        }
    }

    public PaymentIntent retrievePaymentIntent(String paymentIntentId) {
        try {
            return PaymentIntent.retrieve(paymentIntentId);
        } catch (StripeException e) {
            LOGGER.error("Failed to retrieve PaymentIntent ID {}: {}", paymentIntentId, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve PaymentIntent", e);
        }
    }

    public String verifyPaymentIntentStatus(String paymentIntentId) {
        PaymentIntent paymentIntent = retrievePaymentIntent(paymentIntentId);
        String status = paymentIntent.getStatus();
        
        if ("succeeded".equals(status)) {
            LOGGER.info("Stripe: PaymentIntent {} succeeded, checking if user needs update", paymentIntentId);
            
            // Case 1: One-shot payment (original flow)
            Optional<Payment> optPayment = paymentRepository.findById(paymentIntentId);
            if (optPayment.isPresent()) {
                Payment payment = optPayment.get();
                if (payment.getPaymentStatusEnum() != PaymentStatusEnum.PAID_SCHEDULED && payment.getPaymentStatusEnum() != PaymentStatusEnum.CAPTURED) {
                    LOGGER.info("Stripe: Updating status for one-shot PaymentIntent {} and user {}", paymentIntentId, payment.getUserId());
                    updatePaymentStatus(paymentIntentId, PaymentStatusEnum.CAPTURED);
                    
                    if (payment.getUserId() != null) {
                        userRepository.findById(payment.getUserId()).ifPresent(user -> {
                            user.setPremiumEnabled(true);
                            user.setSubscriptionStatus(com.tradeall.tradefood.model.SubscriptionStatus.ACTIVE);
                            user.setSubscriptionDate(java.time.LocalDateTime.now());
                            userRepository.save(user);
                            LOGGER.info("Stripe: Premium activated via one-shot PI for user {}", user.getId());
                        });
                    }
                }
            }
        }
        return status;
    }

    public void handleWebhook(String payload, String sigHeader) {
        Event event;
        try {
            if (webhookSecret != null && !webhookSecret.isEmpty() && sigHeader != null) {
                event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            } else {
                LOGGER.warn("Stripe Webhook: Skipping signature verification (secret or header missing)");
                // In some versions of stripe-java, ApiResource.GSON is not easily accessible.
                // We'll rely on construction via Webhook or skip if not possible.
                return;
            }
        } catch (Exception e) {
            LOGGER.warn("Stripe: Webhook signature verification failed: {}", e.getMessage());
            return;
        }

        LOGGER.info("Stripe Webhook: Received event type: {}, ID: {}", event.getType(), event.getId());

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = dataObjectDeserializer.getObject().orElse(null);

        switch (event.getType()) {
            case "payment_intent.succeeded":
                handlePaymentSuccess((PaymentIntent) stripeObject);
                break;
            case "payment_intent.payment_failed":
                handlePaymentFailed((PaymentIntent) stripeObject);
                break;
            default:
                LOGGER.info("Stripe Webhook: Unhandled event type: {}", event.getType());
        }
    }

    private void handlePaymentSuccess(PaymentIntent paymentIntent) {
        String paymentIntentId = paymentIntent.getId();
        Optional<Payment> optPayment = paymentRepository.findById(paymentIntentId);
        
        if (optPayment.isPresent()) {
            Payment payment = optPayment.get();
            if (payment.getPaymentStatusEnum() != PaymentStatusEnum.CAPTURED && payment.getPaymentStatusEnum() != PaymentStatusEnum.PAID_SCHEDULED) {
                LOGGER.info("Stripe Webhook: Updating one-shot payment {} to CAPTURED and activating user {}", 
                        paymentIntentId, payment.getUserId());
                
                updatePaymentStatus(paymentIntentId, PaymentStatusEnum.CAPTURED);
                
                // Activate Premium for User (one-shot case)
                if (payment.getUserId() != null) {
                    userRepository.findById(payment.getUserId()).ifPresent(user -> {
                        user.setPremiumEnabled(true);
                        user.setSubscriptionStatus(com.tradeall.tradefood.model.SubscriptionStatus.ACTIVE);
                        user.setSubscriptionDate(java.time.LocalDateTime.now());
                        userRepository.save(user);
                        LOGGER.info("Stripe Webhook: Premium activated via one-shot for user {}", user.getId());
                    });
                }
            }
        }
    }

    private void handlePaymentFailed(PaymentIntent paymentIntent) {
        LOGGER.warn("Stripe Webhook: Payment failed for ID: {}. Error: {}", 
                paymentIntent.getId(), paymentIntent.getLastPaymentError().getMessage());
        updatePaymentStatus(paymentIntent.getId(), PaymentStatusEnum.FAILED);
    }

    public PaymentIntent cancelPaymentIntent(String paymentIntentId) {
        try {
            PaymentIntent cancel = PaymentIntent.retrieve(paymentIntentId).cancel();
            updatePaymentStatus(paymentIntentId, PaymentStatusEnum.CANCELED);
            return cancel;
        } catch (StripeException e) {
            LOGGER.error("Failed to cancel PaymentIntent ID {}: {}", paymentIntentId, e.getMessage(), e);
            throw new RuntimeException("Failed to cancel PaymentIntent", e);
        }
    }

    public Account createOrRetrieveConnectedAccount(String accountId, Map<String, Object> accountParams) {
        try {
            if (accountId != null && !accountId.isEmpty()) {
                return Account.retrieve(accountId);
            } else {
                return Account.create(accountParams);
            }
        } catch (StripeException e) {
            LOGGER.error("Failed to create or retrieve connected account: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to handle connected account", e);
        }
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void extendExpiringAuthorizations() {
        try {
            LOGGER.info("Starting batch job to extend expiring authorizations.");

            List<PaymentIntent> expiringIntents = findExpiringPaymentIntents();

            for (PaymentIntent paymentIntent : expiringIntents) {
                extendAuthorization(paymentIntent.getId());
                LOGGER.info("Extended authorization for PaymentIntent ID: {}", paymentIntent.getId());
            }

            LOGGER.info("Batch job completed successfully.");
        } catch (Exception e) {
            LOGGER.error("Error occurred during the batch job for extending authorizations.", e);
        }
    }

    private List<PaymentIntent> findExpiringPaymentIntents() throws StripeException {
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        long twoDaysInSeconds = 2 * 24 * 60 * 60;
        long expirationThreshold = currentTimeInSeconds + twoDaysInSeconds;

        List<PaymentIntent> expiringIntents = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 100);
        params.put("status", "requires_capture");

        PaymentIntentCollection paymentIntents = PaymentIntent.list(params);

        for (PaymentIntent paymentIntent : paymentIntents.getData()) {
            String authorizationTimeStr = paymentIntent.getMetadata().get("authorization_time");

            if (authorizationTimeStr != null) {
                long authorizationTime = Long.parseLong(authorizationTimeStr);
                long authorizationExpirationTime = authorizationTime + 30 * 24 * 60 * 60;

                if (authorizationExpirationTime <= expirationThreshold) {
                    expiringIntents.add(paymentIntent);
                }
            } else {
                LOGGER.warn("PaymentIntent ID {} does not have authorization_time metadata.", paymentIntent.getId());
            }
        }

        return expiringIntents;
    }
}
