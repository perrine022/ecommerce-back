package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyPaymentResponse {
    private Long id;
    private String number;
    private String paid_at;
    private String status;
    private Integer payment_method_id;
    private String type;
    private SellsyAmounts amounts;
    private String currency;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getPaid_at() { return paid_at; }
    public void setPaid_at(String paid_at) { this.paid_at = paid_at; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getPayment_method_id() { return payment_method_id; }
    public void setPayment_method_id(Integer payment_method_id) { this.payment_method_id = payment_method_id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public SellsyAmounts getAmounts() { return amounts; }
    public void setAmounts(SellsyAmounts amounts) { this.amounts = amounts; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyAmounts {
        private String total;
        private String remaining;

        public String getTotal() { return total; }
        public void setTotal(String total) { this.total = total; }
        public String getRemaining() { return remaining; }
        public void setRemaining(String remaining) { this.remaining = remaining; }
    }
}
