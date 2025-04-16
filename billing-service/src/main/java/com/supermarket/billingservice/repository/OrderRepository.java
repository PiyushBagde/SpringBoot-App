package com.supermarket.billingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supermarket.billingservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	List<Order> findByUserId(int userId);

	List<Order> findAllByUserId(int userId);

}
