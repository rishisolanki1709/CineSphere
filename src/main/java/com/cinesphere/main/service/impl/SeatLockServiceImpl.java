package com.cinesphere.main.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.ShowSeat;
import com.cinesphere.main.repository.ShowSeatRepository;
import com.cinesphere.main.service.SeatLockService;

import jakarta.transaction.Transactional;

@Service
public class SeatLockServiceImpl implements SeatLockService {

	private static final long LOCK_TIME_MINUTES = 5;

	private final ShowSeatRepository showSeatRepository;

	public SeatLockServiceImpl(ShowSeatRepository showSeatRepository) {
		this.showSeatRepository = showSeatRepository;
	}

	@Override
	@Transactional
	public void lockSeats(Long showId, List<Long> showSeatIds) {

		List<ShowSeat> seats = showSeatRepository.findAllById(showSeatIds);

		for (ShowSeat seat : seats) {

			// ❌ Already booked
			if (seat.getStatus() == SeatStatus.BOOKED) {
				throw new RuntimeException("Seat Already Booked");
			}

			// ❌ Locked but not expired
			if (seat.getStatus() == SeatStatus.LOCKED && !isLockExpired(seat.getLockedAt())) {
				throw new RuntimeException("Seat Temporarily Locked");
			}

			seat.setStatus(SeatStatus.LOCKED);
			seat.setLockedAt(LocalDateTime.now());
		}

		showSeatRepository.saveAll(seats);
	}

	private boolean isLockExpired(LocalDateTime lockedAt) {
		return lockedAt.plusMinutes(LOCK_TIME_MINUTES).isBefore(LocalDateTime.now());
	}
}
