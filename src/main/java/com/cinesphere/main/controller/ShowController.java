package com.cinesphere.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.ApiResponse;
import com.cinesphere.main.dto.ShowResponseDTO;
import com.cinesphere.main.entity.Show;
import com.cinesphere.main.service.ShowService;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

	private final ShowService showService;

	public ShowController(ShowService showService) {
		this.showService = showService;
	}

	// ADMIN → create show
	@PostMapping("/{movieId}/{screenId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<ShowResponseDTO>> createShow(@PathVariable Long movieId,
			@PathVariable Long screenId, @RequestBody Show show) {

		return ResponseEntity.ok(
				new ApiResponse<>(true, "Show created successfully", showService.createShow(movieId, screenId, show)));
	}

	// USER + ADMIN → view shows
	@GetMapping("/screen/{screenId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponse<List<ShowResponseDTO>>> getShows(@PathVariable Long screenId) {

		return ResponseEntity.ok(new ApiResponse<>(true, "Shows fetched successfully", showService.getShowsByScreen(screenId)));
	}
}
