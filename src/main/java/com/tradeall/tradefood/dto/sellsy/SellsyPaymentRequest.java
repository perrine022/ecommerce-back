package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyPaymentRequest {
    private String number;
    private String paid_at;
    private Integer payment_method_id;
    private String type; // "credit" or "debit"
    private SellsyAmount amount;
    private String note;

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getPaid_at() { return paid_at; }
    public void setPaid_at(String paid_at) { this.paid_at = paid_at; }
    public Integer getPayment_method_id() { return payment_method_id; }
    public void setPayment_method_id(Integer payment_method_id) { this.payment_method_id = payment_method_id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public SellsyAmount getAmount() { return amount; }
    public void setAmount(SellsyAmount amount) { this.amount = amount; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyAmount {
        private String value;
        private String currency;

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
    }
}
