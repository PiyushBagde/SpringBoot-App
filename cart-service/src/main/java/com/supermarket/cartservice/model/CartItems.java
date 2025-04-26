package com.supermarket.cartservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")

public class CartItems {

    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore
    private Cart cart;

    @Column(name = "product_id")
    private int prodId;

    @Column(name = "product_name")
    private String prodName;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_price")
    private double totalPrice;


    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
