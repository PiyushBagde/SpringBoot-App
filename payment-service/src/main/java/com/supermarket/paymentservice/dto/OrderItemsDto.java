package com.supermarket.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsDto {
    private int productId;
    private String productName;
    private int quantity;
    private double price;

}
