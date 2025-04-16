package com.supermarket.cartservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermarket.cartservice.model.Cart;
import com.supermarket.cartservice.model.CartItems;
import com.supermarket.cartservice.repository.CartItemsRepository;
import com.supermarket.cartservice.repository.CartRepository;

@Service
public class CartItemService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemsRepository cartItemsRepository;

	
	
	public CartItems addIteminCart(int userId, CartItems cartItems) {
		return cartItemsRepository.save(cartItems);
	}
	
	public List<CartItems> getCartItemsByUserId(int userId){
		Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart item not found"));
		List<CartItems> itemList = cart.getItems();
		return itemList;
	}
	
//	public double getProductPrice(int prodId) {
//		
//	}
	
	public CartItems updateItemQuantity(int cartItemId, int quantity) {
		CartItems item = cartItemsRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("Cart item not found"));
		item.setQuantity(quantity);
		return cartItemsRepository.save(item);
	}
	
	public void removeCartItem(int cartItemId) {
		cartItemsRepository.deleteById(cartItemId);
	}
}
