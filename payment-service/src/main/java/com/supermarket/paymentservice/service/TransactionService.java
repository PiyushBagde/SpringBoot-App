package com.supermarket.paymentservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermarket.paymentservice.dto.OrderDto;
import com.supermarket.paymentservice.feign.BillingServiceClient;
import com.supermarket.paymentservice.feign.CartServiceClient;
import com.supermarket.paymentservice.model.PaymentMode;
import com.supermarket.paymentservice.model.Transaction;
import com.supermarket.paymentservice.repository.TransactionRepository;

@Service
public class TransactionService {
	@Autowired
	private TransactionRepository transactionRepository;
	
	 @Autowired
	 private BillingServiceClient billingServiceClient;
	 
	 @Autowired
	 private CartServiceClient cartServiceClient;
	 
	public Transaction proceedTransaction(int orderId, PaymentMode paymentMode,double recievedAmount) {
	    // Step 1: Get order info from billing-service using Feign client
	    OrderDto order = billingServiceClient.getOrderByOrderId(orderId);
	    if (order == null) {
	        throw new RuntimeException("Order not found with ID: " + orderId);
	    }

	    Transaction trnx = new Transaction();
	    trnx.setOrderId(orderId);
	    trnx.setUserId(order.getUserId());
	    trnx.setRequiredAmount(order.getTotalBillPrice());
	    trnx.setPaymentMode(paymentMode);
	    trnx.setPaymentTime(LocalDateTime.now());
	    return transactionRepository.save(trnx);
	}
	
	public void verifyTransaction(Transaction trnx, double requiredAmount, double recievedAmount) {
		if(recievedAmount == trnx.getRequiredAmount()) {
			trnx.setBalanceAmount(0.0);
		    trnx.setRecievedAmount(recievedAmount);
			trnx.setPaymentStatus("Completed");
			System.out.println(trnx.getUserId());
			cartServiceClient.clearCart(trnx.getUserId());
		}
		else if(recievedAmount != trnx.getRequiredAmount()) {
			trnx.setBalanceAmount(trnx.getRequiredAmount() - recievedAmount);

		    trnx.setRecievedAmount(recievedAmount);
			trnx.setPaymentStatus("Incomplete");
		}
	}
	
	public Transaction payByCard(int orderId,double recievedAmount, String cardNumber, String cardHolderName) {
		Transaction trnx = proceedTransaction(orderId, PaymentMode.CARD , recievedAmount);
		if (cardNumber == null || cardHolderName == null || cardHolderName.isBlank()) {
            throw new IllegalArgumentException("Card number and holder name are required");
        }
        trnx.setCardNumber(cardNumber);
        trnx.setCardHolderName(cardHolderName);
        verifyTransaction(trnx, orderId, recievedAmount);
        return transactionRepository.save(trnx);
	}
	
	public Transaction payByUpi(int orderId,double recievedAmount, String upiId) {
		Transaction trnx = proceedTransaction(orderId, PaymentMode.UPI , recievedAmount);
		if (upiId == null || upiId.isBlank()) {
            throw new IllegalArgumentException("UPI ID is required for UPI payments");
        }
        verifyTransaction(trnx, orderId, recievedAmount);
        trnx.setUpiId(upiId);
        trnx.setTransactionTime(LocalDateTime.now());
        return transactionRepository.save(trnx);
	}
	
	public Transaction payByCash(int orderId, double recievedAmount) {
		Transaction trnx = proceedTransaction(orderId, PaymentMode.CASH , recievedAmount);
		verifyTransaction(trnx, orderId, recievedAmount);
		return transactionRepository.save(trnx);
	}
	
	public Transaction saveTransaction(Transaction transaction) {
		transaction.setPaymentTime(LocalDateTime.now());

        if (transaction.getPaymentMode() == PaymentMode.UPI) {
            if (transaction.getUpiId() == null || transaction.getPaymentTime() == null) {
                throw new IllegalArgumentException("UPI details are required.");
            }
        }

        if (transaction.getPaymentMode() == PaymentMode.CARD) {
            if (transaction.getCardNumber() == null || transaction.getCardHolderName() == null) {
                throw new IllegalArgumentException("Card details are required.");
            }
        }

        return transactionRepository.save(transaction);
	}
	
	public Transaction getPaymentById(int transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + transactionId
                		));
    }
	
	public List<Transaction> getPaymentsByMode(PaymentMode mode) {
        return transactionRepository.findAll()
                .stream()
                .filter(p -> p.getPaymentMode() == mode)
                .collect(Collectors.toList());
    }
	
	public List<Transaction> getAllPayments() {
	    return transactionRepository.findAll();
	}
	
	
}
















