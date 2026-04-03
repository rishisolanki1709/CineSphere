package com.cinesphere.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.ApiResponse;
import com.cinesphere.main.dto.PaymentVerificationDTO;
import com.cinesphere.main.entity.Payment;
import com.cinesphere.main.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping("/refund/{bookingId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<String>> refund(@PathVariable Long bookingId, Authentication authentication) {

		paymentService.refundBooking(bookingId, authentication.getName());
		return ResponseEntity.ok(new ApiResponse<>(true, "Refund Successful", null));
	}

	@PostMapping("/create-order/{bookingId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<String>> createOrder(@PathVariable Long bookingId, Authentication auth)
			throws Exception {

		String orderId = paymentService.createPaymentOrder(bookingId, auth.getName());

		return ResponseEntity.ok(new ApiResponse<>(true, "Order Created", orderId));
	}

	@PostMapping("success/{bookingId}?paymentId={paymentId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<String>> success(@PathVariable("paymentId") Long paymentId,
			@PathVariable("bookingId") Long bookingId) {

		paymentService.markPaymentSuccess(paymentId, bookingId);

		return ResponseEntity.ok(new ApiResponse<>(true, "Payment Success", null));
	}

	@PostMapping("/failed/{paymentId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<String>> failed(@PathVariable Long paymentId) {

		paymentService.markPaymentFailed(paymentId);

		return ResponseEntity.ok(new ApiResponse<>(true, "Payment failed", null));
	}

	@PostMapping("/verify/{bookingId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<String>> verifyPayment(@PathVariable Long bookingId,
			@RequestBody PaymentVerificationDTO verificationDTO) {

		paymentService.verifyAndConfirmPayment(bookingId, verificationDTO);

		return ResponseEntity.ok(new ApiResponse<>(true, "Payment Verified. Booking Confirmed!", null));
	}

	@GetMapping("all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<Payment>>> getAllPayments() {

		return ResponseEntity
				.ok(new ApiResponse<>(true, "Payment Fetched Successfully", paymentService.getAllPayments()));
	}

}
