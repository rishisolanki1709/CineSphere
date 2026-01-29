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
import com.cinesphere.main.dto.TheatreResponseDTO;
import com.cinesphere.main.entity.Theatre;
import com.cinesphere.main.service.TheatreService;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {

	private final TheatreService theatreService;

	public TheatreController(TheatreService theatreService) {
		this.theatreService = theatreService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<TheatreResponseDTO>> addTheatre(@RequestBody Theatre theatre) {
		TheatreResponseDTO dto = theatreService.addTheatre(theatre);
		return ResponseEntity.ok(new ApiResponse<>(true, "Theatre added successfully", dto));
	}

	@GetMapping("/{city}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponse<List<TheatreResponseDTO>>> getTheatres(@PathVariable String city) {
		List<TheatreResponseDTO> dto = theatreService.getTheatresByCity(city);
		return ResponseEntity.ok(new ApiResponse<>(true, "Theatres fetched successfully", dto));
	}
}
