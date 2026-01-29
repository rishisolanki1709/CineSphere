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
	public ResponseEntity<TheatreResponseDTO> addTheatre(@RequestBody Theatre theatre) {
		return ResponseEntity.ok(theatreService.addTheatre(theatre));
	}

	@GetMapping("/{city}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<List<TheatreResponseDTO>> getTheatres(@PathVariable String city) {
		return ResponseEntity.ok(theatreService.getTheatresByCity(city));
	}
}
