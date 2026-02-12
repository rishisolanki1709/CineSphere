package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.dto.BookingResponseDTO;
import com.cinesphere.main.dto.TicketResponseDTO;

public interface BookinService {
	public BookingResponseDTO confirmBooking(Long showId, List<Long> showSeatIds, String email);

	public void cancelBooking(Long bookingId, String email);

	List<BookingResponseDTO> getMyBookings(String email);

	BookingResponseDTO getBookingDetails(Long bookingId, String email);

	TicketResponseDTO getTicket(Long bookingId, String email);

}
