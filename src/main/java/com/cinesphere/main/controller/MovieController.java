package com.cinesphere.main.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public Movie addMovie(@RequestBody Movie movie) {
		return movieService.addMovie(movie);
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public List<Movie> getMovies() {
		return movieService.getAllActiveMovies();
	}
}
