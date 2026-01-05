package com.tradeall.tradefood.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.UUID;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    public CartItem() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public static CartItemBuilder builder() {
        return new CartItemBuilder();
    }

    public static class CartItemBuilder {
        private Cart cart;
        private Product product;
        private Integer quantity;

        public CartItemBuilder cart(Cart cart) { this.cart = cart; return this; }
        public CartItemBuilder product(Product product) { this.product = product; return this; }
        public CartItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }

        public CartItem build() {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            return item;
        }
    }
}
