package com.cinesphere.main.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.BookingResponseDTO;
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

	@Override
	@Transactional
	public BookingResponseDTO confirmBooking(Long showId, List<Long> showSeatIds, String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

		Show show = showRepository.findById(showId).orElseThrow(() -> new RuntimeException("Show Not Found"));

		List<ShowSeat> seats = showSeatRepository.findAllById(showSeatIds);

		for (ShowSeat seat : seats) {
			if (seat.getStatus() != SeatStatus.LOCKED) {
				throw new RuntimeException("Seat Not Locked");
			}

			if (seat.getLockedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
				throw new RuntimeException("Seat Lock Expired");
			}
		}

		Booking booking = new Booking();
		booking.setUser(user);
		booking.setShow(show);
		booking.setStatus(BookingStatus.PENDING_PAYMENT);
		booking.setTotalAmount(seats.size() * show.getPrice());
		booking.setBookedAt(LocalDateTime.now());

		Booking savedBooking = bookingRepository.save(booking);

		for (ShowSeat seat : seats) {
			seat.setBooking(savedBooking);
		}

		showSeatRepository.saveAll(seats);

		BookingResponseDTO dto = new BookingResponseDTO();
		dto.setBookingId(savedBooking.getId());
		dto.setStatus(savedBooking.getStatus());
		dto.setTotalAmount(savedBooking.getTotalAmount());

		return dto;
	}

	@Override
	@Transactional
	public void cancelBooking(Long bookingId, String email) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking Not Found"));

		if (!booking.getUser().getEmail().equals(email)) {
			throw new RuntimeException("Unauthorized Cancellation");
		}

		if (booking.getStatus() == BookingStatus.CANCELLED) {
			throw new RuntimeException("Booking Already Cancelled");
		}

		if (booking.getStatus() != BookingStatus.CONFIRMED) {
			throw new RuntimeException("Booking Cannot Be Cancelled");
		}

		List<ShowSeat> seats = showSeatRepository.findByBookingId(bookingId);

		for (ShowSeat seat : seats) {
			if (seat.getStatus() != SeatStatus.BOOKED) {
				throw new RuntimeException("Seat State Invalid For Cancellation");
			}
			seat.setStatus(SeatStatus.AVAILABLE);
			seat.setBooking(null);
			seat.setLockedAt(null);
		}

		showSeatRepository.saveAll(seats);

		booking.setStatus(BookingStatus.CANCELLED);
		bookingRepository.save(booking);
	}
}
