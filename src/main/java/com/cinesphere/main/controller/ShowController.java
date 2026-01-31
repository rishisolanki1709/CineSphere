package com.cinesphere.main.controller;

import java.util.List;

//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.ApiResponse;
import com.cinesphere.main.dto.ShowResponseDTO;
import com.cinesphere.main.entity.Show;
import com.cinesphere.main.service.SeatLockService;
import com.cinesphere.main.service.ShowService;
import com.cinesphere.main.service.impl.BookingServiceImpl;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

	private final ShowService showService;
	private final BookingServiceImpl bookingService;
	private final SeatLockService lockService;

	public ShowController(ShowService showService, SeatLockService lockService, BookingServiceImpl bookingService) {
		this.showService = showService;
		this.lockService = lockService;
		this.bookingService = bookingService;
	}

	@PostMapping("/shows/{showId}/confirm-booking")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<String>> confirmBooking(@PathVariable Long showId,
			@RequestBody List<Long> showSeatIds, Authentication authentication) {
		bookingService.confirmBooking(showId, showSeatIds, authentication.getName());
		return ResponseEntity.ok(new ApiResponse<>(true, "Booking confirmed", null));
	}

	// ADMIN → create show
	@PostMapping("/{movieId}/{screenId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<ShowResponseDTO>> createShow(@PathVariable Long movieId,
			@PathVariable Long screenId, @RequestBody Show show) {

		return ResponseEntity.ok(
				new ApiResponse<>(true, "Show created successfully", showService.createShow(movieId, screenId, show)));
	}

	@PostMapping("/shows/{showId}/lock-seats")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<String>> lockSeats(@PathVariable Long showId,
			@RequestBody List<Long> showSeatIds) {
		lockService.lockSeats(showId, showSeatIds);
		return ResponseEntity.ok(new ApiResponse<>(true, "Seats locked for 5 minutes", null));
	}

	// USER + ADMIN → view shows
	@GetMapping("/screen/{screenId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponse<List<ShowResponseDTO>>> getShows(@PathVariable Long screenId) {

		return ResponseEntity
				.ok(new ApiResponse<>(true, "Shows fetched successfully", showService.getShowsByScreen(screenId)));
	}
}
