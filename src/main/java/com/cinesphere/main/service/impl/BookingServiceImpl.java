package com.cinesphere.main.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.entity.Booking;
import com.cinesphere.main.entity.BookingStatus;
import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.Show;
import com.cinesphere.main.entity.ShowSeat;
import com.cinesphere.main.entity.User;
import com.cinesphere.main.repository.BookingRepository;
import com.cinesphere.main.repository.ShowRepository;
import com.cinesphere.main.repository.ShowSeatRepository;
import com.cinesphere.main.repository.UserRepository;
import com.cinesphere.main.service.BookinService;

import jakarta.transaction.Transactional;

@Service
public class BookingServiceImpl implements BookinService {

	private final UserRepository userRepository;
	private final ShowRepository showRepository;
	private final ShowSeatRepository showSeatRepository;
	private final BookingRepository bookingRepository;

	public BookingServiceImpl(UserRepository userRepository, ShowRepository showRepository,
			ShowSeatRepository showSeatRepository, BookingRepository bookingRepository) {
		this.userRepository = userRepository;
		this.showRepository = showRepository;
		this.showSeatRepository = showSeatRepository;
		this.bookingRepository = bookingRepository;
	}

	@Transactional
	public void confirmBooking(Long showId, List<Long> showSeatIds, String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		Show show = showRepository.findById(showId).orElseThrow(() -> new RuntimeException("Show not found"));

		List<ShowSeat> seats = showSeatRepository.findAllById(showSeatIds);

		// 1️⃣ Validate lock
		for (ShowSeat seat : seats) {
			if (seat.getStatus() != SeatStatus.LOCKED) {
				throw new RuntimeException("Seat not locked");
			}

			if (seat.getLockedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
				throw new RuntimeException("Seat lock expired");
			}
		}

		// 2️⃣ Create booking
		Booking booking = new Booking();
		booking.setUser(user);
		booking.setShow(show);
		booking.setStatus(BookingStatus.CONFIRMED);
		booking.setTotalAmount(seats.size() * show.getPrice());
		booking.setBookedAt(LocalDateTime.now());

		Booking savedBooking = bookingRepository.save(booking);

		// 3️⃣ Mark seats as BOOKED
		for (ShowSeat seat : seats) {
			seat.setStatus(SeatStatus.BOOKED);
			seat.setBooking(savedBooking);
			seat.setLockedAt(null);
		}

		showSeatRepository.saveAll(seats);

		savedBooking.setShowSeats(seats);
	}

	@Transactional
	public void cancelBooking(Long bookingId, String email) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		// 1️⃣ Check ownership
		if (!booking.getUser().getEmail().equals(email)) {
			throw new RuntimeException("Unauthorized cancellation");
		}

		// 2️⃣ Check status
		if (booking.getStatus() != BookingStatus.CONFIRMED) {
			throw new RuntimeException("Booking cannot be cancelled");
		}

		// 3️⃣ Release seats
		List<ShowSeat> seats = booking.getShowSeats();

		for (ShowSeat seat : seats) {
			seat.setStatus(SeatStatus.AVAILABLE);
			seat.setBooking(null);
			seat.setLockedAt(null);
		}

		showSeatRepository.saveAll(seats);

		// 4️⃣ Update booking
		booking.setStatus(BookingStatus.CANCELLED);
		bookingRepository.save(booking);
	}

}
