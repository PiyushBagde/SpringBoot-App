package com.supermarket.paymentservice.controller;

import com.supermarket.paymentservice.model.PaymentMode;
import com.supermarket.paymentservice.model.Transaction;
import com.supermarket.paymentservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/biller-customer/payByCard")
    public Transaction payByCard(@RequestParam int orderId, @RequestParam double receivedAmount, @RequestParam String cardNumber, @RequestParam String cardHolderName) {
        return transactionService.payByCard(orderId, receivedAmount, cardNumber, cardHolderName);
    }

    @PostMapping("/biller-customer/payByUpi")
    public Transaction payByUpi(@RequestParam int orderId, @RequestParam double receivedAmount, @RequestParam String upiId) {
        return transactionService.payByUpi(orderId, receivedAmount, upiId);
    }

    @PostMapping("/biller-customer/payByCash")
    public Transaction payByCash(@RequestParam int orderId, @RequestParam double receivedAmount) {
        return transactionService.payByCash(orderId, receivedAmount);
    }

    @GetMapping("/admin/getPaymentById/{transactionId}")
    public Transaction getPaymentById(@PathVariable int transactionId) {
        return transactionService.getPaymentById(transactionId);
    }

    @GetMapping("/admin/getPaymentByMode/{mode}")
    public List<Transaction> getPaymentsByMode(@PathVariable PaymentMode mode) {
        return transactionService.getPaymentsByMode(mode);
    }

    @GetMapping("/admin/getAllPayments")
    public List<Transaction> getAllPayments() {
        return transactionService.getAllPayments();

    }

    // complete get my transactions
    @GetMapping("/customer/getMyTransactions")
    public List<Transaction> getMyTransactions(@RequestHeader("X-UserId") int userId) {
        return transactionService.getAllPaymentsByUserId(userId);
    }

    @GetMapping("/customer/getMyTransactionById/{transactionId}")
    public Transaction getMyTransactionById(@RequestHeader("X-UserId") int userId, @PathVariable int transactionId) {
        return transactionService.getMyTransactionById(userId, transactionId);
    }
}