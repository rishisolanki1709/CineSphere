package com.cinesphere.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

//import org.springframework.data.jpa.repository.JpaRepository;

import com.cinesphere.main.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	List<Movie> findByActiveTrue();
}
