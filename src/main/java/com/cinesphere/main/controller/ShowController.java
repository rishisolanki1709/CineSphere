package com.cinesphere.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.ApiResponseDTO;
import com.cinesphere.main.dto.LockSeatDTO;
import com.cinesphere.main.dto.ShowRequestDTO;
import com.cinesphere.main.dto.ShowResponseDTO;
import com.cinesphere.main.dto.ShowSeatResponseDTO;
import com.cinesphere.main.service.SeatLockService;
import com.cinesphere.main.service.ShowService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

	private final ShowService showService;
	private final SeatLockService lockService;

	public ShowController(ShowService showService, SeatLockService lockService) {
		this.showService = showService;
		this.lockService = lockService;
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseDTO<ShowResponseDTO>> createShow(@Valid @RequestBody ShowRequestDTO dto) {
		return ResponseEntity.ok(new ApiResponseDTO<>(true, "Show Created Successfully", showService.createShow(dto)));
	}

	@PostMapping("/lock-seats")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponseDTO<String>> lockSeats(@Valid @RequestBody LockSeatDTO lockSeatDTO,
			Authentication authentication) {
		lockService.lockSeats(lockSeatDTO.getShowId(), lockSeatDTO.getShowSeatIds(), authentication.getName());
		return ResponseEntity.ok(new ApiResponseDTO<>(true, "Seats Locked For 5 Minutes", null));
	}

	@GetMapping("/screen/id={screenId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponseDTO<List<ShowResponseDTO>>> getShowsByScreenId(@PathVariable Long screenId) {

		return ResponseEntity
				.ok(new ApiResponseDTO<>(true, "Shows Fetched Successfully", showService.getShowsByScreenId(screenId)));
	}

	@GetMapping("movie/id={movieId}/city={city}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponseDTO<List<ShowResponseDTO>>> getShowsByMoiveIdAndCity(@PathVariable Long movieId,
			@PathVariable String city) {
		return ResponseEntity.ok(new ApiResponseDTO<>(true, "Shows Fetched Successfully",
				showService.getShowsByMoiveIdAndCity(movieId, city)));
	}

	@GetMapping("all")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponseDTO<List<ShowResponseDTO>>> getAllShows() {
		return ResponseEntity.ok(new ApiResponseDTO<List<ShowResponseDTO>>(true, "Shows Fetched Successfully",
				showService.getAllShows()));
	}

	@GetMapping("/id={showId}/seats")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponseDTO<List<ShowSeatResponseDTO>>> getShowSeats(@PathVariable Long showId) {
		return ResponseEntity
				.ok(new ApiResponseDTO<>(true, "Seats Fetched Successfully", showService.getShowSeatsByShowId(showId)));
	}

	@GetMapping("id={showId}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponseDTO<ShowResponseDTO>> getShowById(@PathVariable Long showId) {
		return ResponseEntity
				.ok(new ApiResponseDTO<>(true, "Seats Fetched Successfully", showService.getShowbyId(showId)));
	}

	@DeleteMapping("id={showId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseDTO<String>> deleteShowById(@PathVariable("showId") Long id) {
		showService.deleteShowById(id);
		return ResponseEntity.ok(new ApiResponseDTO<>(true, "Show Successfull Deleted", null));
	}

	@GetMapping("/movie/id={movieId}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponseDTO<List<ShowResponseDTO>>> getShowsByMoiveId(@PathVariable Long movieId) {
		return ResponseEntity
				.ok(new ApiResponseDTO<>(true, "Shows Fetched Successfully", showService.getShowsByMoiveId(movieId)));
	}

}
