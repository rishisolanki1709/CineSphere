package com.cinesphere.main.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.ApiResponse;
import com.cinesphere.main.dto.MockPaymentRequestDTO;
import com.cinesphere.main.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping("/mock")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<String>> mockPayment(@RequestBody MockPaymentRequestDTO request) {

		paymentService.processMockPayment(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Mock payment processed", null));
	}
}
