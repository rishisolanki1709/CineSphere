package com.cinesphere.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.ApiResponse;
import com.cinesphere.main.dto.LockSeatDTO;
import com.cinesphere.main.dto.ShowResponseDTO;
import com.cinesphere.main.dto.ShowSeatResponseDTO;
import com.cinesphere.main.entity.Show;
import com.cinesphere.main.service.SeatLockService;
import com.cinesphere.main.service.ShowService;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

	private final ShowService showService;
	private final SeatLockService lockService;

	public ShowController(ShowService showService, SeatLockService lockService) {
		this.showService = showService;
		this.lockService = lockService;
	}

	// ADMIN → create show
	@PostMapping("/{movieId}/{screenId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<ShowResponseDTO>> createShow(@PathVariable Long movieId,
			@PathVariable Long screenId, @RequestBody Show show) {

		return ResponseEntity.ok(
				new ApiResponse<>(true, "Show Created Successfully", showService.createShow(movieId, screenId, show)));
	}

	@PostMapping("/lock-seats")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<String>> lockSeats(@RequestBody LockSeatDTO lockSeatDTO) {
		lockService.lockSeats(lockSeatDTO.getShowId(), lockSeatDTO.getShowSeatIds(),
				lockSeatDTO.getAuthentication().getName());
		return ResponseEntity.ok(new ApiResponse<>(true, "Seats Locked For 5 Minutes", null));
	}

	// USER + ADMIN → view shows
	@GetMapping("/screen/{screenId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponse<List<ShowResponseDTO>>> getShows(@PathVariable Long screenId) {

		return ResponseEntity
				.ok(new ApiResponse<>(true, "Shows Fetched Successfully", showService.getShowsByScreen(screenId)));
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponse<List<ShowResponseDTO>>> getShows(@RequestParam Long movieId,
			@RequestParam String city) {
		return ResponseEntity
				.ok(new ApiResponse<>(true, "Shows Fetched Successfully", showService.getShows(movieId, city)));
	}

	@GetMapping("/{showId}/seats")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponse<List<ShowSeatResponseDTO>>> getShowSeats(@PathVariable Long showId) {
		return ResponseEntity
				.ok(new ApiResponse<>(true, "Seats Fetched Successfully", showService.getShowSeats(showId)));
	}

}
