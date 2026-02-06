package com.cinesphere.main.service;

import com.cinesphere.main.dto.MockPaymentRequestDTO;

public interface PaymentService {
	public void processMockPayment(MockPaymentRequestDTO request);

	public void refundBooking(Long bookingId, String name);
}
