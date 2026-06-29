package com.cinesphere.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.SeatResponseDTO;
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
	public List<SeatResponseDTO> getSeatsByScreenId(Long screenId) {
		List<Seat> seatLs = seatRepository.findByScreenId(screenId);
		List<SeatResponseDTO> list = new ArrayList<>();
		for (Seat seat : seatLs) {
			list.add(mapToResponseDTO(seat));
		}
		return list;
	}

	private SeatResponseDTO mapToResponseDTO(Seat seat) {

		return new SeatResponseDTO(seat.getId(), seat.getRowIndex(), seat.getColIndex(), seat.getSeatType(),
				seat.getStatus(), seat.getScreen().getId());
	}
}
