package com.cinesphere.main.controller;

import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.AdminBookingSummaryDTO;
import com.cinesphere.main.dto.AdminDashboardResponseDTO;
import com.cinesphere.main.dto.AdminPaymentResponseDTO;
import com.cinesphere.main.dto.ApiResponseDTO;
import com.cinesphere.main.dto.UserRegisterRequestDTO;
import com.cinesphere.main.dto.UserResponseDTO;
import com.cinesphere.main.service.AdminService;
import com.cinesphere.main.service.BookingService;
import com.cinesphere.main.service.PaymentService;
import com.cinesphere.main.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final BookingService bookingService;
	private final UserService userService;
	private final AdminService adminService;
	private final PaymentService paymentService;

	public AdminController(UserService userService, AdminService adminService, BookingService bookingService,
			PaymentService paymentService) {
		this.userService = userService;
		this.adminService = adminService;
		this.bookingService = bookingService;
		this.paymentService = paymentService;
	}

	@PostMapping("/create-admin")
	public String createAdmin(@Valid @RequestBody UserRegisterRequestDTO request) {
		userService.createAdmin(request);
		return "Admin Created Successfully";
	}

	@GetMapping("/dashboard")
	public ResponseEntity<ApiResponseDTO<AdminDashboardResponseDTO>> dashboard(
			@RequestParam(defaultValue = "overall") String range) {
		return ResponseEntity.ok(new ApiResponseDTO<AdminDashboardResponseDTO>(true, "Details Fetched Successfully",
				adminService.getDashboard(range)));
	}

	@GetMapping("users")
	public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getAllUserDetails() {
		return ResponseEntity.ok(new ApiResponseDTO<List<UserResponseDTO>>(true, "User Details Fetched Successfully",
				adminService.getAllUsers()));
	}

	@GetMapping("/bookings")
	public ResponseEntity<ApiResponseDTO<Page<AdminBookingSummaryDTO>>> getAllBookings(Pageable pageable) {
		// Returns bookings sorted by newest first by default if configured
		return ResponseEntity.ok(new ApiResponseDTO<Page<AdminBookingSummaryDTO>>(true, "Bookings Fetched Successfully",
				bookingService.findAll(pageable)));
	}

	@GetMapping("/payments")
	public ResponseEntity<ApiResponseDTO<Page<AdminPaymentResponseDTO>>> getAllPayments(Pageable pageable) {
		return ResponseEntity.ok(new ApiResponseDTO<Page<AdminPaymentResponseDTO>>(true,
				"Payments Fetched Successfully", paymentService.findAll(pageable)));
	}

}
