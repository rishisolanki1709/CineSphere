package com.cinesphere.main.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class SeatLockRateLimiter {

	private static final int MAX_ATTEMPTS = 5;
	private static final long WINDOW_MS = 30_000; // 30 seconds

	private final Map<String, RequestCounter> cache = new ConcurrentHashMap<>();

	public void validate(String userEmail, Long showId) {
		String key = userEmail + ":" + showId;
		long now = System.currentTimeMillis();

		cache.compute(key, (k, counter) -> {
			if (counter == null || now - counter.startTime > WINDOW_MS) {
				return new RequestCounter(1, now);
			}

			if (counter.count >= MAX_ATTEMPTS) {
				throw new RuntimeException("Too Many Seat Lock Attempts. Please Wait.");
			}

			counter.count++;
			return counter;
		});
	}

	static class RequestCounter {
		int count;
		long startTime;

		RequestCounter(int count, long startTime) {
			this.count = count;
			this.startTime = startTime;
		}
	}
}
