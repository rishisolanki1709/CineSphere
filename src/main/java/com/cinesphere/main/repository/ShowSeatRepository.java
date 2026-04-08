package com.cinesphere.main.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.ShowSeat;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
	List<ShowSeat> findByShowId(Long showId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
			   SELECT ss FROM ShowSeat ss
			   WHERE ss.show.id = :showId
			   AND ss.seat.id IN :seatIds
			   AND ss.status = 'AVAILABLE'
			""")
	List<ShowSeat> lockSeats(@Param("showId") Long showId, @Param("seatIds") List<Long> seatIds);

	List<ShowSeat> findByBookingId(Long bookingId);

	List<ShowSeat> findByStatusAndLockedAtBefore(SeatStatus status, LocalDateTime time);

	@Query("SELECT COUNT(ss) FROM ShowSeat ss")
	long totalSeats();

	@Query("SELECT COUNT(ss) FROM ShowSeat ss WHERE ss.status='BOOKED'")
	long bookedSeats();

	@Modifying // Required for DELETE/UPDATE queries
	@Transactional
	@Query("DELETE FROM ShowSeat ss WHERE ss.show.id = :id")
	void deleteByShowId(@Param("id") Long id);

	@Query(value = "SELECT m.title as movieTitle, COUNT(ss.id) as ticketsSold " + "FROM show_seats ss "
			+ "JOIN bookings b ON ss.booking_id = b.id " + "JOIN shows s ON ss.show_id = s.id "
			+ "JOIN movies m ON s.movie_id = m.id " + "WHERE b.status = 'CONFIRMED' AND b.booked_at >= :startDate "
			+ "GROUP BY m.title " + "ORDER BY ticketsSold DESC", nativeQuery = true)
	List<Map<String, Object>> getTopMovies(LocalDateTime startDate);
}
