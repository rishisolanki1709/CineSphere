package com.cinesphere.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.ApiResponse;
import com.cinesphere.main.entity.Seat;
import com.cinesphere.main.service.SeatService;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

	private final SeatService seatService;

	public SeatController(SeatService seatService) {
		this.seatService = seatService;
	}

	// USER + ADMIN → view seats
	@GetMapping("/{screenId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponse<List<Seat>>> getSeats(@PathVariable Long screenId) {
		List<Seat> seatList= seatService.getSeatsByScreenId(screenId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Seats Fetched Successfully", seatList));
	}
}
