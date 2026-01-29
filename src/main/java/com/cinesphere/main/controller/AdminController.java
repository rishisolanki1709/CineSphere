package com.cinesphere.main.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.UserRegisterRequest;
import com.cinesphere.main.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final UserService userService;

	public AdminController(UserService userService) {
		this.userService = userService;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/dashboard")
	public String adminDashboard() {
		return "Admin Dashboard";
	}

	@PostMapping("/create-admin")
	public String createAdmin(@RequestBody UserRegisterRequest request) {
		userService.createAdmin(request);
		return "Admin created successfully";
	}
}
