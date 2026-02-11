package com.cinesphere.main.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final BookingRepository bookingRepository;
	private final PaymentRepository paymentRepository;
	private final ShowSeatRepository showSeatRepository;

	public PaymentServiceImpl(BookingRepository bookingRepository, PaymentRepository paymentRepository,
			ShowSeatRepository showSeatRepository) {
		this.bookingRepository = bookingRepository;
		this.paymentRepository = paymentRepository;
		this.showSeatRepository = showSeatRepository;
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

		Payment payment = new Payment();
		payment.setBooking(booking);
		payment.setAmount(booking.getTotalAmount());
		payment.setStatus(PaymentStatus.PENDING);
		payment.setOrderId("ORDER_" + System.currentTimeMillis());
		payment.setCreatedAt(LocalDateTime.now());

		Payment saved = paymentRepository.save(payment);

		return saved.getOrderId();
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
			throw new RuntimeException("Refund not allowed");
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
	public void markPaymentSuccess(Long paymentId) {

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new RuntimeException("Payment not found"));

		if (payment.getStatus() == PaymentStatus.SUCCESS)
			return;

		payment.setStatus(PaymentStatus.SUCCESS);
		payment.setPaymentId("PAY_" + System.currentTimeMillis());

		Booking booking = payment.getBooking();
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

		Payment payment = paymentRepository.findById(paymentId).orElseThrow();

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

}
