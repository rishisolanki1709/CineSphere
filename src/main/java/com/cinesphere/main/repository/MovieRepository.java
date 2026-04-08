package com.cinesphere.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cinesphere.main.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	List<Movie> findByActiveTrue();

	@Query(value = "SELECT DISTINCT m.* FROM movies m " + "JOIN shows s ON m.id = s.movie_id "
			+ "JOIN screens sc ON s.screen_id = sc.id " + "JOIN theatres t ON sc.theatre_id = t.id "
			+ "WHERE t.city = :city " + "AND s.start_time > NOW() " + "AND s.status = 'ACTIVE'", nativeQuery = true)
	List<Movie> findActiveMoviesByCity(@Param("city") String city);

}
