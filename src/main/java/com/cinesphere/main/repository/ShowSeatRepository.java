package com.cinesphere.main.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.ShowSeat;

import jakarta.persistence.LockModeType;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
	List<ShowSeat> findByShowId(Long showId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
			   SELECT ss FROM ShowSeat ss
			   WHERE ss.show.id = :showId
			   AND ss.seat.id IN :seatIds
			   AND ss.status = 'AVAILABLE'
			""")
	List<ShowSeat> lockSeats(Long showId, List<Long> seatIds);

	List<ShowSeat> findByBookingId(Long bookingId);

	List<ShowSeat> findByStatusAndLockedAtBefore(SeatStatus status, LocalDateTime time);

}
