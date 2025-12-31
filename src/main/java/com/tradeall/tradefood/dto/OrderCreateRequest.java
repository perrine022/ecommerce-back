package com.tradeall.tradefood.dto;

import java.util.UUID;

public class OrderCreateRequest {
    private Long invoicingAddressId;
    private Long deliveryAddressId;

    public OrderCreateRequest() {}

    public Long getInvoicingAddressId() { return invoicingAddressId; }
    public void setInvoicingAddressId(Long invoicingAddressId) { this.invoicingAddressId = invoicingAddressId; }
    public Long getDeliveryAddressId() { return deliveryAddressId; }
    public void setDeliveryAddressId(Long deliveryAddressId) { this.deliveryAddressId = deliveryAddressId; }
}
