package com.cinesphere.main.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cinesphere.main.entity.Show;

public interface ShowRepository extends JpaRepository<Show, Long> {

	List<Show> findByScreenId(Long screenId);

	@Query("""
			    SELECT s FROM Show s
			    WHERE s.movie.id = :movieId
			    AND s.screen.theatre.city = :city
			    AND s.startTime > CURRENT_TIMESTAMP
			""")
	List<Show> findAvailableShows(@Param("movieId") Long movieId, @Param("city") String city);

	@Query("""
			    SELECT s FROM Show s
			    WHERE s.screen.id = :screenId
			    AND :startTime < s.endTime
			    AND :endTime > s.startTime
			""")
	List<Show> findOverlappingShows(@Param("screenId") Long screenId, @Param("startTime") LocalDateTime startTime,
			@Param("endTime") LocalDateTime endTime);
}
