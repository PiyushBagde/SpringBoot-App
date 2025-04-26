package com.supermarket.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private int orderId;
    private int userId;
    private int cartId;
    private double totalBillPrice;
    private LocalDateTime orderDate;
    private List<OrderItemsDto> items;
}
