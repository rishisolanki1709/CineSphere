package com.cinesphere.main.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequestDTO {
	@Positive(message = "Show ID must be a positive number")
	@NotNull(message = "Show ID is required")
	private Long showId;

	@NotEmpty(message = "At least one seat must be selected")
	private List<@Positive(message = "Seat ID must be a positive number") Long> showSeatIds;

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public List<Long> getShowSeatIds() {
		return showSeatIds;
	}

	public void setShowSeatIds(List<Long> showSeatIds) {
		this.showSeatIds = showSeatIds;
	}
}
