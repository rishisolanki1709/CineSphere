package com.cinesphere.main.service;

public interface PaymentService {
	public void refundBooking(Long bookingId, String name);

	String createPaymentOrder(Long bookingId, String string) throws Exception;

	void markPaymentSuccess(Long orderId);

	void markPaymentFailed(Long paymentId);
}
