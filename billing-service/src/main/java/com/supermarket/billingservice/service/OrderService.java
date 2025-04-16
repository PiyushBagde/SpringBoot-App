package com.supermarket.billingservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermarket.billingservice.dto.CartItemResponse;
import com.supermarket.billingservice.feign.CartServiceClient;
import com.supermarket.billingservice.model.Order;
import com.supermarket.billingservice.model.OrderItems;
import com.supermarket.billingservice.repository.OrderRepository;
import com.supermarket.billingservice.repository.OrderitemsRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderitemsRepository orderitemsRepository;
	
	@Autowired
	private CartServiceClient cartServiceClient;
	
	
	
	public Order placeOrder(int userId) {
		List<CartItemResponse> cartItems = cartServiceClient.getCartItemsByUserId(userId);
		if(cartItems == null || cartItems.isEmpty()) {
			throw new RuntimeException("Cart is empty! Cannot place order.");
		}
		
		double totalAmount = 0.0;
		
		List<OrderItems> orderItemList = new ArrayList<>();
		
		for (CartItemResponse cartItem : cartItems) {
			OrderItems item = new OrderItems();
			item.setProdId(cartItem.getProdId());
			item.setProdName(cartItem.getProdName());
			item.setQuantity(cartItem.getQuantity());
			item.setPrice(cartItem.getPrice());
			item.setTotalPrice(cartItem.getQuantity() * cartItem.getPrice());
			totalAmount += item.getTotalPrice();
			
			orderItemList.add(item);
		}
		
		Order order = new Order();
		order.setUserId(userId);
		order.setOrderDate(LocalDateTime.now());
		order.setCartId(cartServiceClient.getCartIdByUserId(userId));
		order.setTotalBillPrice(totalAmount);
		
		Order savedOrder = orderRepository.save(order);
		
		for(OrderItems item : orderItemList) {
			item.setOrder(savedOrder);
		}
		
		orderitemsRepository.saveAll(orderItemList);
		savedOrder.setOrderItems(orderItemList);
	    return order;
	}
	
	public List<Order> getOrdersByUserId(int userId) {
        return orderRepository.findAllByUserId(userId);
    }
	
	public Order getOrderByOrderId(int orderId){
		return orderRepository.findById(orderId).orElseThrow(()-> new RuntimeException("Order Id not found."));
	}
	
//	public void cancelOrder(int orderId) {
//		
//	}
	
	public List<Order> getAllOrders(){
		return orderRepository.findAll();
	}
}
