package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyInvoice {
    private Long id;
    private String number;
    private String status;
    private String date;
    private String due_date;
    private String created;
    private String subject;
    private SellsyInvoiceAmounts amounts;
    private String currency;
    private String pdf_link;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyInvoiceAmounts {
        private String total_raw_excl_tax;
        private String total_after_discount_excl_tax;
        private String total_excl_tax;
        private String total_incl_tax;
        private String total_remaining_due_incl_tax;
    }
}
