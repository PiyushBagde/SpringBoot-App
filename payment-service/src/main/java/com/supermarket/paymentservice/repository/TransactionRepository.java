package com.supermarket.paymentservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.supermarket.paymentservice.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
	List<Transaction> findByUserId(int userId);

	Transaction findByOrderId(int orderId);
}
