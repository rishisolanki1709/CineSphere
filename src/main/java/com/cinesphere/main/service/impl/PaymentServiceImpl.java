package com.cinesphere.main.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.AdminPaymentResponseDTO;
import com.cinesphere.main.dto.PaymentVerificationDTO;
import com.cinesphere.main.entity.Booking;
import com.cinesphere.main.entity.BookingStatus;
import com.cinesphere.main.entity.Payment;
import com.cinesphere.main.entity.PaymentStatus;
import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.ShowSeat;
import com.cinesphere.main.repository.BookingRepository;
import com.cinesphere.main.repository.PaymentRepository;
import com.cinesphere.main.repository.ShowSeatRepository;
import com.cinesphere.main.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final BookingRepository bookingRepository;
	private final PaymentRepository paymentRepository;
	private final ShowSeatRepository showSeatRepository;
	private final RazorpayClient razorpayClient;
	@Value("${razorpay.key.secret}")
	private String keySecret;

	public PaymentServiceImpl(BookingRepository bookingRepository, PaymentRepository paymentRepository,
			ShowSeatRepository showSeatRepository, RazorpayClient razorpayClient) {
		this.bookingRepository = bookingRepository;
		this.paymentRepository = paymentRepository;
		this.showSeatRepository = showSeatRepository;
		this.razorpayClient = razorpayClient;
	}

	@Override
	public String createPaymentOrder(Long bookingId, String email) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		if (!booking.getUser().getEmail().equals(email)) {
			throw new RuntimeException("Unauthorized");
		}
		if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
			throw new RuntimeException("Booking not eligible for payment");
		}

		Payment existingPayment = paymentRepository.findByBookingId(bookingId);

		Payment payment;
		if (existingPayment != null) {
			payment = existingPayment;
			// If the previous payment was already successful, don't allow re-payment
			if (payment.getStatus() == PaymentStatus.SUCCESS) {
				throw new RuntimeException("This booking is already paid!");
			}
			// Reuse the existing record
		} else {
			// Create a new record only if it doesn't exist
			payment = new Payment();
			payment.setBooking(booking);
			payment.setAmount(booking.getTotalAmount());
		}

		try {
			JSONObject orderRequest = new JSONObject();
			// Razorpay expects amount in Integer Paise
			orderRequest.put("amount", booking.getTotalAmount().intValue() * 100);
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "txn_" + bookingId);

			// Create the order on Razorpay servers
			Order razorpayOrder = razorpayClient.orders.create(orderRequest);
			String razorpayOrderId = razorpayOrder.get("id");

			payment.setBooking(booking);
			payment.setAmount(booking.getTotalAmount());
			payment.setStatus(PaymentStatus.PENDING);
			payment.setOrderId(razorpayOrderId); // Store the ACTUAL Razorpay ID
			payment.setCreatedAt(LocalDateTime.now());

			paymentRepository.save(payment);
			return razorpayOrderId;
		} catch (RazorpayException e) {
			throw new RuntimeException("Razorpay Order Creation Failed: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public void refundBooking(Long bookingId, String email) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		if (!booking.getUser().getEmail().equals(email)) {
			throw new RuntimeException("Unauthorized");
		}

		if (booking.getStatus() != BookingStatus.CONFIRMED) {
			throw new RuntimeException("Refund Not Allowed");
		}

		// Release seats
		for (ShowSeat seat : booking.getShowSeats()) {
			seat.setStatus(SeatStatus.AVAILABLE);
			seat.setBooking(null);
		}

		showSeatRepository.saveAll(booking.getShowSeats());

		booking.setStatus(BookingStatus.REFUNDED);
		bookingRepository.save(booking);
	}

	@Override
	@Transactional
	public void markPaymentSuccess(Long paymentId, Long bookingId) {

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new RuntimeException("Payment Not Found"));

		if (payment.getStatus() == PaymentStatus.SUCCESS)
			return;

		payment.setStatus(PaymentStatus.SUCCESS);
		payment.setPaymentId("PAY_" + System.currentTimeMillis());

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking Not Found"));
		booking.setStatus(BookingStatus.CONFIRMED);

		// mark seats BOOKED
		List<ShowSeat> seats = showSeatRepository.findByBookingId(booking.getId());
		for (ShowSeat seat : seats) {
			seat.setStatus(SeatStatus.BOOKED);
			seat.setLockedAt(null);
		}

		showSeatRepository.saveAll(seats);
		bookingRepository.save(booking);
		paymentRepository.save(payment);
	}

	@Override
	@Transactional
	public void markPaymentFailed(Long paymentId) {

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new RuntimeException("Payment Not Found"));

		payment.setStatus(PaymentStatus.FAILED);

		Booking booking = payment.getBooking();
		booking.setStatus(BookingStatus.CANCELLED);

		// release seats
		List<ShowSeat> seats = showSeatRepository.findByBookingId(booking.getId());
		for (ShowSeat seat : seats) {
			seat.setStatus(SeatStatus.AVAILABLE);
			seat.setBooking(null);
			seat.setLockedAt(null);
		}

		showSeatRepository.saveAll(seats);
		bookingRepository.save(booking);
		paymentRepository.save(payment);
	}

	@Override
	@Transactional
	public void verifyAndConfirmPayment(Long bookingId, PaymentVerificationDTO dto) {
		// 1. Fetch the booking
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		// 2. Security Check: Verify the Signature
		try {
			JSONObject options = new JSONObject();
			options.put("razorpay_order_id", dto.getRazorpayOrderId());
			options.put("razorpay_payment_id", dto.getRazorpayPaymentId());
			options.put("razorpay_signature", dto.getRazorpaySignature());

			// 'keySecret' is your rzp_test_... secret from application.properties
			boolean isValid = Utils.verifyPaymentSignature(options, keySecret);

			if (!isValid) {
				throw new RuntimeException("Invalid Payment Signature - Possible Fraud Attempt");
			}
		} catch (RazorpayException e) {
			throw new RuntimeException("Verification failed: " + e.getMessage());
		}

		// 3. Logic: Update Statuses
		booking.setStatus(BookingStatus.CONFIRMED);

		// Update the Payment record associated with this booking
		Payment payment = paymentRepository.findByOrderId(dto.getRazorpayOrderId()).get(0);

		payment.setPaymentId(dto.getRazorpayPaymentId());
		payment.setStatus(PaymentStatus.SUCCESS);

		// 4. Logic: Mark Seats as BOOKED
		for (ShowSeat seat : booking.getShowSeats()) {
			seat.setStatus(SeatStatus.BOOKED);
			seat.setLockedAt(null); // Clear the lock timer
		}

		// Save everything (Transaction will roll back if any save fails)
		bookingRepository.save(booking);
		paymentRepository.save(payment);
		showSeatRepository.saveAll(booking.getShowSeats());
	}

	@Override
	public List<Payment> getAllPayments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<AdminPaymentResponseDTO> findAll(Pageable pageable) {
		return paymentRepository.findAll(pageable).map(AdminPaymentResponseDTO::new);
	}

}
