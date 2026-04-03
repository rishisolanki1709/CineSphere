package com.cinesphere.main.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cinesphere.main.entity.Payment;

public class AdminPaymentResponseDTO {
	private Long id;
	private String razorpayOrderId;
	private String userEmail;
	private BigDecimal amount;
	private String status;
	private String movieName;
	private LocalDateTime createdAt;

	// Constructor to map from Entity
	public AdminPaymentResponseDTO(Payment p) {
		this.id = p.getId();
		this.razorpayOrderId = p.getOrderId();
		this.userEmail = p.getBooking().getUser().getEmail();
		this.amount = p.getAmount();
		this.status = p.getStatus().toString();
		this.movieName = p.getBooking().getShow().getMovie().getTitle();
		this.createdAt = p.getCreatedAt();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazorpayOrderId() {
		return razorpayOrderId;
	}

	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}