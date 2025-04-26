package com.supermarket.cartservice.service;

import java.util.List;
import java.util.Optional;

import com.supermarket.cartservice.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermarket.cartservice.feign.InventoryServiceClient;
import com.supermarket.cartservice.model.Cart;
import com.supermarket.cartservice.model.CartItems;
import com.supermarket.cartservice.repository.CartItemsRepository;
import com.supermarket.cartservice.repository.CartRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemsRepository cartItemsRepository;

	@Autowired
	private InventoryServiceClient inventoryServiceClient;
	
	
	public CartService(CartRepository cartRepository, CartItemsRepository cartItemsRepository,
			InventoryServiceClient inventoryServiceClient) {
		super();
		this.cartRepository = cartRepository;
		this.cartItemsRepository = cartItemsRepository;
		this.inventoryServiceClient = inventoryServiceClient;
	}
	
	// increase price
	public double increasePrice(double cartTotalPrice, double totalPrice) {
		return cartTotalPrice + totalPrice;
	}
	
	// decrease price
	public double decreasePrice(double cartTotalPrice, double totalPrice) {
		return cartTotalPrice - totalPrice;
	}
	
	
	
	/**
	 * Adds a product to the user's cart. If the cart does not yet exist for the user,
	 * a new cart is created. The method calculates the total price for the product based
	 * on the quantity and updates the cart's total price accordingly.
	 *
	 * @param userId The ID of the user for whom the cart needs to be updated.
	 * @param prodName The name of the product to be added to the cart.
	 * @param quantity The quantity of the product to be added to the cart.
	 */
	public void addToCart(int userId, String prodName, int quantity) {
		ProductResponse product = inventoryServiceClient.getProductByProdName(prodName);
		int prodId = product.getProdId();
		double prodPrice = product.getPrice();
		double totalPrice = prodPrice * quantity;
		
		Optional<Cart> cart = cartRepository.findByUserId(userId);
		Cart newCart;

		CartItems item = new CartItems();
		if(cart.isEmpty()) {
			Cart emptyCart = new Cart();
			emptyCart.setUserId(userId);
			emptyCart.setCartTotalPrice(0.0);
			cartRepository.save(emptyCart);
			newCart=emptyCart;
		}else {
			newCart = cart.get();
		}
		item.setCart(newCart);
		item.setProdId(prodId);
		item.setProdName(prodName);
		item.setQuantity(quantity);
		item.setPrice(prodPrice);
		item.setTotalPrice(totalPrice);

		cartItemsRepository.save(item);
		
		double cartTotal = increasePrice(newCart.getCartTotalPrice(), totalPrice);
		newCart.setCartTotalPrice(cartTotal);
		cartRepository.save(newCart);
	}

	public Cart getCartByUserId(int userId){
		return cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("No cart available for given userId"));
	}
	
	//increase quantity
	public void increaseQuantity(int userId, String prodName) {
		Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found."));

		for(CartItems item :cart.getItems()) {
			if(item.getProdName().equalsIgnoreCase(prodName)) {
				item.setQuantity(item.getQuantity() + 1);
				double totalPrice = item.getTotalPrice() + item.getPrice();
				double updatedPrice = increasePrice(cart.getCartTotalPrice(), totalPrice);
				item.setTotalPrice(totalPrice);
				cart.setCartTotalPrice(updatedPrice);
			}
		}
		cartRepository.save(cart);
	}
	
	// decrease quantity
	public void decreaseQuantity(int userId, String prodName) {
		Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found."));
		for(CartItems item :cart.getItems()) {
			if(item.getProdName().equalsIgnoreCase(prodName)) {
				item.setQuantity(item.getQuantity() - 1);
				double totalPrice = item.getTotalPrice() - item.getPrice();
				double updatedPrice = decreasePrice(cart.getCartTotalPrice(), totalPrice);
				item.setTotalPrice(totalPrice);
				cart.setCartTotalPrice(updatedPrice);
			}
		}
		cartRepository.save(cart);
	}

	@Transactional
	public CartItems removeItemFromCart(int userId, String prodName) {
        Cart cart = getCartByUserId(userId);
        CartItems item = cartItemsRepository.findByCartCartIdAndProdName(cart.getCartId(), prodName).orElseThrow(() -> new RuntimeException("Cart item not found in cart for user: " + userId));
        double updatedPrice = decreasePrice(cart.getCartTotalPrice(), item.getTotalPrice());
        cart.setCartTotalPrice(updatedPrice);
        cart.getItems().remove(item);
        cartItemsRepository.delete(item);
        cartRepository.save(cart);
        return item;
    }
	
	@Transactional
	public void clearCart(int userId) {
		List<CartItems> itemsList = cartItemsRepository.findByCart_UserId(userId);
		Cart cart = cartRepository.findByUserId(userId).orElseThrow();
		if(itemsList.isEmpty()) {
	        System.out.println("Cart is already empty for user: " + userId);
	    }
        cartItemsRepository.deleteAll(itemsList);
        
        List<CartItems> list = cart.getItems();
        list.clear();
        cart.setItems(list);
        cart.setCartTotalPrice(0.0);
        
        for(CartItems item: itemsList) {
        	inventoryServiceClient.reduceStock(item.getProdId(), item.getQuantity());
        }
        
        cartRepository.save(cart);
    }

	public int getCartIdByUserId(int userId){
		 Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
		 return cart.getCartId();
	}
	
	@Transactional
	public void deleteCart(int cartId) {
		cartItemsRepository.deleteByCart_CartId(cartId);
		cartRepository.deleteById(cartId);
		
	}

	public Cart getMyCart(int userId) {
		System.out.println("getMyCart called from cart repository");
		return cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("No cart available for given userId"));
	}
}