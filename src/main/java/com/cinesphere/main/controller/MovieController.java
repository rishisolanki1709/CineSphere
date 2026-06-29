package com.cinesphere.main.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.ApiResponseDTO;
import com.cinesphere.main.dto.MovieRequestDTO;
import com.cinesphere.main.dto.MovieResponseDTO;
import com.cinesphere.main.service.MovieService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
	private final MovieService movieService;

	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseDTO<Void>> addMovie(@Valid @ModelAttribute MovieRequestDTO movieRequest)
			throws IOException {
		return ResponseEntity.ok(new ApiResponseDTO<>(true, "Movie Added SuccessFully", null));
	}

	@GetMapping("id={id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseDTO<MovieResponseDTO>> getMovieById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(
				new ApiResponseDTO<MovieResponseDTO>(true, "Movie Fetched Successfully", movieService.findById(id)));
	}

	@PutMapping("id={id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseDTO<Void>> updateMovieById(@PathVariable Long id,
			@Valid @ModelAttribute MovieRequestDTO movieRequest) throws IOException {
		return ResponseEntity.ok(new ApiResponseDTO<>(true, "Movie Fetched Successfully", null));
	}

	@GetMapping("all")
	public ResponseEntity<ApiResponseDTO<List<MovieResponseDTO>>> getMovies() {
		return ResponseEntity
				.ok(new ApiResponseDTO<>(true, "Movies Fetched Successfully", movieService.getAllMovies()));
	}

	@GetMapping("/all-active")
	public ResponseEntity<ApiResponseDTO<List<MovieResponseDTO>>> getActiveMovies() {
		return ResponseEntity
				.ok(new ApiResponseDTO<>(true, "Movies Fetched Successfully", movieService.getAllActiveMovies()));
	}

	@GetMapping("city={city}")
	public ResponseEntity<ApiResponseDTO<List<MovieResponseDTO>>> getActiveMoviesByCity(
			@PathVariable("city") String city) {
		if (city == null || city.isEmpty() || city.equals("All")) {
			return ResponseEntity
					.ok(new ApiResponseDTO<>(true, "Movies Fetched Successfully", movieService.getAllMovies()));

		}
		return ResponseEntity.ok(
				new ApiResponseDTO<>(true, "Movies Fetched Successfully", movieService.getAllActiveMoviesByCity(city)));
	}

	@DeleteMapping("id={id}")
	public ResponseEntity<ApiResponseDTO<Void>> deleteMovies(@PathVariable Long id) {
		movieService.deleteMovie(id);
		return ResponseEntity.ok(new ApiResponseDTO<>(true, "Movie Deleted Successfully", null));
	}
}
