package com.supermarket.cartservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.supermarket.cartservice.dto.ProductResponse;

@FeignClient(name = "inventory-service", url = "${inventory-service.url}")
public interface InventoryServiceClient {

	@GetMapping("/invent/getProductById/{id}")
	ProductResponse getProductById(@PathVariable int id) ;
	
	@PutMapping("/invent/reduceStock/{productId}/{quantity}")
	public void reduceStock(@PathVariable int productId, @PathVariable int quantity);
	
}
