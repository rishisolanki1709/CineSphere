package com.cinesphere.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<TheatreResponseDTO>> addTheatre(@RequestBody Theatre theatre) {
		TheatreResponseDTO dto = theatreService.addTheatre(theatre);
		return ResponseEntity.ok(new ApiResponse<>(true, "Theatre Added Successfully", dto));
	}
	
	@PutMapping("/edit/id={id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<TheatreResponseDTO>> updateTheatre(@PathVariable("id") Long id,  @RequestBody Theatre theatre) {
		TheatreResponseDTO dto = theatreService.updateTheatre(id, theatre);
		return ResponseEntity.ok(new ApiResponse<>(true, "Theatre Updated Successfully", dto));
	}

	@GetMapping("city={city}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponse<List<TheatreResponseDTO>>> getTheatresByCity(@PathVariable String city) {
		List<TheatreResponseDTO> dto = theatreService.getTheatresByCity(city);
		return ResponseEntity.ok(new ApiResponse<>(true, "Theatres Fetched Successfully", dto));
	}
	
	@GetMapping("id={id}")
	public ResponseEntity<ApiResponse<TheatreResponseDTO>> getTheatresById(@PathVariable Long id) {
		System.out.println(id);
		TheatreResponseDTO dto = theatreService.getTheatresById(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Theatres Fetched Successfully", dto));
	}

	@GetMapping("/all")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponse<List<TheatreResponseDTO>>> getAllTheatres() {
		List<TheatreResponseDTO> dto = theatreService.getAllTheatres();
		return ResponseEntity.ok(new ApiResponse<>(true, "Theatres Fetched Successfully", dto));
	}

	@PutMapping("/id={id}/status={newStatus}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> updateTheatreStatus(@PathVariable("id") Long id,
			@PathVariable("newStatus") boolean newStatus) {
		theatreService.updateStatus(id, newStatus);
		return ResponseEntity.ok(new ApiResponse<>(true, "Theatre Status Changed Successfully", null));
	}
	
	@DeleteMapping("id={id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteTheatre(@PathVariable Long id){
		theatreService.deleteTheatre(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Theatre Deleted Successfully", null));
	}
	
}
