package com.tradeall.tradefood.dto.sellsy;

import lombok.Data;

@Data
public class SellsyLinkPaymentRequest {
    private Double amount;

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
