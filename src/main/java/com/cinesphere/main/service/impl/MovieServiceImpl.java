package com.cinesphere.main.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.MovieRequestDTO;
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
	public Movie addMovie(MovieRequestDTO movieRequest) throws IOException {

		String imageUrl = imageUploadService.uploadImage(movieRequest.getImage());
		Movie movie = new Movie();
		movie.setTitle(movieRequest.getTitle());
		movie.setDescription(movieRequest.getDescription());
		movie.setDurationMinutes(movieRequest.getDurationMinutes());
		movie.setGenre(movieRequest.getGenre());
		movie.setLanguage(movieRequest.getLanguage());
		movie.setReleaseDate(movieRequest.getReleaseDate());
		movie.setPosterUrl(imageUrl);
		return movieRepository.save(movie);
	}

	@Override
	public List<Movie> getAllActiveMovies() {
		return movieRepository.findByActiveTrue();
	}

	@Override
	public List<Movie> getAllMovies() {
		return movieRepository.findAll();
	}

	@Override
	public Movie findById(Long id) {
		return movieRepository.findById(id).get();
	}

	@Override
	public Movie updateMovie(Long id, MovieRequestDTO movieRequest) throws IOException {
		String imageUrl = imageUploadService.uploadImage(movieRequest.getImage());
		Movie movie = movieRepository.findById(id).get();
		movie.setTitle(movieRequest.getTitle());
		movie.setDescription(movieRequest.getDescription());
		movie.setDurationMinutes(movieRequest.getDurationMinutes());
		movie.setGenre(movieRequest.getGenre());
		movie.setLanguage(movieRequest.getLanguage());
		movie.setReleaseDate(movieRequest.getReleaseDate());
		movie.setPosterUrl(imageUrl);
		return movieRepository.save(movie);
	}

	@Override
	public void deleteMovie(Long id) {
		movieRepository.deleteById(id);
	}
}
