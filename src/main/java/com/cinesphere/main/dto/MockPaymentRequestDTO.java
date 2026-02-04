package com.cinesphere.main.dto;

public class MockPaymentRequestDTO {
	private Long bookingId;
	private Boolean success;

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
}