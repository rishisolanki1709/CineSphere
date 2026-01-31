package com.cinesphere.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cinesphere.main.entity.Show;

public interface ShowRepository extends JpaRepository<Show, Long> {

	List<Show> findByScreenId(Long screenId);

	@Query("""
			    SELECT s FROM Show s
			    WHERE s.movie.id = :movieId
			    AND s.screen.theatre.city = :city
			    AND s.startDate > CURRENT_TIMESTAMP
			""")
	List<Show> findAvailableShows(Long movieId, String city);
}
