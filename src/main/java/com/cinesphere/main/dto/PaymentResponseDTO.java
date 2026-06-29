package com.cinesphere.main.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cinesphere.main.entity.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {

	private Long id;

	private BigDecimal amount;

	private PaymentStatus status;

	private String orderId;

	private String paymentId;

	private LocalDateTime createdAt;

	private Long bookingId;
}