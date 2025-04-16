package com.supermarket.cartservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.supermarket.cartservice.model.Cart;
import com.supermarket.cartservice.model.CartItems;

import jakarta.transaction.Transactional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Integer>{
	List<CartItems> findByCart_UserId(int userId); // Get all cart items for a user

    // Optional<CartItems> findByCartAndProductId(Cart cart, int prod_id); // Find specific item in cart

    void deleteByCart(Cart cart); 
    
    @Transactional
    void deleteByCart_CartId(int cartId);

	CartItems getByProdId(int prodId);

	void deleteAllByCartItemId(int cartItemId);

	CartItems getByCartAndProdId(Cart cart, int prodId);

	@Transactional
	void deleteByCartItemId(int cartItemId);
	
	@Transactional
	void deleteById(int cartItemId);

	CartItems findByCartCartIdAndProdId(int cartId, int prodId);

	@Transactional
	void deleteAllByCartCartId(int cartId);

}
