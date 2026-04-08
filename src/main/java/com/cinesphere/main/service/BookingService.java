package com.cinesphere.main.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cinesphere.main.dto.AdminBookingSummaryDTO;
import com.cinesphere.main.dto.BookingResponseDTO;

public interface BookingService {
	public BookingResponseDTO confirmBooking(Long showId, List<Long> showSeatIds, String email);

	public void cancelBooking(Long bookingId, String email);

	List<BookingResponseDTO> getMyBookings(String email);

	Page<AdminBookingSummaryDTO> findAll(Pageable pageable);

}
