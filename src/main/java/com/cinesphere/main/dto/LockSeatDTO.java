package com.cinesphere.main.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class LockSeatDTO {

	@NotNull(message = "Show ID is required")
	@Positive(message = "Show ID must be positive")
	private Long showId;

	@NotEmpty(message = "Please select at least one seat")
	private List<@Positive(message = "Seat ID must be positive") Long> showSeatIds;

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
