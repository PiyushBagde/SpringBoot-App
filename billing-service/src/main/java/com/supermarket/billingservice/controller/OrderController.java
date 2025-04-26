package com.supermarket.billingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.supermarket.billingservice.model.Order;
import com.supermarket.billingservice.service.OrderService;

@RestController
@RequestMapping("/bill")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping("/biller/placeOrder/{userId}")
	public Order placeOrder(@PathVariable int userId) {
		return orderService.placeOrder(userId);
	}

	@PostMapping("/customer/placeMyOrder")
	public Order placeMyOrder(@RequestHeader("X-UserId") int userId) {
		return orderService.placeOrder(userId);
	}

	@GetMapping("/customer/getMyOrders")
	public List<Order> getMyOrders(@RequestHeader("X-UserId") int userId) {
		return orderService.getOrdersByUserId(userId);
	}

	@DeleteMapping("/customer/cancelMyOrder/{orderId}")
	public String cancelMyOrder(@RequestHeader("X-UserId") int userId ,@PathVariable int orderId) {
		orderService.deleteOrder(userId, orderId);
		return "Order Cancelled Successfully";
	}

	@DeleteMapping("/biller/cancelOrder/{userId}/{orderId}")
	public String cancelOrder(@PathVariable int userId ,@PathVariable int orderId) {
		orderService.deleteOrder(userId, orderId);
		return "Order Cancelled Successfully for user id: "+userId;
	}

	@GetMapping("/admin/getOrderByUserId/{userId}")
	public List<Order> getOrdersByUserId(@PathVariable int userId){
		return orderService.getOrdersByUserId(userId);
	}

	// complete get my getmyorder
	// CUSTOMER ROUTE
	@GetMapping("/admin-biller/getOrderByOrderId/{orderId}")
	public Order getOrderByOrderId(@PathVariable int orderId) {
		return orderService.getOrderByOrderId(orderId);
	}
	
	@GetMapping("/admin/getAllOrders")
	public List<Order> getAllOrders(){
		return orderService.getAllOrders();
	}
}

