package com.supermarket.billingservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItems {

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;

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

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

}
