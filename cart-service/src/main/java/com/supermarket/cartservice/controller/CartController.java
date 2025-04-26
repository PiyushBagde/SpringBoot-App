package com.supermarket.cartservice.controller;

import com.supermarket.cartservice.model.Cart;
import com.supermarket.cartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // routes for customer
    @PostMapping("/biller/addToCart")
    public String addToCart(@RequestParam int userId, @RequestParam String prodName, @RequestParam int quantity) {
        // TODO check when user tries to add same product again
        System.out.println("Adding cart to user " + userId + " product " + prodName);
        cartService.addToCart(userId, prodName, quantity);
        return prodName + " added to cart of userId" + userId;
    }

    @DeleteMapping("/biller/removeItemFromCart")
    public String removeItemFromCart(@RequestParam int userId, @RequestParam String prodName) {
        cartService.removeItemFromCart(userId, prodName);
        return "product removed from cart of userId" + userId + "successfully";
    }

    @PostMapping("/customer/addToMyCart")
    public String addToMyCart(@RequestHeader("X-UserId") int userId, @RequestParam String prodName, @RequestParam int quantity) {
        cartService.addToCart(userId, prodName, quantity);
        return "Product with product name " + prodName + " added to cart wiht userId " + userId;
    }

    @GetMapping("/customer/getMyCart") //	 route for logged user
    public Cart getMyCart(@RequestHeader("X-UserId") int userId) {
        System.out.println("**getCartByUser userId = " + userId);
        return cartService.getMyCart(userId);
    }


    @DeleteMapping("/customer/removeItemFromMyCart")
    public String removeFromMyCart(@RequestHeader("X-UserId") int userId, @RequestParam String prodName) {
        cartService.removeItemFromCart(userId, prodName);
        return "Item removed from cart successfully.";
    }


    @GetMapping("/biller/getCartByUser/{userId}")
    public Cart getCartByUser(@PathVariable int userId) {

        return cartService.getCartByUserId(userId);
    }

    @PutMapping("/customer/increaseQuantity")
    public String increaseQuantity(@RequestHeader("X-UserId") int userId, @RequestParam String prodName) {
        cartService.increaseQuantity(userId, prodName);
        return "Increased item quantity by one";
    }

    @PutMapping("/customer/decreaseQuantity")
    public String decreaseQuantity(@RequestHeader("X-UserId") int userId, @RequestParam String prodName) {
        cartService.decreaseQuantity(userId, prodName);
        return "Decreased item quantity by one";
    }

    @PutMapping("/biller/increaseQuantity")
    public String increaseQuantityFromUserCart(@RequestParam int userId, @RequestParam String prodName) {
        cartService.increaseQuantity(userId, prodName);
        return "Increased item quantity by one";
    }

    @PutMapping("/biller/decreaseQuantity")
    public String decreaseQuantityFromUserCart(@RequestParam int userId, @RequestParam String prodName) {
        cartService.decreaseQuantity(userId, prodName);
        return "Decreased item quantity by one";
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


    @GetMapping("/getCartIdByUserId/{userId}")
    public int getCartIdByUserId(@PathVariable int userId) {
        return cartService.getCartIdByUserId(userId);
    }

}
