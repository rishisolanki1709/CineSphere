package com.cinesphere.main.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cinesphere.main.dto.MovieStatsDTO;
import com.cinesphere.main.entity.Booking;
import com.cinesphere.main.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByUserEmailOrderByBookedAtDesc(String email);

	@Query("SELECT COUNT(b) FROM Booking b WHERE b.status='CONFIRMED'")
	long totalBookings();

	@Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status='CONFIRMED'")
	Double totalRevenue();

	@Query(value = """
			SELECT COUNT(*)
			FROM bookings
			WHERE status='CONFIRMED'
			AND DATE(booked_at)=CURDATE()
			""", nativeQuery = true)
	long bookingsToday();

	// Spring Data JPA generates the query automatically from the method name
	List<Booking> findAllByStatusAndBookedAtBefore(BookingStatus status, LocalDateTime dateTime);

	@Query("SELECT new com.cinesphere.dto.MovieStatsDTO(m.title, COUNT(ss.id)) " + "FROM ShowSeat ss "
			+ "JOIN ss.booking b " + "JOIN ss.show s " + "JOIN s.movie m "
			+ "WHERE b.status = com.cinesphere.enums.BookingStatus.CONFIRMED " + "GROUP BY m.title "
			+ "ORDER BY COUNT(ss.id) DESC")
	List<MovieStatsDTO> getTopPerformingMovies();
}
