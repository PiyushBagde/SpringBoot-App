package com.supermarket.billingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supermarket.billingservice.model.OrderItems;

public interface OrderitemsRepository extends JpaRepository<OrderItems, Integer> {

}
