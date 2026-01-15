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
        private Boolean show_vat_mention;
        private Boolean show_bank_account;
        private Boolean show_check_label;
        private Boolean show_contact_name;
        private Boolean show_siret;
        private Boolean show_siren;
        private Boolean show_vat_number;
        private Boolean show_company_reference;
        private Boolean show_payment_methods;
        private Boolean show_payment_terms;
        private Boolean show_payment_deadlines;
        private Boolean show_company_phone_number;
        private Boolean show_delivery_address;
        private Boolean show_barcode_values;
        private Boolean show_barcode_images;
        private Boolean show_reference_column;
        private Boolean show_quantity_column;
        private Boolean show_image_column;
        private Boolean show_unit_cost_column;
        private Boolean show_amount_column;
        private Boolean show_taxes_column;
        private Boolean show_discount_column;
    }
}
