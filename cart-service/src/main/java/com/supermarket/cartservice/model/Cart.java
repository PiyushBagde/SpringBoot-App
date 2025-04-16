package com.supermarket.cartservice.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cart {
	
	@Id
	@Column(name = "cart_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartId;
	
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "cart_total_price")
	private double cartTotalPrice;


	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<CartItems> items = new ArrayList<>();

	public Cart() {
		// TODO Auto-generated constructor stub
	}


	public double getCartTotalPrice() {
		return cartTotalPrice;
	}

	public void setCartTotalPrice(double cartTotalPrice) {
		this.cartTotalPrice = cartTotalPrice;
	}
	
	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public int getUserId() {
		return userId;
	}

	public List<CartItems> getItems() {
		return items;
	}

	public void setItems(List<CartItems> items) {
		this.items = items;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	
	
}
