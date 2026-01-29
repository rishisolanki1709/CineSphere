package com.cinesphere.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.ApiResponse;
import com.cinesphere.main.entity.Movie;
import com.cinesphere.main.service.impl.MovieServiceImpl;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
	private final MovieServiceImpl movieService;

	public MovieController(MovieServiceImpl movieService) {
		this.movieService = movieService;
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Movie>> addMovie(@RequestBody Movie movie) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Movie Added SuccessFully", movieService.addMovie(movie)));
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<ApiResponse<List<Movie>>> getMovies() {
		return ResponseEntity
				.ok(new ApiResponse<>(true, "Movies fetched successfully", movieService.getAllActiveMovies()));
	}
}
