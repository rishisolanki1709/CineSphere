package com.cinesphere.main.service;

import java.io.IOException;
import java.util.List;

import com.cinesphere.main.dto.MovieRequestDTO;
import com.cinesphere.main.dto.MovieResponseDTO;

public interface MovieService {
	void addMovie(MovieRequestDTO movie) throws IOException;

	List<MovieResponseDTO> getAllMovies();

	MovieResponseDTO findById(Long id);

	List<MovieResponseDTO> getAllActiveMovies();

	void updateMovie(Long id, MovieRequestDTO movieRequest) throws IOException;

	void deleteMovie(Long id);

	List<MovieResponseDTO> getAllActiveMoviesByCity(String city);
}
