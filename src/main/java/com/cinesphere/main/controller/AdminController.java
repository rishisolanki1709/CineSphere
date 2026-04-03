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
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.AdminBookingSummaryDTO;
import com.cinesphere.main.dto.AdminDashboardDTO;
import com.cinesphere.main.dto.AdminPaymentResponseDTO;
import com.cinesphere.main.dto.ApiResponse;
import com.cinesphere.main.dto.UserRegisterRequest;
import com.cinesphere.main.entity.User;
import com.cinesphere.main.service.AdminService;
import com.cinesphere.main.service.BookingService;
import com.cinesphere.main.service.PaymentService;
import com.cinesphere.main.service.UserService;

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
	public String createAdmin(@RequestBody UserRegisterRequest request) {
		userService.createAdmin(request);
		return "Admin Created Successfully";
	}

	@GetMapping("/dashboard")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<AdminDashboardDTO>> dashboard() {
		return ResponseEntity.ok(
				new ApiResponse<AdminDashboardDTO>(true, "Details Fetched Successfully", adminService.getDashboard()));
	}

	@GetMapping("users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<User>>> getAllUserDetails() {
		return ResponseEntity
				.ok(new ApiResponse<List<User>>(true, "User Details Fetched Successfully", adminService.getAllUsers()));
	}

	@GetMapping("/bookings")
	public ResponseEntity<ApiResponse<Page<AdminBookingSummaryDTO>>> getAllBookings(Pageable pageable) {
		// Returns bookings sorted by newest first by default if configured
		return ResponseEntity.ok(new ApiResponse<Page<AdminBookingSummaryDTO>>(true, "Bookings Fetched Successfully",
				bookingService.findAll(pageable)));
	}

	@GetMapping("/payments")
	public ResponseEntity<ApiResponse<Page<AdminPaymentResponseDTO>>> getAllPayments(Pageable pageable) {
		return ResponseEntity.ok(new ApiResponse<Page<AdminPaymentResponseDTO>>(true, "Payments Fetched Successfully",
				paymentService.findAll(pageable)));
	}

}
