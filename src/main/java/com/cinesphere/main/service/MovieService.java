package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.entity.Movie;

public interface MovieService {
	Movie addMovie(Movie movie);

	List<Movie> getAllActiveMovies();

}
