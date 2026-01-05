package com.tradeall.tradefood.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Column(name = "total_amount")
    private Double totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private OrderStatus status;

    @Column(name = "number")
    private String number;

    @Column(name = "sellsy_status")
    private String sellsyStatus;

    @Column(name = "sellsy_order_status")
    private String sellsyOrderStatus;

    @Column(name = "date")
    private String date;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "created")
    private String created;

    @Column(name = "subject")
    private String subject;
    
    // Amounts
    @Column(name = "total_raw_excl_tax")
    private String totalRawExclTax;

    @Column(name = "total_after_discount_excl_tax")
    private String totalAfterDiscountExclTax;

    @Column(name = "total_packaging")
    private String totalPackaging;

    @Column(name = "total_shipping")
    private String totalShipping;

    @Column(name = "total_excl_tax")
    private String totalExclTax;

    @Column(name = "total_incl_tax")
    private String totalInclTax;
    
    @Column(name = "currency")
    private String currency;

    @Column(name = "fiscal_year_id")
    private Long fiscalYearId;

    @Column(name = "pdf_link")
    private String pdfLink;
    
    @Column(name = "assigned_staff_id")
    private Long assignedStaffId;

    @Column(name = "invoicing_address_id")
    private Long invoicingAddressId;

    @Column(name = "delivery_address_id")
    private Long deliveryAddressId;

    @Column(name = "issuer_address_id")
    private Long issuerAddressId;

    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "rate_category_id")
    private Long rateCategoryId;
    
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
    
    @Column(name = "shipping_date")
    private String shippingDate;

    @Column(name = "company_reference")
    private String companyReference;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "bank_account_id")
    private Long bankAccountId;

    @Column(name = "eco_tax_id")
    private Long ecoTaxId;

    @Column(name = "check_label_id")
    private Long checkLabelId;

    @Column(name = "vat_mode")
    private String vatMode;

    @Column(name = "vat_mention")
    private String vatMention;

    @Column(name = "shipping_weight_unit")
    private String shippingWeightUnit;

    @Column(name = "shipping_weight_value")
    private String shippingWeightValue;

    @Column(name = "shipping_volume")
    private String shippingVolume;

    @Column(name = "validation_code")
    private String validationCode;

    @Column(name = "is_validated")
    private boolean validated = false;

    @Column(name = "sellsy_order_id")
    private String sellsyOrderId;

    @Column(name = "sellsy_order_doc_type")
    private String sellsyOrderDocType; // estimate, order, invoice

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    public enum OrderStatus {
        PENDING,
        PAID,
        CANCELLED,
        SHIPPED
    }

    public Order() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getSellsyOrderId() { return sellsyOrderId; }
    public void setSellsyOrderId(String sellsyOrderId) { this.sellsyOrderId = sellsyOrderId; }
    public String getSellsyOrderDocType() { return sellsyOrderDocType; }
    public void setSellsyOrderDocType(String sellsyOrderDocType) { this.sellsyOrderDocType = sellsyOrderDocType; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getSellsyStatus() { return sellsyStatus; }
    public void setSellsyStatus(String sellsyStatus) { this.sellsyStatus = sellsyStatus; }
    public String getSellsyOrderStatus() { return sellsyOrderStatus; }
    public void setSellsyOrderStatus(String sellsyOrderStatus) { this.sellsyOrderStatus = sellsyOrderStatus; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getTotalRawExclTax() { return totalRawExclTax; }
    public void setTotalRawExclTax(String totalRawExclTax) { this.totalRawExclTax = totalRawExclTax; }
    public String getTotalAfterDiscountExclTax() { return totalAfterDiscountExclTax; }
    public void setTotalAfterDiscountExclTax(String totalAfterDiscountExclTax) { this.totalAfterDiscountExclTax = totalAfterDiscountExclTax; }
    public String getTotalPackaging() { return totalPackaging; }
    public void setTotalPackaging(String totalPackaging) { this.totalPackaging = totalPackaging; }
    public String getTotalShipping() { return totalShipping; }
    public void setTotalShipping(String totalShipping) { this.totalShipping = totalShipping; }
    public String getTotalExclTax() { return totalExclTax; }
    public void setTotalExclTax(String totalExclTax) { this.totalExclTax = totalExclTax; }
    public String getTotalInclTax() { return totalInclTax; }
    public void setTotalInclTax(String totalInclTax) { this.totalInclTax = totalInclTax; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Long getFiscalYearId() { return fiscalYearId; }
    public void setFiscalYearId(Long fiscalYearId) { this.fiscalYearId = fiscalYearId; }
    public String getPdfLink() { return pdfLink; }
    public void setPdfLink(String pdfLink) { this.pdfLink = pdfLink; }
    public Long getAssignedStaffId() { return assignedStaffId; }
    public void setAssignedStaffId(Long assignedStaffId) { this.assignedStaffId = assignedStaffId; }
    public Long getInvoicingAddressId() { return invoicingAddressId; }
    public void setInvoicingAddressId(Long invoicingAddressId) { this.invoicingAddressId = invoicingAddressId; }
    public Long getDeliveryAddressId() { return deliveryAddressId; }
    public void setDeliveryAddressId(Long deliveryAddressId) { this.deliveryAddressId = deliveryAddressId; }
    public Long getIssuerAddressId() { return issuerAddressId; }
    public void setIssuerAddressId(Long issuerAddressId) { this.issuerAddressId = issuerAddressId; }
    public Long getContactId() { return contactId; }
    public void setContactId(Long contactId) { this.contactId = contactId; }
    public Long getRateCategoryId() { return rateCategoryId; }
    public void setRateCategoryId(Long rateCategoryId) { this.rateCategoryId = rateCategoryId; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getShippingDate() { return shippingDate; }
    public void setShippingDate(String shippingDate) { this.shippingDate = shippingDate; }
    public String getCompanyReference() { return companyReference; }
    public void setCompanyReference(String companyReference) { this.companyReference = companyReference; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Long getBankAccountId() { return bankAccountId; }
    public void setBankAccountId(Long bankAccountId) { this.bankAccountId = bankAccountId; }
    public Long getEcoTaxId() { return ecoTaxId; }
    public void setEcoTaxId(Long ecoTaxId) { this.ecoTaxId = ecoTaxId; }
    public Long getCheckLabelId() { return checkLabelId; }
    public void setCheckLabelId(Long checkLabelId) { this.checkLabelId = checkLabelId; }
    public String getVatMode() { return vatMode; }
    public void setVatMode(String vatMode) { this.vatMode = vatMode; }
    public String getVatMention() { return vatMention; }
    public void setVatMention(String vatMention) { this.vatMention = vatMention; }
    public String getShippingWeightUnit() { return shippingWeightUnit; }
    public void setShippingWeightUnit(String shippingWeightUnit) { this.shippingWeightUnit = shippingWeightUnit; }
    public String getShippingWeightValue() { return shippingWeightValue; }
    public void setShippingWeightValue(String shippingWeightValue) { this.shippingWeightValue = shippingWeightValue; }
    public String getShippingVolume() { return shippingVolume; }
    public void setShippingVolume(String shippingVolume) { this.shippingVolume = shippingVolume; }

    public String getValidationCode() { return validationCode; }
    public void setValidationCode(String validationCode) { this.validationCode = validationCode; }

    public boolean isValidated() { return validated; }
    public void setValidated(boolean validated) { this.validated = validated; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {
        private User user;
        private LocalDateTime orderDate;
        private Double totalAmount;
        private OrderStatus status;
        private String sellsyOrderId;
        private String validationCode;
        private List<OrderItem> items;

        public OrderBuilder user(User user) { this.user = user; return this; }
        public OrderBuilder orderDate(LocalDateTime orderDate) { this.orderDate = orderDate; return this; }
        public OrderBuilder totalAmount(Double totalAmount) { this.totalAmount = totalAmount; return this; }
        public OrderBuilder status(OrderStatus status) { this.status = status; return this; }
        public OrderBuilder sellsyOrderId(String sellsyOrderId) { this.sellsyOrderId = sellsyOrderId; return this; }
        public OrderBuilder validationCode(String validationCode) { this.validationCode = validationCode; return this; }
        public OrderBuilder items(List<OrderItem> items) { this.items = items; return this; }

        public Order build() {
            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(orderDate);
            order.setTotalAmount(totalAmount);
            order.setStatus(status);
            order.setSellsyOrderId(sellsyOrderId);
            order.setValidationCode(validationCode);
            if (items != null) order.setItems(items);
            return order;
        }
    }
}
