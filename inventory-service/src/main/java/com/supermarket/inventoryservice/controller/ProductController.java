package com.supermarket.inventoryservice.controller;

import com.supermarket.inventoryservice.exception.ResourceAlreadyExistsException;
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
    public String addProduct(@RequestBody Product product) throws ResourceAlreadyExistsException {
        Product addedProduct = productService.addProduct(product);
        return addedProduct.getProdName() + " added successfully";
    }

    @PutMapping("/admin/updateProduct/{prodId}")
    public Product updateProd(@PathVariable int prodId, @RequestBody Product updatedproduct) {
        return productService.updateProduct(prodId, updatedproduct);

    }

    @DeleteMapping("/admin/deleteProduct/{prodId}")
    public String deleteProd(@PathVariable int prodId) {
        productService.deleteProd(prodId);
        return "Product deleted successfully";
    }

    // need to be looked
    @GetMapping("/admin/getAllProducts")
    public List<Product> getAllProd() {
        return productService.getAllProducts();
    }

    @PutMapping("/admin/updateQuantity/{prodId}")
    public Product updateQty(@PathVariable int prodId, @RequestParam int newQuantity) {
        return productService.updateQuantity(prodId, newQuantity);
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
    @GetMapping("/biller/getCategoryByProduct/{prodId}")
    public Category getCategoryByProduct(@PathVariable int prodId) {
        return productService.getCategoryByProduct(prodId);
    }

    @GetMapping("/getProductByProdName")
    public Product getProductByProdName(@RequestParam String prodName) {
        return productService.getProductByProdName(prodName);
    }

}