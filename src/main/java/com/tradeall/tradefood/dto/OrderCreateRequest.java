package com.tradeall.tradefood.dto;

import java.util.List;
import java.util.UUID;

public class OrderCreateRequest {
    private UUID clientId; // Optionnel, utilisé par le commercial
    private Long invoicingAddressId;
    private Long deliveryAddressId;
    private List<ProductItemRequest> items;

    public static class ProductItemRequest {
        private UUID productId;
        private Integer quantity;

        public ProductItemRequest() {}
        public UUID getProductId() { return productId; }
        public void setProductId(UUID productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public OrderCreateRequest() {}

    public UUID getClientId() { return clientId; }
    public void setClientId(UUID clientId) { this.clientId = clientId; }

    public Long getInvoicingAddressId() { return invoicingAddressId; }
    public void setInvoicingAddressId(Long invoicingAddressId) { this.invoicingAddressId = invoicingAddressId; }
    public Long getDeliveryAddressId() { return deliveryAddressId; }
    public void setDeliveryAddressId(Long deliveryAddressId) { this.deliveryAddressId = deliveryAddressId; }
    public List<ProductItemRequest> getItems() { return items; }
    public void setItems(List<ProductItemRequest> items) { this.items = items; }
}
