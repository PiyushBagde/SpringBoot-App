package com.supermarket.paymentservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto {
	private int orderId;
    private int userId;
    private int cartId;
    private double totalBillPrice;
    private LocalDateTime orderDate;
    private List<OrderItemsDto> items;

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCartId() {
        return cartId;
    }
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }


    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItemsDto> getItems() {
        return items;
    }
    public void setItems(List<OrderItemsDto> items) {
        this.items = items;
    }
	public double getTotalBillPrice() {
		return totalBillPrice;
	}
	public void setTotalBillPrice(double totalBillPrice) {
		this.totalBillPrice = totalBillPrice;
	}
}
