package com.cinesphere.main.service;

import java.util.List;

public interface SeatLockService {
	public void lockSeats(Long showId, List<Long> showSeatIds);
}
