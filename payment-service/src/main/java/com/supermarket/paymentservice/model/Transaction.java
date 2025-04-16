package com.supermarket.paymentservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment")
public class Transaction {
	@Id
	@Column(name = "transaction_id") //modify to 32 bit
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int transactionId;
	
	@Column(name = "userId")
	private int userId;
	
	@Column(name = "order_id")
	private int orderId;
	
	@Column(name = "required_amount")
	private double requiredAmount;
	
	@Column(name = "recieved_amount")
	private double recievedAmount;
	
	@Column(name = "balance_amount")
	private double balanceAmount;
	
	public double getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
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
    
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public double getRequiredAmount() {
		return requiredAmount;
	}
	public void setRequiredAmount(double requiredAmount) {
		this.requiredAmount = requiredAmount;
	}
	public double getRecievedAmount() {
		return recievedAmount;
	}
	public void setRecievedAmount(double recievedAmount) {
		this.recievedAmount = recievedAmount;
	}
	public PaymentMode getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public LocalDateTime getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(LocalDateTime paymentTime) {
		this.paymentTime = paymentTime;
	}
	public String getUpiId() {
		return upiId;
	}
	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}
    
	
	
}
