package com.supermarket.inventoryservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.supermarket.inventoryservice.model.Category;
import com.supermarket.inventoryservice.model.Product;
import com.supermarket.inventoryservice.service.ProductService;

@RestController
@RequestMapping("/invent")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/addProduct") //give category id while specifying category in body
	public String addProduct(@RequestBody Product product) {
		productService.addProduct(product);
		return "Product added successfully";
	}
	
	@PutMapping("/reduceStock/{productId}/{quantity}")
	public void reduceStock(@PathVariable int productId, @PathVariable int quantity) {
	    productService.reduceStock(productId, quantity);
	}
	
	@GetMapping("/getProductById/{id}")
	public Product getProductById(@PathVariable int id) {
		Product prod = productService.getProductById(id);
		return prod;
	}
	
	@GetMapping("/getAllProducts")
	public List<Product> getAllProd(){
		List<Product> prodList = productService.getAllProducts();
		return prodList;
	}
	
	@GetMapping("/getProductsByCategory/{categoryId}")
	public List<Product> getProdByCategory(@PathVariable int categoryId){
		return productService.getProductsByCategory(categoryId);
	}
	
	@GetMapping("/getCategoryByProduct/{prod_id}")
	public Category getCategoryByProduct(@PathVariable int prod_id) {
		return productService.getCategoryByProduct(prod_id);
	}
	
	
	@PutMapping("/updateProduct/{prod_id}") 
	public Product updateProd(@PathVariable int prod_id, @RequestBody Product updatedproduct) {
		return productService.updateProduct(prod_id, updatedproduct);
	    
	}
	
	@PutMapping("/updateQuantity/{prod_id}")
	public Product updateQty(@PathVariable int prod_id, @RequestParam int newqty) {
		return productService.updateQuantity(prod_id, newqty);
	}
	
	@DeleteMapping("/deleteProduct/{prod_id}")
	public String deleteProd(@PathVariable int prod_id) {
		productService.deleteProd(prod_id);
		return "Product deleted successfully";
	}
	
}
