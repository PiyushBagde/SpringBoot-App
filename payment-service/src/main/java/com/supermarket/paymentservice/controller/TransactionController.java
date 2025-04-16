package com.supermarket.paymentservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.supermarket.paymentservice.model.PaymentMode;
import com.supermarket.paymentservice.model.Transaction;
import com.supermarket.paymentservice.service.TransactionService;

@RestController
@RequestMapping("/payment")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/payByCard")
    public Transaction payByCard(
            @RequestParam int orderId,
            @RequestParam double recievedAmount,
            @RequestParam String cardNumber,
            @RequestParam String cardHolderName
         
    ) {
    	return transactionService.payByCard(orderId, recievedAmount, cardNumber, cardHolderName);
    }
    	
    @PostMapping("/payByUpi")
    public Transaction payByUpi(
            @RequestParam int orderId,
            @RequestParam double recievedAmount,
            @RequestParam String upiId
         
    ) {
    	return transactionService.payByUpi(orderId, recievedAmount, upiId);
    }

    @PostMapping("/payByCash")
    public Transaction payByCash(
            @RequestParam int orderId,
            @RequestParam double recievedAmount
         
    ) {
    	return transactionService.payByCash(orderId, recievedAmount);
    }
    
    @GetMapping("/getPaymentById/{transactionId}")
    public Transaction getPaymentById(@PathVariable int transactionId) {
        return transactionService.getPaymentById(transactionId);
    }

    @GetMapping("/getPaymentByMode/{mode}")
    public List<Transaction> getPaymentsByMode(@PathVariable PaymentMode mode){
    	return transactionService.getPaymentsByMode(mode);
    }
    
    @GetMapping("/getAllPayments")
    public List<Transaction> getAllPayments() {
    	return transactionService.getAllPayments();
    	
    }
    
    
    
    
    
    
//    @GetMapping("/order/{orderId}")
//    public ResponseEntity<?> getPaymentByOrderId(@PathVariable int orderId) {
//        Payment payment = paymentService.getPaymentByOrderId(orderId);
//        if (payment == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(payment);
//    }
}