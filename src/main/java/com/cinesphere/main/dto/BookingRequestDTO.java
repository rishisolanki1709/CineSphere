package com.cinesphere.main.dto;

import java.util.List;

public class BookingRequestDTO {
	private Long showId;
	private List<Long> showSeatIds;

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
