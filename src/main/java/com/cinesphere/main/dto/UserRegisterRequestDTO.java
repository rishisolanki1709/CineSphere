package com.cinesphere.main.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// this is just a DTO class
public class UserRegisterRequestDTO {

	@NotBlank(message = "Name is required")
	@Size(min = 3, max = 50)
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Please enter a valid email")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 50)
	private String password;

	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Please enter a valid 10-digit Indian phone number")
	private String phone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
