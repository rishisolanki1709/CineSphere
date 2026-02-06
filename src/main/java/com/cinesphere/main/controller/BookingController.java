package com.cinesphere.main.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.ApiResponse;
import com.cinesphere.main.dto.BookingRequestDTO;
import com.cinesphere.main.dto.BookingResponseDTO;
import com.cinesphere.main.service.BookinService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookinService bookingService;

	public BookingController(BookinService bookingService) {
		this.bookingService = bookingService;
	}

	@PostMapping("/confirm")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<BookingResponseDTO>> confirmBooking(@RequestBody BookingRequestDTO request,
			Authentication authentication) {

		BookingResponseDTO dto = bookingService.confirmBooking(request.getShowId(), request.getShowSeatIds(),
				authentication.getName());

		return ResponseEntity.ok(new ApiResponse<>(true, "Booking Confirmed Successfully", dto));
	}

	@DeleteMapping("/cancel/{bookingId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<Void>> cancelBooking(@PathVariable Long bookingId,
			Authentication authentication) {

		bookingService.cancelBooking(bookingId, authentication.getName());

		return ResponseEntity.ok(new ApiResponse<>(true, "Booking Cancelled Successfully", null));
	}
}
