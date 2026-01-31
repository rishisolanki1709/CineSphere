package com.cinesphere.main.service;

import java.util.List;

public interface BookinService {
	public void confirmBooking(Long showId, List<Long> showSeatIds, String email);

	public void cancelBooking(Long bookingId, String email);
}
