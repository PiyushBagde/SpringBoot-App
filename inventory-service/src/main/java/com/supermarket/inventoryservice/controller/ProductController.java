package com.supermarket.inventoryservice.controller;

import com.supermarket.inventoryservice.model.Category;
import com.supermarket.inventoryservice.model.Product;
import com.supermarket.inventoryservice.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invent")
@Validated // enabling parameter validation
public class ProductController {

    @Autowired
    private ProductService productService;

    // routes for admin
    @PostMapping("/admin/addProduct") //give category id while specifying category in body
    public String addProduct(@Valid @RequestBody Product product) {
        Product addedProduct = productService.addProduct(product);
        return addedProduct.getProdName() + " added successfully";
    }

    @PutMapping("/admin/updateProduct/{prodId}")
    public Product updateProd(
            @PathVariable @NotNull(message = "Category cannot be null") int prodId,
            @Valid @RequestBody Product updatedproduct) { // category must be passed here
        return productService.updateProduct(prodId, updatedproduct);

    }

    @DeleteMapping("/admin/deleteProduct/{prodId}")
    public String deleteProd(@PathVariable @Min(value = 1, message = "Product ID must be positive") int prodId) {
        productService.deleteProd(prodId);
        return "Product deleted successfully";
    }

    // need to be looked
    @GetMapping("/admin/getAllProducts")
    public List<Product> getAllProd() {
        return productService.getAllProducts();
    }

    @PutMapping("/admin/updateQuantity/{prodId}")
    public Product updateQty(
            @PathVariable @Min(value = 1, message = "Product ID must be positive") int prodId,
            @RequestParam @PositiveOrZero(message = "New quantity cannot be negative") int newQuantity) {
        return productService.updateQuantity(prodId, newQuantity);
    }

    // routes for biller and customer
    @PutMapping("/reduceStock/{productId}/{quantity}")
    public void reduceStock(
            @PathVariable @Min(value = 1, message = "Product ID must be positive") int productId,
            @PathVariable @Positive(message = "Quantity to reduce must be positive") int quantity) {
        productService.reduceStock(productId, quantity);
    }

    @GetMapping("/biller-customer/getProductById/{id}")
    public Product getProductById(@PathVariable @Min(value = 1, message = "Product ID must be positive") int id) {
        return productService.getProductById(id);
    }


    @GetMapping("/biller-customer/getProductsByCategory/{categoryId}")
    public List<Product> getProdByCategory(@PathVariable @Min(value = 1, message = "Category ID must be positive") int categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }

    @GetMapping("/customer/getProductsByCategoryName")
    public List<Product> getProductsByCategoryName(@RequestParam @NotBlank(message = "Category name cannot be blank") String categoryName) {
        return productService.getProductsByCategoryName(categoryName);
    }

    // routes for biller
    @GetMapping("/biller/getCategoryByProduct/{prodId}")
    public Category getCategoryByProduct(@PathVariable @Min(value = 1, message = "Product ID must be positive") int prodId) {
        return productService.getCategoryByProduct(prodId);
    }

    @GetMapping("/getProductByProdName")
    public Product getProductByProdName(@RequestParam @NotBlank(message = "Product name cannot be blank") String prodName) {
        return productService.getProductByProdName(prodName);
    }

}