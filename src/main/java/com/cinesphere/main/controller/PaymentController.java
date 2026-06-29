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

import com.cinesphere.main.dto.ApiResponseDTO;
import com.cinesphere.main.dto.PaymentResponseDTO;
import com.cinesphere.main.dto.PaymentVerificationDTO;
import com.cinesphere.main.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping("/refund/{bookingId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponseDTO<String>> refund(@PathVariable Long bookingId, Authentication authentication) {

		paymentService.refundBooking(bookingId, authentication.getName());
		return ResponseEntity.ok(new ApiResponseDTO<>(true, "Refund Successful", null));
	}

	@PostMapping("/create-order/{bookingId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponseDTO<String>> createOrder(@PathVariable Long bookingId, Authentication auth)
			throws Exception {

		String orderId = paymentService.createPaymentOrder(bookingId, auth.getName());

		return ResponseEntity.ok(new ApiResponseDTO<>(true, "Order Created", orderId));
	}

	@PostMapping("/failed/{paymentId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponseDTO<String>> failed(@PathVariable Long paymentId) {

		paymentService.markPaymentFailed(paymentId);

		return ResponseEntity.ok(new ApiResponseDTO<>(true, "Payment failed", null));
	}

	@PostMapping("/verify/{bookingId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponseDTO<String>> verifyPayment(@PathVariable Long bookingId,
			@Valid @RequestBody PaymentVerificationDTO verificationDTO) {

		paymentService.verifyAndConfirmPayment(bookingId, verificationDTO);

		return ResponseEntity.ok(new ApiResponseDTO<>(true, "Payment Verified. Booking Confirmed!", null));
	}

	@GetMapping("all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseDTO<List<PaymentResponseDTO>>> getAllPayments() {

		return ResponseEntity.ok(new ApiResponseDTO<List<PaymentResponseDTO>>(true, "Payment Fetched Successfully",
				paymentService.getAllPayments()));
	}

}
