package com.tradeall.tradefood.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Transient
    private Double totalHT;
    @Transient
    private Double totalTVA;
    @Transient
    private Double totalTTC;

    public Cart() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public Double getTotalHT() { return totalHT; }
    public void setTotalHT(Double totalHT) { this.totalHT = totalHT; }
    public Double getTotalTVA() { return totalTVA; }
    public void setTotalTVA(Double totalTVA) { this.totalTVA = totalTVA; }
    public Double getTotalTTC() { return totalTTC; }
    public void setTotalTTC(Double totalTTC) { this.totalTTC = totalTTC; }

    public static CartBuilder builder() {
        return new CartBuilder();
    }

    public static class CartBuilder {
        private User user;
        public CartBuilder user(User user) { this.user = user; return this; }
        public Cart build() {
            Cart cart = new Cart();
            cart.setUser(user);
            return cart;
        }
    }
}
