package com.tradeall.tradefood.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.UUID;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "sellsy_row_id")
    private Long sellsyRowId;

    @Column(name = "type")
    private String type;

    @Column(name = "unit_id")
    private Long unitId;

    @Column(name = "purchase_amount")
    private String purchaseAmount;

    @Column(name = "unit_amount")
    private String unitAmount;

    @Column(name = "tax_id")
    private Long taxId;

    @Column(name = "quantity_str")
    private String quantityStr;

    @Column(name = "reference")
    private String reference;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    // Discount
    @Column(name = "discount_type")
    private String discountType;

    @Column(name = "discount_value")
    private String discountValue;

    @Column(name = "discount_total")
    private String discountTotal;
    
    @Column(name = "tax_rate")
    private String taxRate;

    @Column(name = "tax_amount")
    private String taxAmount;

    @Column(name = "tax_label")
    private String taxLabel;

    @Column(name = "amount_tax_inc")
    private String amountTaxInc;

    @Column(name = "amount_tax_exc")
    private String amountTaxExc;

    @Column(name = "accounting_code_id")
    private Long accountingCodeId;

    @Column(name = "analytic_code")
    private String analyticCode;

    @Column(name = "is_optional")
    private Boolean isOptional;

    public OrderItem() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

    public Long getSellsyRowId() { return sellsyRowId; }
    public void setSellsyRowId(Long sellsyRowId) { this.sellsyRowId = sellsyRowId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getUnitId() { return unitId; }
    public void setUnitId(Long unitId) { this.unitId = unitId; }
    public String getPurchaseAmount() { return purchaseAmount; }
    public void setPurchaseAmount(String purchaseAmount) { this.purchaseAmount = purchaseAmount; }
    public String getUnitAmount() { return unitAmount; }
    public void setUnitAmount(String unitAmount) { this.unitAmount = unitAmount; }
    public Long getTaxId() { return taxId; }
    public void setTaxId(Long taxId) { this.taxId = taxId; }
    public String getQuantityStr() { return quantityStr; }
    public void setQuantityStr(String quantityStr) { this.quantityStr = quantityStr; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public String getDiscountValue() { return discountValue; }
    public void setDiscountValue(String discountValue) { this.discountValue = discountValue; }
    public String getDiscountTotal() { return discountTotal; }
    public void setDiscountTotal(String discountTotal) { this.discountTotal = discountTotal; }
    public String getTaxRate() { return taxRate; }
    public void setTaxRate(String taxRate) { this.taxRate = taxRate; }
    public String getTaxAmount() { return taxAmount; }
    public void setTaxAmount(String taxAmount) { this.taxAmount = taxAmount; }
    public String getTaxLabel() { return taxLabel; }
    public void setTaxLabel(String taxLabel) { this.taxLabel = taxLabel; }
    public String getAmountTaxInc() { return amountTaxInc; }
    public void setAmountTaxInc(String amountTaxInc) { this.amountTaxInc = amountTaxInc; }
    public String getAmountTaxExc() { return amountTaxExc; }
    public void setAmountTaxExc(String amountTaxExc) { this.amountTaxExc = amountTaxExc; }
    public Long getAccountingCodeId() { return accountingCodeId; }
    public void setAccountingCodeId(Long accountingCodeId) { this.accountingCodeId = accountingCodeId; }
    public String getAnalyticCode() { return analyticCode; }
    public void setAnalyticCode(String analyticCode) { this.analyticCode = analyticCode; }
    public Boolean getIsOptional() { return isOptional; }
    public void setIsOptional(Boolean isOptional) { this.isOptional = isOptional; }

    public static OrderItemBuilder builder() {
        return new OrderItemBuilder();
    }

    public static class OrderItemBuilder {
        private Order order;
        private Product product;
        private Integer quantity;
        private Double unitPrice;

        public OrderItemBuilder order(Order order) { this.order = order; return this; }
        public OrderItemBuilder product(Product product) { this.product = product; return this; }
        public OrderItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public OrderItemBuilder unitPrice(Double unitPrice) { this.unitPrice = unitPrice; return this; }

        public OrderItem build() {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setUnitPrice(unitPrice);
            return item;
        }
    }
}
