package com.cinesphere.main.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.ShowSeat;
import com.cinesphere.main.repository.ShowSeatRepository;

import jakarta.transaction.Transactional;

@Component
public class SeatAutoReleaseScheduler {
	private final ShowSeatRepository showSeatRepository;

	public SeatAutoReleaseScheduler(ShowSeatRepository showSeatRepository) {
		this.showSeatRepository = showSeatRepository;
	}

	@Transactional
	@Scheduled(fixedRate = 60000) // every 1 minute
	public void releaseExpiredLocks() {
		System.out.println("Seat Auto Release Scheduler On Fire");
		LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(5);
		List<ShowSeat> expiredSeats = showSeatRepository.findByStatusAndLockedAtBefore(SeatStatus.LOCKED, expiryTime);
		if (expiredSeats.isEmpty())
			return;
		for (ShowSeat seat : expiredSeats) {
			System.out.println("Free Seats");
			if (seat.getStatus() == SeatStatus.LOCKED
					&& seat.getLockedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
				seat.setStatus(SeatStatus.AVAILABLE);
				seat.setLockedAt(null);
				seat.setBooking(null);
			}
		}
		showSeatRepository.saveAll(expiredSeats);
	}

}
