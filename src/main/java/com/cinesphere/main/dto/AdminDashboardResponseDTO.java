package com.cinesphere.main.dto;

import java.util.List;

public class AdminDashboardResponseDTO {

	private List<RevenueDTO> revenueData; // Line Chart
	private List<MovieStatsDTO> movieData; // Bar Chart
	private List<StatusCountDTO> statusData; // Pie Chart

	public List<RevenueDTO> getRevenueData() {
		return revenueData;
	}

	public void setRevenueData(List<RevenueDTO> revenueData) {
		this.revenueData = revenueData;
	}

	public List<MovieStatsDTO> getMovieData() {
		return movieData;
	}

	public void setMovieData(List<MovieStatsDTO> movieData) {
		this.movieData = movieData;
	}

	public List<StatusCountDTO> getStatusData() {
		return statusData;
	}

	public void setStatusData(List<StatusCountDTO> statusData) {
		this.statusData = statusData;
	}

}
