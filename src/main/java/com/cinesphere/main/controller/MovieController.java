package com.cinesphere.main.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.ApiResponse;
import com.cinesphere.main.dto.MovieRequestDTO;
import com.cinesphere.main.entity.Movie;
import com.cinesphere.main.service.impl.ImageUploadService;
import com.cinesphere.main.service.impl.MovieServiceImpl;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
	private final MovieServiceImpl movieService;
	private final ImageUploadService imageUploadService;

	public MovieController(MovieServiceImpl movieService, ImageUploadService imageUploadService) {
		this.movieService = movieService;
		this.imageUploadService = imageUploadService;
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Movie>> addMovie(@ModelAttribute MovieRequestDTO movieRequest) throws IOException {

		String imageUrl = imageUploadService.uploadImage(movieRequest.getImage());
		Movie movie = new Movie();
		movie.setTitle(movieRequest.getTitle());
		movie.setDescription(movieRequest.getDescription());
		movie.setDurationMinutes(movieRequest.getDurationMinutes());
		movie.setGenre(movieRequest.getGenre());
		movie.setLanguage(movieRequest.getLanguage());
		movie.setReleaseDate(movieRequest.getReleaseDate());
		movie.setPosterUrl(imageUrl);
		return ResponseEntity.ok(new ApiResponse<>(true, "Movie Added SuccessFully", movieService.addMovie(movie)));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<Movie>>> getMovies() {
		return ResponseEntity
				.ok(new ApiResponse<>(true, "Movies Fetched Successfully", movieService.getAllActiveMovies()));
	}
}
