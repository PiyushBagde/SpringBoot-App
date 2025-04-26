package com.supermarket.paymentservice.service;

import com.supermarket.paymentservice.dto.OrderDto;
import com.supermarket.paymentservice.exception.ResourceNotFoundException;
import com.supermarket.paymentservice.feign.BillingServiceClient;
import com.supermarket.paymentservice.model.PaymentMode;
import com.supermarket.paymentservice.model.Transaction;
import com.supermarket.paymentservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BillingServiceClient billingServiceClient;

    public Transaction proceedTransaction(int orderId, PaymentMode paymentMode) {
        // Step 1: Get order info from billing-service using Feign client
        OrderDto order = billingServiceClient.getOrderByOrderId(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setOrderId(orderId);
        newTransaction.setUserId(order.getUserId());
        newTransaction.setRequiredAmount(order.getTotalBillPrice());
        newTransaction.setPaymentMode(paymentMode);
        newTransaction.setPaymentTime(LocalDateTime.now());
        return transactionRepository.save(newTransaction);
    }

    public void verifyTransaction(Transaction transaction, double receivedAmount) {
        if (receivedAmount >= transaction.getRequiredAmount()) {
            transaction.setBalanceAmount(0.0);
            transaction.setReceivedAmount(receivedAmount);
            transaction.setPaymentStatus("Completed");
            System.out.println(transaction.getUserId());
        } else if (receivedAmount < transaction.getRequiredAmount()) {
            transaction.setBalanceAmount(transaction.getRequiredAmount() - receivedAmount);

            transaction.setReceivedAmount(receivedAmount);
            transaction.setPaymentStatus("Incomplete");
        }
    }

    public Transaction payByCard(int orderId, double receivedAmount, String cardNumber, String cardHolderName) {
        Transaction transaction = proceedTransaction(orderId, PaymentMode.CARD);
        if (cardNumber == null || cardHolderName == null || cardHolderName.isBlank()) {
            throw new IllegalArgumentException("Card number and holder name are required");
        }
        transaction.setCardNumber(cardNumber);
        transaction.setCardHolderName(cardHolderName);
        verifyTransaction(transaction, receivedAmount);
        return transactionRepository.save(transaction);
    }

    public Transaction payByUpi(int orderId, double receivedAmount, String upiId) {
        Transaction transaction = proceedTransaction(orderId, PaymentMode.UPI);
        if (upiId == null || upiId.isBlank()) {
            throw new IllegalArgumentException("UPI ID is required for UPI payments");
        }
        verifyTransaction(transaction, receivedAmount);
        transaction.setUpiId(upiId);
        transaction.setTransactionTime(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public Transaction payByCash(int orderId, double receivedAmount) {
        Transaction transaction = proceedTransaction(orderId, PaymentMode.CASH);
        verifyTransaction(transaction, receivedAmount);
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

    public List<Transaction> getAllPaymentsByUserId(int userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    public Transaction getMyTransactionById(int userId, int transactionId) {
        return transactionRepository.findByTransactionIdAndUserId(transactionId, userId).orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + transactionId));
    }
}
















