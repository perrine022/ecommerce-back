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
    private SellsySettings settings;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsySettings {
        private SellsyPayments payments;
        private SellsyPdfDisplay pdf_display;

        public SellsyPayments getPayments() { return payments; }
        public void setPayments(SellsyPayments payments) { this.payments = payments; }
        public SellsyPdfDisplay getPdf_display() { return pdf_display; }
        public void setPdf_display(SellsyPdfDisplay pdf_display) { this.pdf_display = pdf_display; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyPayments {
        private List<String> payment_modules = new ArrayList<>();
        private String direct_debit_module;

        public List<String> getPayment_modules() { return payment_modules; }
        public void setPayment_modules(List<String> payment_modules) { this.payment_modules = payment_modules; }
        public String getDirect_debit_module() { return direct_debit_module; }
        public void setDirect_debit_module(String direct_debit_module) { this.direct_debit_module = direct_debit_module; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyPdfDisplay {
        private Boolean show_vat_mention = true;
        private Boolean show_bank_account = true;
        private Boolean show_check_label = true;
        private Boolean show_contact_name = true;
        private Boolean show_siret = true;
        private Boolean show_siren = true;
        private Boolean show_vat_number = true;
        private Boolean show_company_reference = true;
        private Boolean show_payment_methods = true;
        private Boolean show_payment_terms = true;
        private Boolean show_payment_deadlines = true;
        private Boolean show_company_phone_number = true;
        private Boolean show_delivery_address = true;
        private Boolean show_barcode_values = true;
        private Boolean show_barcode_images = true;
        private Boolean show_reference_column = true;
        private Boolean show_quantity_column = true;
        private Boolean show_image_column = true;
        private Boolean show_unit_cost_column = true;
        private Boolean show_amount_column = true;
        private Boolean show_taxes_column = true;
        private Boolean show_discount_column = true;

        // Getters and Setters omitted for brevity in search_replace if not needed, but better include them
        public Boolean getShow_vat_mention() { return show_vat_mention; }
        public void setShow_vat_mention(Boolean show_vat_mention) { this.show_vat_mention = show_vat_mention; }
        public Boolean getShow_bank_account() { return show_bank_account; }
        public void setShow_bank_account(Boolean show_bank_account) { this.show_bank_account = show_bank_account; }
        public Boolean getShow_check_label() { return show_check_label; }
        public void setShow_check_label(Boolean show_check_label) { this.show_check_label = show_check_label; }
        public Boolean getShow_contact_name() { return show_contact_name; }
        public void setShow_contact_name(Boolean show_contact_name) { this.show_contact_name = show_contact_name; }
        public Boolean getShow_siret() { return show_siret; }
        public void setShow_siret(Boolean show_siret) { this.show_siret = show_siret; }
        public Boolean getShow_siren() { return show_siren; }
        public void setShow_siren(Boolean show_siren) { this.show_siren = show_siren; }
        public Boolean getShow_vat_number() { return show_vat_number; }
        public void setShow_vat_number(Boolean show_vat_number) { this.show_vat_number = show_vat_number; }
        public Boolean getShow_company_reference() { return show_company_reference; }
        public void setShow_company_reference(Boolean show_company_reference) { this.show_company_reference = show_company_reference; }
        public Boolean getShow_payment_methods() { return show_payment_methods; }
        public void setShow_payment_methods(Boolean show_payment_methods) { this.show_payment_methods = show_payment_methods; }
        public Boolean getShow_payment_terms() { return show_payment_terms; }
        public void setShow_payment_terms(Boolean show_payment_terms) { this.show_payment_terms = show_payment_terms; }
        public Boolean getShow_payment_deadlines() { return show_payment_deadlines; }
        public void setShow_payment_deadlines(Boolean show_payment_deadlines) { this.show_payment_deadlines = show_payment_deadlines; }
        public Boolean getShow_company_phone_number() { return show_company_phone_number; }
        public void setShow_company_phone_number(Boolean show_company_phone_number) { this.show_company_phone_number = show_company_phone_number; }
        public Boolean getShow_delivery_address() { return show_delivery_address; }
        public void setShow_delivery_address(Boolean show_delivery_address) { this.show_delivery_address = show_delivery_address; }
        public Boolean getShow_barcode_values() { return show_barcode_values; }
        public void setShow_barcode_values(Boolean show_barcode_values) { this.show_barcode_values = show_barcode_values; }
        public Boolean getShow_barcode_images() { return show_barcode_images; }
        public void setShow_barcode_images(Boolean show_barcode_images) { this.show_barcode_images = show_barcode_images; }
        public Boolean getShow_reference_column() { return show_reference_column; }
        public void setShow_reference_column(Boolean show_reference_column) { this.show_reference_column = show_reference_column; }
        public Boolean getShow_quantity_column() { return show_quantity_column; }
        public void setShow_quantity_column(Boolean show_quantity_column) { this.show_quantity_column = show_quantity_column; }
        public Boolean getShow_image_column() { return show_image_column; }
        public void setShow_image_column(Boolean show_image_column) { this.show_image_column = show_image_column; }
        public Boolean getShow_unit_cost_column() { return show_unit_cost_column; }
        public void setShow_unit_cost_column(Boolean show_unit_cost_column) { this.show_unit_cost_column = show_unit_cost_column; }
        public Boolean getShow_amount_column() { return show_amount_column; }
        public void setShow_amount_column(Boolean show_amount_column) { this.show_amount_column = show_amount_column; }
        public Boolean getShow_taxes_column() { return show_taxes_column; }
        public void setShow_taxes_column(Boolean show_taxes_column) { this.show_taxes_column = show_taxes_column; }
        public Boolean getShow_discount_column() { return show_discount_column; }
        public void setShow_discount_column(Boolean show_discount_column) { this.show_discount_column = show_discount_column; }
    }

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
    public SellsySettings getSettings() { return settings; }
    public void setSettings(SellsySettings settings) { this.settings = settings; }
}
