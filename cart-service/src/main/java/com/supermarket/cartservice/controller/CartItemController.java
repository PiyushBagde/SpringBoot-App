package com.supermarket.cartservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.supermarket.cartservice.model.CartItems;
import com.supermarket.cartservice.service.CartItemService;

@RestController
@RequestMapping("/cart")
public class CartItemController {
	
	@Autowired
	private CartItemService cartItemService;
	
	@PostMapping("/addToCartItem/")
	public String addToCartItem(@RequestBody CartItems cartItem) {
		CartItems item =  cartItemService.addItemInCart(cartItem);
		return item.getProdName() + " added to cart of userId" + item.getCart().getUserId();
	}
	
	@GetMapping("/getCartItemsByUserId/{userId}")
	public List<CartItems> getCartItemsByUserId(@PathVariable int userId){
		return cartItemService.getCartItemsByUserId(userId);
	}
	
	@PutMapping("/updateItemQuantity/{cartItemId}/{quantity}")
	public CartItems updateItemQuantity(@PathVariable int cartItemId,@PathVariable int quantity) {
		return cartItemService.updateItemQuantity(cartItemId, quantity);
	}
	
	@DeleteMapping("/removeCartItem/{cartItemId}")
	public String removeCartItem(@PathVariable int cartItemId) {
		cartItemService.removeCartItem(cartItemId);
		return "Item removed from cart successfully.";
	}

} 
