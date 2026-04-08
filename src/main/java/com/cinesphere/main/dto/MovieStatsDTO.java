package com.cinesphere.main.dto;

public class MovieStatsDTO {

	private String movieTitle;
	private Long ticketsSold;

	// Constructor required for JPA to automatically map the query results
	public MovieStatsDTO(String movieTitle, Long ticketsSold) {
		this.movieTitle = movieTitle;
		this.ticketsSold = ticketsSold;
	}

	// Getters and Setters
	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public Long getTicketsSold() {
		return ticketsSold;
	}

	public void setTicketsSold(Long ticketsSold) {
		this.ticketsSold = ticketsSold;
	}
}