package com.cinesphere.main.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShowResponseDTO {

	private Long showId;
	private String movieTitle;
	private String screenName;
	private String theatreName;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private BigDecimal VIP_price;
	private BigDecimal PREMIUM_price;
	private BigDecimal REGULAR_price;

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getVIP_price() {
		return VIP_price;
	}

	public void setVIP_price(BigDecimal vIP_price) {
		VIP_price = vIP_price;
	}

	public BigDecimal getPREMIUM_price() {
		return PREMIUM_price;
	}

	public void setPREMIUM_price(BigDecimal pREMIUM_price) {
		PREMIUM_price = pREMIUM_price;
	}

	public BigDecimal getREGULAR_price() {
		return REGULAR_price;
	}

	public void setREGULAR_price(BigDecimal rEGULAR_price) {
		REGULAR_price = rEGULAR_price;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}
}
