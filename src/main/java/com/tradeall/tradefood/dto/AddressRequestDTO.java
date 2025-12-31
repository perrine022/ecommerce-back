package com.tradeall.tradefood.dto;

public class AddressRequestDTO {
    private Long invoicingAddressId;
    private Long deliveryAddressId;

    public AddressRequestDTO() {}

    public AddressRequestDTO(Long invoicingAddressId, Long deliveryAddressId) {
        this.invoicingAddressId = invoicingAddressId;
        this.deliveryAddressId = deliveryAddressId;
    }

    public Long getInvoicingAddressId() { return invoicingAddressId; }
    public void setInvoicingAddressId(Long invoicingAddressId) { this.invoicingAddressId = invoicingAddressId; }
    public Long getDeliveryAddressId() { return deliveryAddressId; }
    public void setDeliveryAddressId(Long deliveryAddressId) { this.deliveryAddressId = deliveryAddressId; }
}
