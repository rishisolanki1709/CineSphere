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
import com.cinesphere.main.dto.ScreenResponseDTO;
import com.cinesphere.main.entity.Screen;
import com.cinesphere.main.service.ScreenService;

@RestController
@RequestMapping("/api/screens")
public class ScreenController {

	private final ScreenService screenService;

	public ScreenController(ScreenService screenService) {
		this.screenService = screenService;
	}

	// ADMIN only
	@PostMapping("/{theatreId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<ScreenResponseDTO>> addScreen(@PathVariable Long theatreId,
			@RequestBody Screen screen) {

		return ResponseEntity
				.ok(new ApiResponse<>(true, "Screen Added Successful", screenService.addScreen(theatreId, screen)));
	}

	// USER + ADMIN
	@GetMapping("/{theatreId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponse<List<ScreenResponseDTO>>> getScreens(@PathVariable Long theatreId) {

		return ResponseEntity.ok(
				new ApiResponse<>(true, "Screens fetched successfully", screenService.getScreensByTheatre(theatreId)));
	}
}
