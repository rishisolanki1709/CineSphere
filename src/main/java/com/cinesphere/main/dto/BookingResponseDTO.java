package com.cinesphere.main.dto;

import java.util.List;

import com.cinesphere.main.entity.BookingStatus;

public class BookingResponseDTO {
	private Long bookingId;
	private Long showId;
	private Double totalAmount;
	private BookingStatus status;
	private List<String> seats;

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double double1) {
		this.totalAmount = double1;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public List<String> getSeats() {
		return seats;
	}

	public void setSeats(List<String> seats) {
		this.seats = seats;
	}

}
