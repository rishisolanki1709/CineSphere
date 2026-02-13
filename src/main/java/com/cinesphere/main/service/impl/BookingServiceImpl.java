package com.cinesphere.main.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.BookingResponseDTO;
import com.cinesphere.main.dto.TicketResponseDTO;
import com.cinesphere.main.entity.Booking;
import com.cinesphere.main.entity.BookingStatus;
import com.cinesphere.main.entity.Payment;
import com.cinesphere.main.entity.PaymentStatus;
import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.Show;
import com.cinesphere.main.entity.ShowSeat;
import com.cinesphere.main.entity.User;
import com.cinesphere.main.repository.BookingRepository;
import com.cinesphere.main.repository.PaymentRepository;
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
	private final PaymentRepository paymentRepository;

	public BookingServiceImpl(UserRepository userRepository, ShowRepository showRepository,
			ShowSeatRepository showSeatRepository, BookingRepository bookingRepository,
			PaymentRepository paymentRepository) {
		this.userRepository = userRepository;
		this.showRepository = showRepository;
		this.showSeatRepository = showSeatRepository;
		this.bookingRepository = bookingRepository;
		this.paymentRepository = paymentRepository;
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

		if (booking.getShow().getStartTime().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Show Already Started");
		}

		double refund = calculateRefund(booking);

		List<ShowSeat> seats = showSeatRepository.findByBookingId(bookingId);
		for (ShowSeat seat : seats) {
			seat.setStatus(SeatStatus.AVAILABLE);
			seat.setBooking(null);
			seat.setLockedAt(null);
		}
		showSeatRepository.saveAll(seats);

		Payment payment = paymentRepository.findByBookingId(bookingId);
		if (payment != null) {
			payment.setStatus(PaymentStatus.REFUNDED);
			paymentRepository.save(payment);
		}

		booking.setRefundAmount(refund);
		booking.setStatus(BookingStatus.CANCELLED);

		bookingRepository.save(booking);
	}

	@Override
	public List<BookingResponseDTO> getMyBookings(String email) {
		List<Booking> bookings = bookingRepository.findByUserEmailOrderByBookedAtDesc(email);

		return bookings.stream().map(this::mapToDTO).toList();
	}

	@Override
	public BookingResponseDTO getBookingDetails(Long bookingId, String email) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		if (!booking.getUser().getEmail().equals(email)) {
			throw new RuntimeException("Unauthorized");
		}

		return mapToDTO(booking);
	}

	private BookingResponseDTO mapToDTO(Booking booking) {

		BookingResponseDTO dto = new BookingResponseDTO();

		dto.setBookingId(booking.getId());
		dto.setShowId(booking.getShow().getId());
		dto.setStatus(booking.getStatus());
		dto.setTotalAmount(booking.getTotalAmount());
		dto.setBookedAt(booking.getBookedAt());

		dto.setSeats(booking.getShowSeats().stream().map(ss -> ss.getSeat().getSeatRow() + ss.getSeat().getSeatNumber())
				.toList());

		dto.setMovieName(booking.getShow().getMovie().getTitle());
		dto.setTheatreName(booking.getShow().getScreen().getTheatre().getName());
		dto.setScreenName(booking.getShow().getScreen().getScreenName());
		dto.setShowTime(booking.getShow().getStartTime());

		return dto;
	}

	@Override
	public TicketResponseDTO getTicket(Long bookingId, String email) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		if (booking.getStatus().equals(BookingStatus.CANCELLED) || booking.getStatus().equals(BookingStatus.REFUNDED)) {
			throw new RuntimeException("Booking was CANCELLED");
		}

		if (!booking.getUser().getEmail().equals(email)) {
			throw new RuntimeException("Unauthorized access");
		}

		return mapToTicketDTO(booking);
	}

	private TicketResponseDTO mapToTicketDTO(Booking booking) {

		TicketResponseDTO dto = new TicketResponseDTO();

		dto.setBookingId(booking.getId());
		dto.setMovieName(booking.getShow().getMovie().getTitle());
		dto.setTheatreName(booking.getShow().getScreen().getTheatre().getName());
		dto.setScreenName(booking.getShow().getScreen().getScreenName());
		dto.setShowTime(booking.getShow().getStartTime());

		dto.setSeats(booking.getShowSeats().stream().map(ss -> ss.getSeat().getSeatRow() + ss.getSeat().getSeatNumber())
				.toList());

		dto.setAmount(booking.getTotalAmount());
		dto.setStatus(booking.getStatus());
		dto.setBookedAt(booking.getBookedAt());

		return dto;
	}

	private double calculateRefund(Booking booking) {

		LocalDateTime showTime = booking.getShow().getStartTime();
		long hours = Duration.between(LocalDateTime.now(), showTime).toHours();

		if (hours > 24)
			return booking.getTotalAmount();
		if (hours > 3)
			return booking.getTotalAmount() * 0.5;
		return 0;
	}

}
