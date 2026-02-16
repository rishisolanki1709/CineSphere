package com.cinesphere.main.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.AdminDashboardDTO;
import com.cinesphere.main.dto.ApiResponse;
import com.cinesphere.main.dto.UserRegisterRequest;
import com.cinesphere.main.service.AdminService;
import com.cinesphere.main.service.UserService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final UserService userService;
	private final AdminService adminService;

	public AdminController(UserService userService, AdminService adminService) {
		this.userService = userService;
		this.adminService = adminService;
	}

	@PostMapping("/create-admin")
	public String createAdmin(@RequestBody UserRegisterRequest request) {
		userService.createAdmin(request);
		return "Admin Created Successfully";
	}

	@GetMapping("/dashboard")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<AdminDashboardDTO>> dashboard() {
		return ResponseEntity.ok(new ApiResponse(true, "Details Fetched Successfully", adminService.getDashboard()));
//		return new ApiResponse(ResponseEntity.ok(adminService.getDashboard()));
	}

}
