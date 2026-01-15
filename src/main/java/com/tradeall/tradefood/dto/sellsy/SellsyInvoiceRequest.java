package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyInvoiceRequest {
    private String number;
    private String date;
    private String due_date;
    private String created;
    private String subject;
    private String currency;
    private List<SellsyRelated> related;
    private Boolean public_link_enabled;
    private Long assigned_staff_id;
    private Long owner_id;
    private String order_reference;
    private Long invoicing_address_id;
    private Long delivery_address_id;
    private Long issuer_address_id;
    private String note;
    private String shipping_date;
    private String shipping_volume;
    private SellsyShippingWeight shipping_weight;
    private String vat_mode;
    private String vat_mention;
    private Long bank_account_id;
    private List<Long> payment_method_ids;
    private List<SellsyRowRequest> rows;
    private SellsySettings settings;

    @Data
    public static class SellsyRelated {
        private Long id;
        private String type;

        public SellsyRelated() {}
        public SellsyRelated(Long id, String type) {
            this.id = id;
            this.type = type;
        }
    }

    @Data
    public static class SellsyRowRequest {
        private String type;
        private Long unit_id;
        private String purchase_amount;
        private String unit_amount;
        private Long tax_id;
        private String quantity;
        private String reference;
        private String description;
        private Long accounting_code_id;
    }

    @Data
    public static class SellsyShippingWeight {
        private String value;
        private String unit;

        public SellsyShippingWeight() {}
        public SellsyShippingWeight(String value, String unit) {
            this.value = value;
            this.unit = unit;
        }
    }

    @Data
    public static class SellsySettings {
        private SellsyPayments payments;
        private SellsyPdfDisplay pdf_display;
    }

    @Data
    public static class SellsyPayments {
        private List<String> payment_modules;
        private String direct_debit_module;
    }

    @Data
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
    }
}
