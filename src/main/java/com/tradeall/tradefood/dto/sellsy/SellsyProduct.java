package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyProduct {
    private Long id;
    private String type;
    private String name;
    private String reference;
    private String reference_price;
    private String reference_price_taxes_exc;
    private String purchase_amount;
    private String reference_price_taxes_inc;
    private Boolean is_reference_price_taxes_free;
    private Long tax_id;
    private Long unit_id;
    private Long category_id;
    private String price_excl_tax;
    private String currency;
    private String standard_quantity;
    private String description;
    private Boolean is_name_included_in_description;
    private Long accounting_code_id;
    private Long accounting_purchase_code_id;
    private Boolean is_archived;
    private Boolean is_declined;
    private Boolean is_einvoicing_compliant;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getReference_price() { return reference_price; }
    public void setReference_price(String reference_price) { this.reference_price = reference_price; }
    public String getReference_price_taxes_exc() { return reference_price_taxes_exc; }
    public void setReference_price_taxes_exc(String reference_price_taxes_exc) { this.reference_price_taxes_exc = reference_price_taxes_exc; }
    public String getPurchase_amount() { return purchase_amount; }
    public void setPurchase_amount(String purchase_amount) { this.purchase_amount = purchase_amount; }
    public String getReference_price_taxes_inc() { return reference_price_taxes_inc; }
    public void setReference_price_taxes_inc(String reference_price_taxes_inc) { this.reference_price_taxes_inc = reference_price_taxes_inc; }
    public Boolean getIs_reference_price_taxes_free() { return is_reference_price_taxes_free; }
    public void setIs_reference_price_taxes_free(Boolean is_reference_price_taxes_free) { this.is_reference_price_taxes_free = is_reference_price_taxes_free; }
    public Long getTax_id() { return tax_id; }
    public void setTax_id(Long tax_id) { this.tax_id = tax_id; }
    public Long getUnit_id() { return unit_id; }
    public void setUnit_id(Long unit_id) { this.unit_id = unit_id; }
    public Long getCategory_id() { return category_id; }
    public void setCategory_id(Long category_id) { this.category_id = category_id; }
    public String getPrice_excl_tax() { return price_excl_tax; }
    public void setPrice_excl_tax(String price_excl_tax) { this.price_excl_tax = price_excl_tax; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getStandard_quantity() { return standard_quantity; }
    public void setStandard_quantity(String standard_quantity) { this.standard_quantity = standard_quantity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIs_name_included_in_description() { return is_name_included_in_description; }
    public void setIs_name_included_in_description(Boolean is_name_included_in_description) { this.is_name_included_in_description = is_name_included_in_description; }
    public Long getAccounting_code_id() { return accounting_code_id; }
    public void setAccounting_code_id(Long accounting_code_id) { this.accounting_code_id = accounting_code_id; }
    public Long getAccounting_purchase_code_id() { return accounting_purchase_code_id; }
    public void setAccounting_purchase_code_id(Long accounting_purchase_code_id) { this.accounting_purchase_code_id = accounting_purchase_code_id; }
    public Boolean getIs_archived() { return is_archived; }
    public void setIs_archived(Boolean is_archived) { this.is_archived = is_archived; }
    public Boolean getIs_declined() { return is_declined; }
    public void setIs_declined(Boolean is_declined) { this.is_declined = is_declined; }
    public Boolean getIs_einvoicing_compliant() { return is_einvoicing_compliant; }
    public void setIs_einvoicing_compliant(Boolean is_einvoicing_compliant) { this.is_einvoicing_compliant = is_einvoicing_compliant; }
}
