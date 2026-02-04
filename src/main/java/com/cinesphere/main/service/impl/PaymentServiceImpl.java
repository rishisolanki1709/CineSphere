package com.cinesphere.main.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.MockPaymentRequestDTO;
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

	@Transactional
	public void processMockPayment(MockPaymentRequestDTO request) {

		Booking booking = bookingRepository.findById(request.getBookingId())
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		Payment payment = new Payment();
		payment.setBooking(booking);
		payment.setAmount(booking.getTotalAmount());
		payment.setPaymentMode("MOCK");
		payment.setCreatedAt(LocalDateTime.now());

		if (request.getSuccess()) {
			// ✅ Payment Success
			payment.setStatus(PaymentStatus.SUCCESS);
			booking.setStatus(BookingStatus.CONFIRMED);

			for (ShowSeat seat : booking.getShowSeats()) {
				seat.setStatus(SeatStatus.BOOKED);
				seat.setLockedAt(null);
			}

			showSeatRepository.saveAll(booking.getShowSeats());
		} else {
			payment.setStatus(PaymentStatus.FAILED);
			booking.setStatus(BookingStatus.CANCELLED);

			for (ShowSeat seat : booking.getShowSeats()) {
				seat.setStatus(SeatStatus.AVAILABLE);
				seat.setLockedAt(null);
				seat.setBooking(null);
			}

			showSeatRepository.saveAll(booking.getShowSeats());
		}

		paymentRepository.save(payment);
		bookingRepository.save(booking);
	}
}
