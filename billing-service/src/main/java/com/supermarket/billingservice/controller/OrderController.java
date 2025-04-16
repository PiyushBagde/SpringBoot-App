package com.supermarket.billingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.supermarket.billingservice.model.Order;
import com.supermarket.billingservice.service.OrderService;

@RestController
@RequestMapping("/bill")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/placeOrder/{userId}")
	public Order placeOrder(@PathVariable int userId) {
		return orderService.placeOrder(userId);
	}
	
	@GetMapping("/getOrderByUserId/{userId}")
	public List<Order> getOrdersByUserId(@PathVariable int userId){
		return orderService.getOrdersByUserId(userId);
	}
	
	@GetMapping("/getOrderByOrderId/{orderId}")
	public Order getOrderByOrderId(@PathVariable int orderId) {
		return orderService.getOrderByOrderId(orderId);
	}
	
	@GetMapping("/getAllOrders")
	public List<Order> getAllOrders(){
		return orderService.getAllOrders();
	}
}

