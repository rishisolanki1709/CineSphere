package com.cinesphere.main.dto;

public class TheatreResponseDTO {
	private Long id;
	private String name;
	private String city;
	private String address;
	private boolean active;
	private int screens = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getScreens() {
		return screens;
	}

	public void setScreens(int i) {
		this.screens = i;
	}
}
