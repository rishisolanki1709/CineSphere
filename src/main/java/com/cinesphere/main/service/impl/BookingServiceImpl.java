package com.cinesphere.main.service.impl;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.AdminBookingSummaryDTO;
import com.cinesphere.main.dto.BookingResponseDTO;
import com.cinesphere.main.entity.Booking;
import com.cinesphere.main.entity.BookingStatus;
import com.cinesphere.main.entity.Payment;
import com.cinesphere.main.entity.PaymentStatus;
import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.Show;
import com.cinesphere.main.entity.ShowSeat;
import com.cinesphere.main.entity.ShowStatus;
import com.cinesphere.main.entity.User;
import com.cinesphere.main.repository.BookingRepository;
import com.cinesphere.main.repository.PaymentRepository;
import com.cinesphere.main.repository.ShowRepository;
import com.cinesphere.main.repository.ShowSeatRepository;
import com.cinesphere.main.repository.UserRepository;
import com.cinesphere.main.service.BookingService;

import jakarta.transaction.Transactional;

@Service
public class BookingServiceImpl implements BookingService {

	private final UserRepository userRepository;
	private final ShowRepository showRepository;
	private final ShowSeatRepository showSeatRepository;
	private final BookingRepository bookingRepository;
	private final PaymentRepository paymentRepository;
	private final RefundService refundService;

	public BookingServiceImpl(UserRepository userRepository, ShowRepository showRepository,
			ShowSeatRepository showSeatRepository, BookingRepository bookingRepository,
			PaymentRepository paymentRepository, RefundService refundService) {
		this.userRepository = userRepository;
		this.showRepository = showRepository;
		this.showSeatRepository = showSeatRepository;
		this.bookingRepository = bookingRepository;
		this.paymentRepository = paymentRepository;
		this.refundService = refundService;
	}

	@Override
	@Transactional
	public BookingResponseDTO confirmBooking(Long showId, List<Long> showSeatIds, String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

		Show show = showRepository.findById(showId).orElseThrow(() -> new RuntimeException("Show Not Found"));

		if (show.getStatus() != ShowStatus.ACTIVE) {
			throw new RuntimeException("Show Not Active");
		}

		List<ShowSeat> seats = showSeatRepository.findAllById(showSeatIds);

		BigDecimal totalAmount = new BigDecimal(0);
		for (ShowSeat seat : seats) {
			totalAmount = totalAmount.add(seat.getPrice());
			if (seat.getStatus() != SeatStatus.LOCKED) {
				throw new RuntimeException("Seat Not Locked");
			}

			if (seat.getLockedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
				throw new RuntimeException("Seat Lock Expired");
			}
			System.out.println("Total Amount : " + totalAmount);
		}

		Booking booking = new Booking();
		booking.setUser(user);
		booking.setShow(show);
		booking.setStatus(BookingStatus.PENDING_PAYMENT);
		booking.setTotalAmount(totalAmount);
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
		dto.setBookedAt(LocalDateTime.now());
		dto.setMovieName(savedBooking.getShow().getMovie().getTitle());
		dto.setScreenName(savedBooking.getShow().getScreen().getScreenName());
		dto.setSeats(savedBooking.getShowSeats());
		dto.setShowId(savedBooking.getShow().getId());
		dto.setShowTime(savedBooking.getShow().getStartTime());
		dto.setTheatreName(savedBooking.getShow().getScreen().getTheatre().getName());

		return dto;
	}

	@Override
	public Page<AdminBookingSummaryDTO> findAll(Pageable pageable) {
		Page<Booking> bookings = bookingRepository.findAll(pageable);

		// This converts the Page<Booking> to Page<AdminBookingSummaryDTO>
		Page<AdminBookingSummaryDTO> dtoPage = bookings.map(AdminBookingSummaryDTO::new);
		return dtoPage;
	}

	@Override
	@Transactional
	public void cancelBooking(Long bookingId, String email) {

		System.out.println("Canceling Booking...");
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking Not Found"));

		if (!booking.getUser().getEmail().equals(email)) {
			throw new RuntimeException("Unauthorized Cancellation");
		}

		if (booking.getStatus() == BookingStatus.CANCELLED) {
			throw new RuntimeException("Booking Already Cancelled");
		}

		if (booking.getShow().getStartTime().isBefore(LocalDateTime.now().plusHours(4))) {
			throw new RuntimeException("Show Can Not Be Cancel At this Time");
		}

		BigDecimal refund = calculateRefund(booking);

		List<ShowSeat> seats = showSeatRepository.findByBookingId(bookingId);
		for (ShowSeat seat : seats) {
			seat.setStatus(SeatStatus.AVAILABLE);
			seat.setBooking(null);
			seat.setLockedAt(null);
		}
		showSeatRepository.saveAll(seats);

		Payment payment = paymentRepository.findByBookingId(bookingId);
		if (payment != null) {
			String refundId = refundService.initiateRefund(booking, payment);
			booking.setRefundAmount(refund);
			booking.setStatus(BookingStatus.REFUNDED);
			payment.setStatus(PaymentStatus.REFUNDED);
			payment.setOrderId(refundId);
		} else {
			throw new RuntimeException("Payment Not Found");
		}
		paymentRepository.save(payment);
		bookingRepository.save(booking);
	}

	@Override
	public List<BookingResponseDTO> getMyBookings(String email) {
		List<Booking> bookings = bookingRepository.findByUserEmailOrderByBookedAtDesc(email);

		return bookings.stream().map(this::mapToDTO).toList();
	}

	private BookingResponseDTO mapToDTO(Booking booking) {

		BookingResponseDTO dto = new BookingResponseDTO();

		dto.setBookingId(booking.getId());
		dto.setShowId(booking.getShow().getId());
		dto.setStatus(booking.getStatus());
		dto.setTotalAmount(booking.getTotalAmount());
		dto.setBookedAt(booking.getBookedAt());
		dto.setSeats(null);
		dto.setMovieName(booking.getShow().getMovie().getTitle());
		dto.setTheatreName(booking.getShow().getScreen().getTheatre().getName());
		dto.setScreenName(booking.getShow().getScreen().getScreenName());
		dto.setShowTime(booking.getShow().getStartTime());

		return dto;
	}

	private BigDecimal calculateRefund(Booking booking) {

		LocalDateTime showTime = booking.getShow().getStartTime();
		long hours = Duration.between(LocalDateTime.now(), showTime).toHours();

		if (hours > 24)
			return booking.getTotalAmount();
		return new BigDecimal(0);
	}

}
