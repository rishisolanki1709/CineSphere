package com.cinesphere.main.dto;

import java.util.List;

import org.springframework.security.core.Authentication;

public class LockSeatDTO {

	private Long showId;
	private List<Long> showSeatIds;
	private Authentication authentication;

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

	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
}
