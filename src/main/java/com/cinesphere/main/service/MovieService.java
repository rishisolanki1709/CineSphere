package com.cinesphere.main.service;

import java.io.IOException;
import java.util.List;

import com.cinesphere.main.dto.MovieRequestDTO;
import com.cinesphere.main.entity.Movie;

public interface MovieService {
	Movie addMovie(MovieRequestDTO movie) throws IOException;

	List<Movie> getAllMovies();

	Movie findById(Long id);

	List<Movie> getAllActiveMovies();

	Movie updateMovie(Long id, MovieRequestDTO movieRequest) throws IOException;

	void deleteMovie(Long id);

	List<Movie> getAllActiveMoviesByCity(String city);
}
