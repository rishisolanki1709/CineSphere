package com.cinesphere.main.dto;

import java.time.LocalDateTime;

import com.cinesphere.main.entity.Booking;

public class AdminBookingSummaryDTO {
	private Long id;
	private String userEmail;
	private String movieName;
	private Double amount;
	private String status;
	private LocalDateTime date;

	// Constructor to map from Entity
	public AdminBookingSummaryDTO(Booking b) {
		this.id = b.getId();
		this.userEmail = b.getUser().getEmail();
		this.movieName = b.getShow().getMovie().getTitle();
		this.amount = b.getTotalAmount().doubleValue();
		this.status = b.getStatus().toString();
		this.date = b.getBookedAt();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}