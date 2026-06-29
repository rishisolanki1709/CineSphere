package com.cinesphere.main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TheatreRequestDTO {

	@NotBlank(message = "Theatre name is required")
	@Size(min = 3, max = 100)
	private String name;

	@NotBlank(message = "City is required")
	@Size(max = 50)
	private String city;

	@NotBlank(message = "Address is required")
	@Size(min = 10, max = 255)
	private String address;

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
}