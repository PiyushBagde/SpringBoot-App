package com.supermarket.cartservice.service;

import com.supermarket.cartservice.exception.OperationFailedException;
import com.supermarket.cartservice.exception.ResourceNotFoundException;
import com.supermarket.cartservice.model.Cart;
import com.supermarket.cartservice.model.CartItems;
import com.supermarket.cartservice.repository.CartItemsRepository;
import com.supermarket.cartservice.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Transactional
    public CartItems addItemInCart(CartItems cartItem) {
        if (cartItem.getCart() == null || cartItem.getCart().getCartId() == 0) {
            throw new IllegalArgumentException("Cart information is missing in CartItems object.");
        }
        // Ensure the referenced cart actually exists
        cartRepository.findById(cartItem.getCart().getCartId()).orElseThrow(() -> new ResourceNotFoundException("Cannot add item. Associated cart not found with ID: " + cartItem.getCart().getCartId()));

        try {
            return cartItemsRepository.save(cartItem);
        } catch (DataAccessException e) {
            throw new OperationFailedException("Failed to save cart item.", e);
        }
    }

    public List<CartItems> getCartItemsByUserId(int userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("No cart found for user ID: " + userId + " to retrieve items."));
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return List.of(); // Return empty list
        }
        return cart.getItems();
    }

    @Transactional
    public CartItems updateItemQuantity(int cartItemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        CartItems item = cartItemsRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + cartItemId));
        item.setQuantity(quantity);
        item.setTotalPrice(item.getPrice() * quantity); // Recalculate based on stored unit price

        try {
            return cartItemsRepository.save(item);
        } catch (DataAccessException e) {
            throw new OperationFailedException("Failed to update quantity for cart item ID: " + cartItemId, e);
        }
    }

    @Transactional
    public void removeCartItem(int cartItemId) {

        // Check if item exists before attempting to delete for better error handling
        cartItemsRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cannot remove. Cart item not found with ID: " + cartItemId));
        try {
            cartItemsRepository.deleteById(cartItemId);

        } catch (DataAccessException e) {
            // If foreign key issues occur, this might indicate related data exists unexpectedly
            throw new OperationFailedException("Failed to remove cart item with ID: " + cartItemId, e);
        }
    }
}