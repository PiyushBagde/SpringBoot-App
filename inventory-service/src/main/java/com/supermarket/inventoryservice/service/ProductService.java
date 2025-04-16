package com.supermarket.inventoryservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermarket.inventoryservice.exception.ResourceNotFoundException;
import com.supermarket.inventoryservice.model.Category;
import com.supermarket.inventoryservice.model.Product;
import com.supermarket.inventoryservice.repository.CategoryRepository;
import com.supermarket.inventoryservice.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	public ProductRepository getProductRepository() {
		return productRepository;
	}
	
	public Product addProduct(Product product) {
		Category category = categoryRepository.findByCategoryName(product.getCategory().getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + product.getCategory().getCategoryName()));
        product.setCategory(category);
        return productRepository.save(product);
	}
	
	public Product getProductById(int id) {
		return productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id: "+id));
	}
	
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	public List<Product> getProductsByCategory(int categoryid){
		Category category = categoryRepository.findById(categoryid).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryid));
		return productRepository.findByCategory(category);
		
	}
	
	public void reduceStock(int prodId, int quantity) {
    	Product product = productRepository.findById(prodId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + prodId));
    	if (product.getStock() < quantity) {
    		throw new ResourceNotFoundException("Not enough stock available");
    	}
    	product.setStock(product.getStock() - quantity);
    	productRepository.save(product);
    }
	
	public Product updateProduct(int prod_id, Product updatedproduct) {
		Product existingProduct = productRepository.findById(prod_id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: "+ prod_id));
		
		existingProduct.setProdName(updatedproduct.getProdName());
		existingProduct.setPrice(updatedproduct.getPrice());
		existingProduct.setStock(updatedproduct.getStock());
		if (updatedproduct.getCategory() != null) {
            String categoryName = updatedproduct.getCategory().getCategoryName();
            Category category = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryName));
            existingProduct.setCategory(category);
        }
		return productRepository.save(existingProduct);
	}
	
	public Category getCategoryByProduct(int prod_id) {
		Product prod = productRepository.findById(prod_id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: "+ prod_id));
		Category category = prod.getCategory();
		return category;
	}
	
	
    public Product updateQuantity(int productId, int newQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock level cannot be negative.");
        }

        product.setStock(newQuantity);
        return productRepository.save(product);
    }
    
    
    public void deleteProd(int prodId) {
		if(!productRepository.existsById(prodId)) {
			throw new ResourceNotFoundException("User not found with id "+ prodId);
		}
		productRepository.deleteById(prodId);
    }
	
}
