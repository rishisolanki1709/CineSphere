package com.cinesphere.main.dto;

public class AdminDashboardDTO {

	private double totalRevenue;
	private long totalBookings;
	private long activeShows;
	private long seatsBookedToday;
	private double occupancyRate;

	public double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public long getTotalBookings() {
		return totalBookings;
	}

	public void setTotalBookings(long totalBookings) {
		this.totalBookings = totalBookings;
	}

	public long getActiveShows() {
		return activeShows;
	}

	public void setActiveShows(long activeShows) {
		this.activeShows = activeShows;
	}

	public long getSeatsBookedToday() {
		return seatsBookedToday;
	}

	public void setSeatsBookedToday(long seatsBookedToday) {
		this.seatsBookedToday = seatsBookedToday;
	}

	public double getOccupancyRate() {
		return occupancyRate;
	}

	public void setOccupancyRate(double occupancyRate) {
		this.occupancyRate = occupancyRate;
	}
}
