package com.tradeall.tradefood.entity;

import com.tradeall.tradefood.model.payment.PaymentRequest;
import com.tradeall.tradefood.model.payment.PaymentStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.stripe.model.PaymentIntent;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment implements Serializable {

    @Id
    private String id; // This will be the Stripe PaymentIntent ID

    private String paymentIntent;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status_enum", length = 50)
    private PaymentStatusEnum paymentStatusEnum;

    private long amount;

    private UUID userId;

    private String customerId;

    private String description;

    private String idempotencyKey;

    private String proId;

    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    private User user;

    private LocalDateTime paymentDate;

    private Long createdAt;
    private Long paidAt;
    private Long nextPayoutDate;

    @Column(unique = true)
    private String stripeAccountId;
    
    private long transferAmount;
    private String transferId;
    private String payoutId;

    public Payment() {
    }

    public Payment(PaymentRequest paymentRequest, PaymentIntent paymentIntent, String idempotencyKey) {
        this.id = paymentIntent.getId();
        this.paymentIntent = paymentIntent.getId();
        if (paymentRequest.getUserId() != null) {
            // Note: In the provided code it was Long, here it's UUID
            // We'll handle this in the service
        }
        this.amount = paymentIntent.getAmount();
        this.idempotencyKey = idempotencyKey;
        this.proId = paymentRequest.getProId();
        this.customerId = paymentRequest.getCustomerId();
        this.description = paymentRequest.getDescription();
        this.paymentStatusEnum = PaymentStatusEnum.PENDING;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentIntent() {
        return paymentIntent;
    }

    public void setPaymentIntent(String paymentIntent) {
        this.paymentIntent = paymentIntent;
    }

    public PaymentStatusEnum getPaymentStatusEnum() {
        return paymentStatusEnum;
    }

    public void setPaymentStatusEnum(PaymentStatusEnum paymentStatusEnum) {
        this.paymentStatusEnum = paymentStatusEnum;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setAmount(double amount) {
        this.amount = (long) (amount * 100);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getId();
        }
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Long paidAt) {
        this.paidAt = paidAt;
    }

    public Long getNextPayoutDate() {
        return nextPayoutDate;
    }

    public void setNextPayoutDate(Long nextPayoutDate) {
        this.nextPayoutDate = nextPayoutDate;
    }

    public String getStripeAccountId() {
        return stripeAccountId;
    }

    public void setStripeAccountId(String stripeAccountId) {
        this.stripeAccountId = stripeAccountId;
    }

    public long getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(long transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getPayoutId() {
        return payoutId;
    }

    public void setPayoutId(String payoutId) {
        this.payoutId = payoutId;
    }
}
