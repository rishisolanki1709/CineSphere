package com.cinesphere.main.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.cinesphere.main.entity.Booking;
import com.cinesphere.main.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByUserEmailOrderByBookedAtDesc(String email);

	// Spring Data JPA generates the query automatically from the method name
	List<Booking> findAllByStatusAndBookedAtBefore(BookingStatus status, LocalDateTime dateTime);

	// For the Pie Chart
	@Query(value = "SELECT status, COUNT(id) as count " + "FROM bookings " + "WHERE booked_at >= :startDate "
			+ "GROUP BY status", nativeQuery = true)
	List<Map<String, Object>> getBookingStatusStats(@Param("startDate") LocalDateTime startDate);

	// For the Line Chart (Revenue)
	@Query(value = "SELECT DATE_FORMAT(booked_at, '%Y-%m-%d') as date, SUM(total_amount) as totalRevenue "
			+ "FROM bookings " + "WHERE status = 'CONFIRMED' AND booked_at >= :startDate "
			+ "GROUP BY DATE_FORMAT(booked_at, '%Y-%m-%d') " + "ORDER BY date ASC", nativeQuery = true)
	List<Map<String, Object>> getRevenueStats(@Param("startDate") LocalDateTime startDate);

}
