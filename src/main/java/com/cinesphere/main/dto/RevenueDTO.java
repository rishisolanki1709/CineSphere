package com.cinesphere.main.dto;

public class RevenueDTO {
	private String date;
	private Double totalRevenue;

	public RevenueDTO(String date, Double totalRevenue) {
		super();
		this.date = date;
		this.totalRevenue = totalRevenue;
	}


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(Double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
}