package com.tradeall.tradefood.model.payment;

import java.io.Serializable;
import java.util.Map;
public class PaymentRequest implements Serializable {
    private Long amount;                // Amount in smallest currency unit (e.g., cents)
    private String currency;            // Currency code (e.g., "eur")
    private String paymentMethodId;     // ID of the payment method
    private String customerId;          // Stripe customer ID
    private String description;
    private String receiptEmail;
    private Map<String, String> metadata;
    private boolean captureImmediately;
    private java.util.UUID userId;                // Linked to our User entity
    private String proId;
    private String priceId;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiptEmail() {
        return receiptEmail;
    }

    public void setReceiptEmail(String receiptEmail) {
        this.receiptEmail = receiptEmail;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public boolean isCaptureImmediately() {
        return captureImmediately;
    }

    public void setCaptureImmediately(boolean captureImmediately) {
        this.captureImmediately = captureImmediately;
    }

    public java.util.UUID getUserId() {
        return userId;
    }

    public void setUserId(java.util.UUID userId) {
        this.userId = userId;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }
}
