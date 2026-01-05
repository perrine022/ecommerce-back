package com.tradeall.tradefood.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "sellsy_id")
    private Long sellsyId;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "reference")
    private String reference;

    @Column(name = "reference_price")
    private String referencePrice;

    @Column(name = "reference_price_taxes_exc")
    private String referencePriceTaxesExc;

    @Column(name = "purchase_amount")
    private String purchaseAmount;

    @Column(name = "reference_price_taxes_inc")
    private String referencePriceTaxesInc;

    @Column(name = "is_reference_price_taxes_free")
    private Boolean isReferencePriceTaxesFree;

    @Column(name = "tax_id")
    private Long taxId;

    @Column(name = "unit_id")
    private Long unitId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "price_excl_tax")
    private String priceExclTax;

    @Column(name = "currency")
    private String currency;

    @Column(name = "standard_quantity")
    private String standardQuantity;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "is_name_included_in_description")
    private Boolean isNameIncludedInDescription;

    @Column(name = "accounting_code_id")
    private Long accountingCodeId;

    @Column(name = "accounting_purchase_code_id")
    private Long accountingPurchaseCodeId;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @Column(name = "is_declined")
    private Boolean isDeclined;

    @Column(name = "is_einvoicing_compliant")
    private Boolean isEinvoicingCompliant;
    
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    public Product() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Long getSellsyId() { return sellsyId; }
    public void setSellsyId(Long sellsyId) { this.sellsyId = sellsyId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getReferencePrice() { return referencePrice; }
    public void setReferencePrice(String referencePrice) { this.referencePrice = referencePrice; }
    public String getReferencePriceTaxesExc() { return referencePriceTaxesExc; }
    public void setReferencePriceTaxesExc(String referencePriceTaxesExc) { this.referencePriceTaxesExc = referencePriceTaxesExc; }
    public String getPurchaseAmount() { return purchaseAmount; }
    public void setPurchaseAmount(String purchaseAmount) { this.purchaseAmount = purchaseAmount; }
    public String getReferencePriceTaxesInc() { return referencePriceTaxesInc; }
    public void setReferencePriceTaxesInc(String referencePriceTaxesInc) { this.referencePriceTaxesInc = referencePriceTaxesInc; }
    public Boolean getIsReferencePriceTaxesFree() { return isReferencePriceTaxesFree; }
    public void setIsReferencePriceTaxesFree(Boolean isReferencePriceTaxesFree) { this.isReferencePriceTaxesFree = isReferencePriceTaxesFree; }
    public Long getTaxId() { return taxId; }
    public void setTaxId(Long taxId) { this.taxId = taxId; }
    public Long getUnitId() { return unitId; }
    public void setUnitId(Long unitId) { this.unitId = unitId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getPriceExclTax() { return priceExclTax; }
    public void setPriceExclTax(String priceExclTax) { this.priceExclTax = priceExclTax; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getStandardQuantity() { return standardQuantity; }
    public void setStandardQuantity(String standardQuantity) { this.standardQuantity = standardQuantity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIsNameIncludedInDescription() { return isNameIncludedInDescription; }
    public void setIsNameIncludedInDescription(Boolean isNameIncludedInDescription) { this.isNameIncludedInDescription = isNameIncludedInDescription; }
    public Long getAccountingCodeId() { return accountingCodeId; }
    public void setAccountingCodeId(Long accountingCodeId) { this.accountingCodeId = accountingCodeId; }
    public Long getAccountingPurchaseCodeId() { return accountingPurchaseCodeId; }
    public void setAccountingPurchaseCodeId(Long accountingPurchaseCodeId) { this.accountingPurchaseCodeId = accountingPurchaseCodeId; }
    public Boolean getIsArchived() { return isArchived; }
    public void setIsArchived(Boolean isArchived) { this.isArchived = isArchived; }
    public Boolean getIsDeclined() { return isDeclined; }
    public void setIsDeclined(Boolean isDeclined) { this.isDeclined = isDeclined; }
    public Boolean getIsEinvoicingCompliant() { return isEinvoicingCompliant; }
    public void setIsEinvoicingCompliant(Boolean isEinvoicingCompliant) { this.isEinvoicingCompliant = isEinvoicingCompliant; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private UUID id;
        private Long sellsyId;
        private String name;
        private String reference;
        private String description;
        private String referencePrice;
        private String purchaseAmount;

        public ProductBuilder id(UUID id) { this.id = id; return this; }
        public ProductBuilder sellsyId(Long sellsyId) { this.sellsyId = sellsyId; return this; }
        public ProductBuilder name(String name) { this.name = name; return this; }
        public ProductBuilder reference(String reference) { this.reference = reference; return this; }
        public ProductBuilder description(String description) { this.description = description; return this; }
        public ProductBuilder referencePrice(String referencePrice) { this.referencePrice = referencePrice; return this; }
        public ProductBuilder purchaseAmount(String purchaseAmount) { this.purchaseAmount = purchaseAmount; return this; }

        public Product build() {
            Product product = new Product();
            product.setId(id);
            product.setSellsyId(sellsyId);
            product.setName(name);
            product.setReference(reference);
            product.setDescription(description);
            product.setReferencePrice(referencePrice);
            product.setPurchaseAmount(purchaseAmount);
            return product;
        }
    }
}
