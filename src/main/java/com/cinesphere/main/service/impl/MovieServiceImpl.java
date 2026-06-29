package com.cinesphere.main.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.MovieRequestDTO;
import com.cinesphere.main.dto.MovieResponseDTO;
import com.cinesphere.main.entity.Movie;
import com.cinesphere.main.repository.MovieRepository;
import com.cinesphere.main.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

	private final MovieRepository movieRepository;
	private final ImageUploadService imageUploadService;

	public MovieServiceImpl(MovieRepository movieRepository, ImageUploadService imageUploadService) {
		this.movieRepository = movieRepository;
		this.imageUploadService = imageUploadService;
	}

	@Override
	public void addMovie(MovieRequestDTO movieRequest) throws IOException {

		String imageUrl = imageUploadService.uploadImage(movieRequest.getImage());
		Movie movie = new Movie();
		movie.setTitle(movieRequest.getTitle());
		movie.setDescription(movieRequest.getDescription());
		movie.setDurationMinutes(movieRequest.getDurationMinutes());
		movie.setGenre(movieRequest.getGenre());
		movie.setLanguage(movieRequest.getLanguage());
		movie.setReleaseDate(movieRequest.getReleaseDate());
		movie.setPosterUrl(imageUrl);
		movieRepository.save(movie);
	}

	@Override
	public List<MovieResponseDTO> getAllActiveMovies() {
		List<Movie> movieLs = movieRepository.findByActiveTrue();
		List<MovieResponseDTO> list = new ArrayList<>();
		for (Movie movie : movieLs) {
			list.add(mapToResponse(movie));
		}
		return list;
	}

	@Override
	public List<MovieResponseDTO> getAllMovies() {
		List<Movie> movieLs = movieRepository.findAll();
		List<MovieResponseDTO> list = new ArrayList<>();
		for (Movie movie : movieLs) {
			list.add(mapToResponse(movie));
		}
		return list;
	}

	@Override
	public MovieResponseDTO findById(Long id) {
		Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie Not Found"));
		return mapToResponse(movie);
	}

	@Override
	public void updateMovie(Long id, MovieRequestDTO movieRequest) throws IOException {
		String imageUrl = imageUploadService.uploadImage(movieRequest.getImage());
		Movie movie = movieRepository.findById(id).get();
		movie.setTitle(movieRequest.getTitle());
		movie.setDescription(movieRequest.getDescription());
		movie.setDurationMinutes(movieRequest.getDurationMinutes());
		movie.setGenre(movieRequest.getGenre());
		movie.setLanguage(movieRequest.getLanguage());
		movie.setReleaseDate(movieRequest.getReleaseDate());
		movie.setPosterUrl(imageUrl);
	}

	@Override
	public void deleteMovie(Long id) {
		movieRepository.deleteById(id);
	}

	@Override
	public List<MovieResponseDTO> getAllActiveMoviesByCity(String city) {
		List<Movie> movieLs = movieRepository.findActiveMoviesByCity(city);
		List<MovieResponseDTO> list = new ArrayList<>();
		for (Movie movie : movieLs) {
			list.add(mapToResponse(movie));
		}
		return list;
	}

	private MovieResponseDTO mapToResponse(Movie movie) {

		return new MovieResponseDTO(movie.getId(), movie.getTitle(), movie.getDescription(), movie.getLanguage(),
				movie.getDurationMinutes(), movie.getGenre(), movie.getReleaseDate(), movie.isActive(),
				movie.getPosterUrl());
	}
}
