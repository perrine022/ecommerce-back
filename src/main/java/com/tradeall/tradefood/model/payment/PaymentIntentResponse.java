package com.tradeall.tradefood.model.payment;

public class PaymentIntentResponse {
    private String paymentIntentId;
    private String clientSecret;
    private String ephemeralKey;
    private String publishableKey;

    public PaymentIntentResponse() {}

    public PaymentIntentResponse(String paymentIntentId, String clientSecret, String ephemeralKey, String publishableKey) {
        this.paymentIntentId = paymentIntentId;
        this.clientSecret = clientSecret;
        this.ephemeralKey = ephemeralKey;
        this.publishableKey = publishableKey;
    }

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getEphemeralKey() {
        return ephemeralKey;
    }

    public void setEphemeralKey(String ephemeralKey) {
        this.ephemeralKey = ephemeralKey;
    }

    public String getPublishableKey() {
        return publishableKey;
    }

    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }
}
