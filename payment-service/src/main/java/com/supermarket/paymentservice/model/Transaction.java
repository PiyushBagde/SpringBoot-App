package com.supermarket.paymentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment")
public class Transaction {
    @Id
    @Column(name = "transaction_id") //modify to 32 bit
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "required_amount")
    private double requiredAmount;

    @Column(name = "received_amount")
    private double receivedAmount;

    @Column(name = "balance_amount")
    private double balanceAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode; //Cash, Card, UPI

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    private String upiId; // For UPI
    private LocalDateTime transactionTime;

    private String cardNumber; // For Card
    private String cardHolderName; // Optional


}
