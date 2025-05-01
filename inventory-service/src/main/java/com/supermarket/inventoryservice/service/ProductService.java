package com.supermarket.inventoryservice.service;

import com.supermarket.inventoryservice.exception.InsufficientStockException;
import com.supermarket.inventoryservice.exception.OperationFailedException;
import com.supermarket.inventoryservice.exception.ResourceAlreadyExistsException;
import com.supermarket.inventoryservice.exception.ResourceNotFoundException;
import com.supermarket.inventoryservice.model.Category;
import com.supermarket.inventoryservice.model.Product;
import com.supermarket.inventoryservice.repository.CategoryRepository;
import com.supermarket.inventoryservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Product addProduct(Product product) {
        Category category = categoryRepository.findByCategoryName(product.getCategory().getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot add product. Category not found with name: " + product.getCategory().getCategoryName()));
        product.setCategory(category);

        if (productRepository.findByProdName(product.getProdName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Product with name '" + product.getProdName() + "' already exists.");
        }

        try {
            return productRepository.save(product);
        } catch (DataAccessException e) {
            throw new OperationFailedException("Failed to add product: " + product.getProdName());
        } catch (Exception e) {
            throw new OperationFailedException("An unexpected error occurred while adding product: " + product.getProdName());
        }
    }

    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found in the database.");
        }
        return products;
    }

    public List<Product> getProductsByCategoryId(int categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Cannot get products. Category not found with id: " + categoryId));
        List<Product> products = productRepository.findByCategory(category);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found for category id: " + categoryId);
        }
        return products;

    }

    public void reduceStock(int prodId, int quantity) {
        if (quantity <= 0) { // Add check for non-positive quantity
            throw new IllegalArgumentException("Quantity to reduce must be positive.");
        }
        Product product = productRepository.findById(prodId).orElseThrow(() -> new ResourceNotFoundException("Cannot reduce stock. Product not found with id: " + prodId));

        if (product.getStock() < quantity) {
            throw new InsufficientStockException("Not enough stock available for product '" + product.getProdName() + "' (ID: " + prodId + "). Available: " + product.getStock() + ", Requested: " + quantity);
        }

        product.setStock(product.getStock() - quantity);

        try {
            productRepository.save(product);
        } catch (DataAccessException e) {
            throw new OperationFailedException("Failed to update stock for product ID: " + prodId);
        } catch (Exception e) {
            throw new OperationFailedException("An unexpected error occurred while updating stock for product ID: " + prodId);
        }
    }

    @Transactional
    public Product updateProduct(int prodId, Product updatedproduct) {
        Product existingProduct = productRepository.findById(prodId).orElseThrow(() -> new ResourceNotFoundException("Cannot update. Product not found with id: " + prodId));

        if (updatedproduct.getPrice() < 0) throw new IllegalArgumentException("Price cannot be negative.");
        if (updatedproduct.getStock() < 0) throw new IllegalArgumentException("Stock level cannot be negative.");
        if (updatedproduct.getProdName() == null) throw new IllegalArgumentException("Product name cannot be null.");

        existingProduct.setProdName(updatedproduct.getProdName());
        existingProduct.setPrice(updatedproduct.getPrice());
        existingProduct.setStock(updatedproduct.getStock());
        if (updatedproduct.getCategory() != null && updatedproduct.getCategory().getCategoryName() != null) {
            String categoryName = updatedproduct.getCategory().getCategoryName();
            Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(() -> new ResourceNotFoundException("Cannot update product. Category not found: " + categoryName));
            existingProduct.setCategory(category);
        }
        try {
            return productRepository.save(existingProduct);
        } catch (DataAccessException e) {
            throw new OperationFailedException("Failed to update product with ID: " + prodId);
        } catch (Exception e) {
            throw new OperationFailedException("An unexpected error occurred while updating product with ID: " + prodId);
        }
    }

    public Category getCategoryByProduct(int prodId) {
        Product prod = productRepository.findById(prodId).orElseThrow(() -> new ResourceNotFoundException("Cannot get category. Product not found with id: " + prodId));

        if (prod.getCategory() == null) {
            throw new ResourceNotFoundException("Product with id " + prodId + " does not have an associated category.");
        }
        return prod.getCategory();
    }

    @Transactional
    public Product updateQuantity(int productId, int newQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot update quantity. Product not found with id: " + productId));
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }

        product.setStock(newQuantity);
        try {
            return productRepository.save(product);
        } catch (DataAccessException e) {
            throw new OperationFailedException("Failed to update quantity for product ID: " + productId);
        } catch (Exception e) {
            throw new OperationFailedException("An unexpected error occurred while updating quantity for product ID: " + productId);
        }
    }

    @Transactional
    public void deleteProd(int prodId) {
        if (!productRepository.existsById(prodId)) {
            throw new ResourceNotFoundException("Cannot delete. Product not found with id: " + prodId);
        }
        try {
            productRepository.deleteById(prodId);
        } catch (DataAccessException e) {
            throw new OperationFailedException("Failed to delete product with ID: " + prodId);
        } catch (Exception e) {
            throw new OperationFailedException("An unexpected error occurred while deleting product with ID: " + prodId);
        }
    }

    public Product getProductByProdName(String prodName) {
        return productRepository.findByProdName(prodName).orElseThrow(() -> new ResourceNotFoundException("Product not found with name: " + prodName));
    }

    public List<Product> getProductsByCategoryName(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(() -> new ResourceNotFoundException("Cannot get products. Category not found with name: " + categoryName));
        List<Product> productList = productRepository.findByCategory(category);

        if (productList.isEmpty()) {
            throw new ResourceNotFoundException("No product found for category: " + categoryName);
        }

        return productList;
    }
}
