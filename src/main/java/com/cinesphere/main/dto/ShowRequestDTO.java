package com.cinesphere.main.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ShowRequestDTO {
	private Long movieId;
	private Long screenId;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime startTime;

	// Pricing for the different levels we designed in the Layout
	private BigDecimal vipPrice;
	private BigDecimal premiumPrice;
	private BigDecimal regularPrice;

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public Long getScreenId() {
		return screenId;
	}

	public void setScreenId(Long screenId) {
		this.screenId = screenId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public BigDecimal getVipPrice() {
		return vipPrice;
	}

	public void setVipPrice(BigDecimal vipPrice) {
		this.vipPrice = vipPrice;
	}

	public BigDecimal getPremiumPrice() {
		return premiumPrice;
	}

	public void setPremiumPrice(BigDecimal premiumPrice) {
		this.premiumPrice = premiumPrice;
	}

	public BigDecimal getRegularPrice() {
		return regularPrice;
	}

	public void setRegularPrice(BigDecimal regularPrice) {
		this.regularPrice = regularPrice;
	}

}