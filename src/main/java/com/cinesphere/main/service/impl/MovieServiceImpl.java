package com.cinesphere.main.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.entity.Movie;
import com.cinesphere.main.repository.MovieRepository;
import com.cinesphere.main.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

	private final MovieRepository movieRepository;

	public MovieServiceImpl(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	@Override
	public Movie addMovie(Movie movie) {
		return movieRepository.save(movie);
	}

	@Override
	public List<Movie> getAllActiveMovies() {
		return movieRepository.findByActiveTrue();
	}

}
