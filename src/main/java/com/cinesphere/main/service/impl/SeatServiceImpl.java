package com.cinesphere.main.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.entity.Seat;
import com.cinesphere.main.repository.ScreenRepository;
import com.cinesphere.main.repository.SeatRepository;
import com.cinesphere.main.service.SeatService;

@Service
public class SeatServiceImpl implements SeatService {

	private final SeatRepository seatRepository;

	public SeatServiceImpl(SeatRepository seatRepository, ScreenRepository screenRepository) {
		this.seatRepository = seatRepository;
	}

	@Override
	public List<Seat> getSeatsByScreenId(Long screenId) {
		return seatRepository.findByScreenId(screenId);
	}
}
