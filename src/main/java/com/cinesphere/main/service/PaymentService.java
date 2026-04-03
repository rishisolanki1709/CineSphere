package com.cinesphere.main.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cinesphere.main.dto.AdminPaymentResponseDTO;
import com.cinesphere.main.dto.PaymentVerificationDTO;
import com.cinesphere.main.entity.Payment;

public interface PaymentService {
	public void refundBooking(Long bookingId, String name);

	String createPaymentOrder(Long bookingId, String string) throws Exception;

	void markPaymentSuccess(Long orderId, Long bookingId);

	void markPaymentFailed(Long paymentId);

	public void verifyAndConfirmPayment(Long bookingId, PaymentVerificationDTO verificationDTO);

	public List<Payment> getAllPayments();

	Page<AdminPaymentResponseDTO> findAll(Pageable pageable);

//	public void verifyPayment(Long bookingId);
}
