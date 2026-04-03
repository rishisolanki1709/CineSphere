package com.cinesphere.main.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cinesphere.main.entity.Booking;
import com.cinesphere.main.entity.BookingStatus;
import com.cinesphere.main.entity.Payment;
import com.cinesphere.main.entity.PaymentStatus;
import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.ShowSeat;
import com.cinesphere.main.repository.BookingRepository;
import com.cinesphere.main.repository.PaymentRepository;
import com.cinesphere.main.repository.ShowSeatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookingCleanupScheduler {

	private final BookingRepository bookingRepository;
	private final ShowSeatRepository showSeatRepository;
	private final PaymentRepository paymentRepository;

	public BookingCleanupScheduler(BookingRepository bookingRepository, ShowSeatRepository showSeatRepository,
			PaymentRepository paymentRepository) {
		this.bookingRepository = bookingRepository;
		this.showSeatRepository = showSeatRepository;
		this.paymentRepository = paymentRepository;
	}

	/**
	 * Runs every 60 seconds (60000ms). Finds PENDING_PAYMENT bookings older than 10
	 * minutes and cancels them.
	 */
	@Scheduled(fixedRate = 60000)
	@Transactional
	public void cancelExpiredBookings() {
		// Define the expiry threshold (e.g., 10 minutes ago)
		LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);

		// 1. Fetch bookings that are still pending and created before the threshold
		List<Booking> expiredBookings = bookingRepository
				.findAllByStatusAndBookedAtBefore(BookingStatus.PENDING_PAYMENT, threshold);

		if (expiredBookings.isEmpty()) {
			return;
		}

		for (Booking booking : expiredBookings) {
			// 2. Mark Booking as CANCELLED
			booking.setStatus(BookingStatus.CANCELLED);

			// 3. Release the associated seats
			// We fetch seats by booking to ensure we clear the specific hold
			List<ShowSeat> seats = showSeatRepository.findByBookingId(booking.getId());
			for (ShowSeat seat : seats) {
				seat.setStatus(SeatStatus.AVAILABLE);
				seat.setBooking(null);
				seat.setLockedAt(null);
			}

			showSeatRepository.saveAll(seats);
		}

		bookingRepository.saveAll(expiredBookings);
	}

	@Scheduled(fixedRate = 60000) // Runs every minute
	@Transactional
	public void cleanupExpiredPayments() {
		// Threshold: 15 minutes (Giving 5 mins extra beyond the seat lock)
		LocalDateTime threshold = LocalDateTime.now().minusMinutes(15);

		List<Payment> expiredPayments = paymentRepository.findAllByStatusAndCreatedAtBefore(PaymentStatus.PENDING,
				threshold);

		if (expiredPayments.isEmpty())
			return;

		for (Payment payment : expiredPayments) {
			// 1. Mark Payment as FAILED
			payment.setStatus(PaymentStatus.FAILED);

			// 2. Mark associated Booking as CANCELLED (if not already)
			Booking booking = payment.getBooking();
			if (booking != null && booking.getStatus() == BookingStatus.PENDING_PAYMENT) {
				booking.setStatus(BookingStatus.CANCELLED);

				// 3. Release Seats
				List<ShowSeat> seats = showSeatRepository.findByBookingId(booking.getId());
				for (ShowSeat seat : seats) {
					seat.setStatus(SeatStatus.AVAILABLE);
					seat.setBooking(null);
					seat.setLockedAt(null);
				}
				showSeatRepository.saveAll(seats);
				bookingRepository.save(booking);
			}
		}
		paymentRepository.saveAll(expiredPayments);
		System.out.println("Scheduler: Cleaned up " + expiredPayments.size() + " abandoned payments.");
	}
}