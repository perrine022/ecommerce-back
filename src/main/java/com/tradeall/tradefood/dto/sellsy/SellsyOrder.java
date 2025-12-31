package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyOrder {
    private Long id;
    private String number;
    private String status;
    private String order_status;
    private String date;
    private String due_date;
    private String created;
    private String subject;
    private SellsyAmounts amounts;
    private String currency;
    private List<SellsyTax> taxes;
    private SellsyDiscount discount;
    private List<SellsyRelated> related;
    private SellsyPublicLink public_link;
    private SellsyPaymentConditions payment_conditions_acceptance;
    private SellsyOwner owner;
    private Long fiscal_year_id;
    private String pdf_link;
    private SellsyDecimalNumber decimal_number;
    private Long assigned_staff_id;
    private Long invoicing_address_id;
    private Long delivery_address_id;
    private Long issuer_address_id;
    private Long contact_id;
    private Long rate_category_id;
    private SellsyServiceDates service_dates;
    private String note;
    private SellsyOrderEmbed _embed;
    private String shipping_date;
    private String company_reference;
    private String company_name;
    private Long bank_account_id;
    private Long eco_tax_id;
    private Long check_label_id;
    private String vat_mode;
    private String vat_mention;
    private SellsyWeight shipping_weight;
    private String shipping_volume;
    private List<SellsyRow> rows;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyAmounts {
        private String total_raw_excl_tax;
        private String total_after_discount_excl_tax;
        private String total_packaging;
        private String total_shipping;
        private String total_excl_tax;
        private String total_incl_tax;

        // Getters and Setters
        public String getTotal_raw_excl_tax() { return total_raw_excl_tax; }
        public void setTotal_raw_excl_tax(String total_raw_excl_tax) { this.total_raw_excl_tax = total_raw_excl_tax; }
        public String getTotal_after_discount_excl_tax() { return total_after_discount_excl_tax; }
        public void setTotal_after_discount_excl_tax(String total_after_discount_excl_tax) { this.total_after_discount_excl_tax = total_after_discount_excl_tax; }
        public String getTotal_packaging() { return total_packaging; }
        public void setTotal_packaging(String total_packaging) { this.total_packaging = total_packaging; }
        public String getTotal_shipping() { return total_shipping; }
        public void setTotal_shipping(String total_shipping) { this.total_shipping = total_shipping; }
        public String getTotal_excl_tax() { return total_excl_tax; }
        public void setTotal_excl_tax(String total_excl_tax) { this.total_excl_tax = total_excl_tax; }
        public String getTotal_incl_tax() { return total_incl_tax; }
        public void setTotal_incl_tax(String total_incl_tax) { this.total_incl_tax = total_incl_tax; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyTax {
        private String label;
        private Long id;
        private String rate;
        private String amount;

        // Getters and Setters
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getRate() { return rate; }
        public void setRate(String rate) { this.rate = rate; }
        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyDiscount {
        private String percent;
        private String amount;
        private String type;

        // Getters and Setters
        public String getPercent() { return percent; }
        public void setPercent(String percent) { this.percent = percent; }
        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyRelated {
        private Long id;
        private String type;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyPublicLink {
        private Boolean enabled;
        private String url;

        // Getters and Setters
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyPaymentConditions {
        private Boolean enabled;

        // Getters and Setters
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyOwner {
        private Long id;
        private String type;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyDecimalNumber {
        @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = SafeIntegerDeserializer.class)
        private Integer unit_price;
        @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = SafeIntegerDeserializer.class)
        private Integer quantity;
        @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = SafeIntegerDeserializer.class)
        private Integer main;

        // Getters and Setters
        public Integer getUnit_price() { return unit_price; }
        public void setUnit_price(Integer unit_price) { this.unit_price = unit_price; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public Integer getMain() { return main; }
        public void setMain(Integer main) { this.main = main; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyServiceDates {
        private String start;
        private String end;

        // Getters and Setters
        public String getStart() { return start; }
        public void setStart(String start) { this.start = start; }
        public String getEnd() { return end; }
        public void setEnd(String end) { this.end = end; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyWeight {
        private String unit;
        private String value;

        // Getters and Setters
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyRow {
        private Long id;
        private String type;
        private Long unit_id;
        private String purchase_amount;
        private String unit_amount;
        private Long tax_id;
        private String quantity;
        private String reference;
        private String description;
        private SellsyDiscount discount;
        private String tax_rate;
        private String tax_amount;
        private String tax_label;
        private String amount_tax_inc;
        private String amount_tax_exc;
        private Long accounting_code_id;
        private String analytic_code;
        private Boolean is_optional;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
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
        public SellsyDiscount getDiscount() { return discount; }
        public void setDiscount(SellsyDiscount discount) { this.discount = discount; }
        public String getTax_rate() { return tax_rate; }
        public void setTax_rate(String tax_rate) { this.tax_rate = tax_rate; }
        public String getTax_amount() { return tax_amount; }
        public void setTax_amount(String tax_amount) { this.tax_amount = tax_amount; }
        public String getTax_label() { return tax_label; }
        public void setTax_label(String tax_label) { this.tax_label = tax_label; }
        public String getAmount_tax_inc() { return amount_tax_inc; }
        public void setAmount_tax_inc(String amount_tax_inc) { this.amount_tax_inc = amount_tax_inc; }
        public String getAmount_tax_exc() { return amount_tax_exc; }
        public void setAmount_tax_exc(String amount_tax_exc) { this.amount_tax_exc = amount_tax_exc; }
        public Long getAccounting_code_id() { return accounting_code_id; }
        public void setAccounting_code_id(Long accounting_code_id) { this.accounting_code_id = accounting_code_id; }
        public String getAnalytic_code() { return analytic_code; }
        public void setAnalytic_code(String analytic_code) { this.analytic_code = analytic_code; }
        public Boolean getIs_optional() { return is_optional; }
        public void setIs_optional(Boolean is_optional) { this.is_optional = is_optional; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyOrderEmbed {
        // Embed content can be huge, let's include at least some mentioned in the JSON
        private SellsyContact contact;
        private List<SellsyRelated> related;

        // Getters and Setters
        public SellsyContact getContact() { return contact; }
        public void setContact(SellsyContact contact) { this.contact = contact; }
        public List<SellsyRelated> getRelated() { return related; }
        public void setRelated(List<SellsyRelated> related) { this.related = related; }
    }

    // Getters and Setters for SellsyOrder
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOrder_status() { return order_status; }
    public void setOrder_status(String order_status) { this.order_status = order_status; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getDue_date() { return due_date; }
    public void setDue_date(String due_date) { this.due_date = due_date; }
    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public SellsyAmounts getAmounts() { return amounts; }
    public void setAmounts(SellsyAmounts amounts) { this.amounts = amounts; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public List<SellsyTax> getTaxes() { return taxes; }
    public void setTaxes(List<SellsyTax> taxes) { this.taxes = taxes; }
    public SellsyDiscount getDiscount() { return discount; }
    public void setDiscount(SellsyDiscount discount) { this.discount = discount; }
    public List<SellsyRelated> getRelated() { return related; }
    public void setRelated(List<SellsyRelated> related) { this.related = related; }
    public SellsyPublicLink getPublic_link() { return public_link; }
    public void setPublic_link(SellsyPublicLink public_link) { this.public_link = public_link; }
    public SellsyPaymentConditions getPayment_conditions_acceptance() { return payment_conditions_acceptance; }
    public void setPayment_conditions_acceptance(SellsyPaymentConditions payment_conditions_acceptance) { this.payment_conditions_acceptance = payment_conditions_acceptance; }
    public SellsyOwner getOwner() { return owner; }
    public void setOwner(SellsyOwner owner) { this.owner = owner; }
    public Long getFiscal_year_id() { return fiscal_year_id; }
    public void setFiscal_year_id(Long fiscal_year_id) { this.fiscal_year_id = fiscal_year_id; }
    public String getPdf_link() { return pdf_link; }
    public void setPdf_link(String pdf_link) { this.pdf_link = pdf_link; }
    public SellsyDecimalNumber getDecimal_number() { return decimal_number; }
    public void setDecimal_number(SellsyDecimalNumber decimal_number) { this.decimal_number = decimal_number; }
    public Long getAssigned_staff_id() { return assigned_staff_id; }
    public void setAssigned_staff_id(Long assigned_staff_id) { this.assigned_staff_id = assigned_staff_id; }
    public Long getInvoicing_address_id() { return invoicing_address_id; }
    public void setInvoicing_address_id(Long invoicing_address_id) { this.invoicing_address_id = invoicing_address_id; }
    public Long getDelivery_address_id() { return delivery_address_id; }
    public void setDelivery_address_id(Long delivery_address_id) { this.delivery_address_id = delivery_address_id; }
    public Long getIssuer_address_id() { return issuer_address_id; }
    public void setIssuer_address_id(Long issuer_address_id) { this.issuer_address_id = issuer_address_id; }
    public Long getContact_id() { return contact_id; }
    public void setContact_id(Long contact_id) { this.contact_id = contact_id; }
    public Long getRate_category_id() { return rate_category_id; }
    public void setRate_category_id(Long rate_category_id) { this.rate_category_id = rate_category_id; }
    public SellsyServiceDates getService_dates() { return service_dates; }
    public void setService_dates(SellsyServiceDates service_dates) { this.service_dates = service_dates; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public SellsyOrderEmbed get_embed() { return _embed; }
    public void set_embed(SellsyOrderEmbed _embed) { this._embed = _embed; }
    public String getShipping_date() { return shipping_date; }
    public void setShipping_date(String shipping_date) { this.shipping_date = shipping_date; }
    public String getCompany_reference() { return company_reference; }
    public void setCompany_reference(String company_reference) { this.company_reference = company_reference; }
    public String getCompany_name() { return company_name; }
    public void setCompany_name(String company_name) { this.company_name = company_name; }
    public Long getBank_account_id() { return bank_account_id; }
    public void setBank_account_id(Long bank_account_id) { this.bank_account_id = bank_account_id; }
    public Long getEco_tax_id() { return eco_tax_id; }
    public void setEco_tax_id(Long eco_tax_id) { this.eco_tax_id = eco_tax_id; }
    public Long getCheck_label_id() { return check_label_id; }
    public void setCheck_label_id(Long check_label_id) { this.check_label_id = check_label_id; }
    public String getVat_mode() { return vat_mode; }
    public void setVat_mode(String vat_mode) { this.vat_mode = vat_mode; }
    public String getVat_mention() { return vat_mention; }
    public void setVat_mention(String vat_mention) { this.vat_mention = vat_mention; }
    public SellsyWeight getShipping_weight() { return shipping_weight; }
    public void setShipping_weight(SellsyWeight shipping_weight) { this.shipping_weight = shipping_weight; }
    public String getShipping_volume() { return shipping_volume; }
    public void setShipping_volume(String shipping_volume) { this.shipping_volume = shipping_volume; }
    public List<SellsyRow> getRows() { return rows; }
    public void setRows(List<SellsyRow> rows) { this.rows = rows; }
}
