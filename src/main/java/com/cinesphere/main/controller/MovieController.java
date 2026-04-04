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

import com.cinesphere.main.dto.ApiResponse;
import com.cinesphere.main.dto.MovieRequestDTO;
import com.cinesphere.main.entity.Movie;
import com.cinesphere.main.service.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
	private final MovieService movieService;

	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Movie>> addMovie(@ModelAttribute MovieRequestDTO movieRequest)
			throws IOException {
		return ResponseEntity
				.ok(new ApiResponse<>(true, "Movie Added SuccessFully", movieService.addMovie(movieRequest)));
	}

	@GetMapping("id={id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Movie>> getMovieById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(new ApiResponse<Movie>(true, "Movie Fetched Successfully", movieService.findById(id)));
	}

	@PutMapping("id={id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Movie>> updateMovieById(@PathVariable Long id,
			@ModelAttribute MovieRequestDTO movieRequest) throws IOException {
		return ResponseEntity.ok(
				new ApiResponse<Movie>(true, "Movie Fetched Successfully", movieService.updateMovie(id, movieRequest)));
	}

	@GetMapping("all")
	public ResponseEntity<ApiResponse<List<Movie>>> getMovies() {
		return ResponseEntity.ok(new ApiResponse<>(true, "Movies Fetched Successfully", movieService.getAllMovies()));
	}

	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse<List<Movie>>> getActiveMovies() {
		return ResponseEntity
				.ok(new ApiResponse<>(true, "Movies Fetched Successfully", movieService.getAllActiveMovies()));
	}

	@GetMapping("city={city}")
	public ResponseEntity<ApiResponse<List<Movie>>> getActiveMoviesByCity(@PathVariable("city") String city) {
		if (city == null || city.isEmpty() || city.equals("All")) {
			return ResponseEntity
					.ok(new ApiResponse<>(true, "Movies Fetched Successfully", movieService.getAllMovies()));

		}
		return ResponseEntity.ok(
				new ApiResponse<>(true, "Movies Fetched Successfully", movieService.getAllActiveMoviesByCity(city)));
	}

	@DeleteMapping("id={id}")
	public ResponseEntity<ApiResponse<Void>> deleteMovies(@PathVariable Long id) {
		movieService.deleteMovie(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Movie Deleted Successfully", null));
	}
}
