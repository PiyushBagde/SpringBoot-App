package com.supermarket.cartservice.controller;

import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;

import com.supermarket.cartservice.model.Cart;
import com.supermarket.cartservice.model.CartItems;
import com.supermarket.cartservice.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@PostMapping("/add")
	public String addToCart(@RequestParam int userId, @RequestParam int prodId, @RequestParam int quantity) {
		// TODO check when user tries to add same product again
		cartService.addToCart(userId, prodId, quantity);
		return "Product with product id " + prodId + " added to cart " + userId;
	}

	@GetMapping("/getCartIdByUserId/{userId}")
	public int getCartIdByUserId(@PathVariable int userId) {
		return cartService.getCartIdByUserId(userId);
	}

//	 route for logged user
	@GetMapping("/getMyCart")
	public Cart getMyCart(@RequestHeader("X-UserId") int userId) {
		System.out.println("**getCartByUser userId = " + userId);
		return cartService.getMyCart(userId);
	}
	

	@GetMapping("/getCartByUser/{userId}")
	public Cart getCartByUser(@PathVariable int userId) {

		return cartService.getCartByUserId(userId);
	}
	
	@PutMapping("/increaseQuantity")
	public String increaseQuantity(@RequestParam int cartId, @RequestParam int prodId) {
		cartService.increaseQuantity(cartId, prodId);
		return "Increased item quantity by one";
	}
	
	@PutMapping("/decreaseQuantity")
	public String decreaseQuantity(@RequestParam int cartId, @RequestParam int prodId) {
		cartService.decreaseQuantity(cartId, prodId);
		return "Decreased item quantity by one";
	}
		
	@DeleteMapping("/remove")
    public CartItems removeFromCart(@RequestParam int userId, @RequestParam int prodId) {
        return cartService.removeItemFromCart(userId, prodId);
        // return "Item removed from cart";
    }
	
	@DeleteMapping("/clearCart/{userId}")
	public String clearCart(@PathVariable int userId) {
		cartService.clearCart(userId);
		return "Cart cleared successfully.";
	}
	
	@DeleteMapping("/deleteCart/{cartId}")
	public String deleteCart(@PathVariable int cartId) {
		cartService.deleteCart(cartId);
		return "Cart deleted sucessfully";
	}
	
}
