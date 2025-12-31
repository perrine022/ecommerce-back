package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SellsyOrderRequest {
    private String number;
    private String date;
    private String due_date;
    private String created;
    private String subject = "";
    private String currency = "EUR";
    private List<SellsyRelated> related = new ArrayList<>();
    private Boolean public_link_enabled = true;
    private Long owner_id;
    private Long assigned_staff_id;
    private Long invoicing_address_id;
    private Long delivery_address_id;
    private Long issuer_address_id;
    private String note = "";
    private String shipping_date;
    private String vat_mode = "debit";
    private List<SellsyRowRequest> rows = new ArrayList<>();
    private Long rate_category_id;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyRelated {
        private Long id;
        private String type;

        public SellsyRelated() {}
        public SellsyRelated(Long id, String type) { this.id = id; this.type = type; }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyRowRequest {
        private String type = "single";
        private Long unit_id;
        private String purchase_amount;
        private String unit_amount;
        private Long tax_id;
        private String quantity;
        private String reference = "";
        private String description = "";
        private Long accounting_code_id;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Long getUnit_id() { return unit_id; }
        public void setUnit_id(Long unit_id) { this.unit_id = unit_id; }
        public String getPurchase_amount() { return purchase_amount; }
        public void setPurchase_amount(String purchase_amount) { this.purchase_amount = purchase_amount; }
        public String getUnit_amount() { return unit_amount; }
        public void setUnit_amount(String unit_amount) { this.unit_amount = unit_amount; }
        public Long getTax_id() { return tax_id; }
        public void setTax_id(Long tax_id) { this.tax_id = tax_id; }
        public String getQuantity() { return quantity; }
        public void setQuantity(String quantity) { this.quantity = quantity; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Long getAccounting_code_id() { return accounting_code_id; }
        public void setAccounting_code_id(Long accounting_code_id) { this.accounting_code_id = accounting_code_id; }
    }

    // Getters and Setters
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getDue_date() { return due_date; }
    public void setDue_date(String due_date) { this.due_date = due_date; }
    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public List<SellsyRelated> getRelated() { return related; }
    public void setRelated(List<SellsyRelated> related) { this.related = related; }
    public Boolean getPublic_link_enabled() { return public_link_enabled; }
    public void setPublic_link_enabled(Boolean public_link_enabled) { this.public_link_enabled = public_link_enabled; }
    public Long getOwner_id() { return owner_id; }
    public void setOwner_id(Long owner_id) { this.owner_id = owner_id; }
    public Long getAssigned_staff_id() { return assigned_staff_id; }
    public void setAssigned_staff_id(Long assigned_staff_id) { this.assigned_staff_id = assigned_staff_id; }
    public Long getInvoicing_address_id() { return invoicing_address_id; }
    public void setInvoicing_address_id(Long invoicing_address_id) { this.invoicing_address_id = invoicing_address_id; }
    public Long getDelivery_address_id() { return delivery_address_id; }
    public void setDelivery_address_id(Long delivery_address_id) { this.delivery_address_id = delivery_address_id; }
    public Long getIssuer_address_id() { return issuer_address_id; }
    public void setIssuer_address_id(Long issuer_address_id) { this.issuer_address_id = issuer_address_id; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getShipping_date() { return shipping_date; }
    public void setShipping_date(String shipping_date) { this.shipping_date = shipping_date; }
    public String getVat_mode() { return vat_mode; }
    public void setVat_mode(String vat_mode) { this.vat_mode = vat_mode; }
    public List<SellsyRowRequest> getRows() { return rows; }
    public void setRows(List<SellsyRowRequest> rows) { this.rows = rows; }
    public Long getRate_category_id() { return rate_category_id; }
    public void setRate_category_id(Long rate_category_id) { this.rate_category_id = rate_category_id; }
}
