package com.supermarket.inventoryservice.controller;

import com.supermarket.inventoryservice.model.Category;
import com.supermarket.inventoryservice.model.Product;
import com.supermarket.inventoryservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invent")
public class ProductController {

    @Autowired
    private ProductService productService;

    // routes for admin
    @PostMapping("/admin/addProduct") //give category id while specifying category in body
    public String addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return "Product added successfully";
    }

    @PutMapping("/admin/updateProduct/{prod_id}")
    public Product updateProd(@PathVariable int prod_id, @RequestBody Product updatedproduct) {
        return productService.updateProduct(prod_id, updatedproduct);

    }

    @DeleteMapping("/admin/deleteProduct/{prod_id}")
    public String deleteProd(@PathVariable int prod_id) {
        productService.deleteProd(prod_id);
        return "Product deleted successfully";
    }

    // need to be looked
    @GetMapping("/admin/getAllProducts")
    public List<Product> getAllProd() {
        return productService.getAllProducts();
    }

    @PutMapping("/admin/updateQuantity/{prod_id}")
    public Product updateQty(@PathVariable int prod_id, @RequestParam int newQuantity) {
        return productService.updateQuantity(prod_id, newQuantity);
    }

    // routes for biller and customer
    @PutMapping("/reduceStock/{productId}/{quantity}")
    public void reduceStock(@PathVariable int productId, @PathVariable int quantity) {
        productService.reduceStock(productId, quantity);
    }

    @GetMapping("/biller-customer/getProductById/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }


    @GetMapping("/biller-customer/getProductsByCategory/{categoryId}")
    public List<Product> getProdByCategory(@PathVariable int categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }

    @GetMapping("/customer/getProductsByCategoryName")
    public List<Product> getProductsByCategoryName(@RequestParam String categoryName) {
        return productService.getProductsByCategoryName(categoryName);
    }

    // routes for biller

    @GetMapping("/biller/getCategoryByProduct/{prod_id}")
    public Category getCategoryByProduct(@PathVariable int prod_id) {
        return productService.getCategoryByProduct(prod_id);
    }

    @GetMapping("/getProductByProdName")
    public Product getProductByProdName(@RequestParam String prodName) {
        return productService.getProductByProdName(prodName);
    }

}