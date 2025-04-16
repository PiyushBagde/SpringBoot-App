package com.supermarket.paymentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.supermarket.paymentservice.dto.OrderDto;

@FeignClient(name = "billing-service", url = "${billing-service.url}")
public interface BillingServiceClient {

    @GetMapping("/bill/getOrderByOrderId/{orderId}")
	OrderDto getOrderByOrderId(@PathVariable int orderId) ;
    
    
}