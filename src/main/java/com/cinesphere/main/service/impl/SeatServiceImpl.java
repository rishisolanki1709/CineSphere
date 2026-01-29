package com.cinesphere.main.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.SeatResponseDTO;
import com.cinesphere.main.entity.Screen;
import com.cinesphere.main.entity.Seat;
import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.SeatType;
import com.cinesphere.main.repository.ScreenRepository;
import com.cinesphere.main.repository.SeatRepository;
import com.cinesphere.main.service.SeatService;

@Service
public class SeatServiceImpl implements SeatService {

	private final SeatRepository seatRepository;
	private final ScreenRepository screenRepository;

	public SeatServiceImpl(SeatRepository seatRepository, ScreenRepository screenRepository) {
		this.seatRepository = seatRepository;
		this.screenRepository = screenRepository;
	}

	@Override
	public void createSeatsForScreen(Long screenId) {

		Screen screen = screenRepository.findById(screenId).orElseThrow(() -> new RuntimeException("Screen not found"));

		char row = 'A';
		int seatsPerRow = 10;

		for (int i = 0; i < screen.getTotalSeats(); i++) {

			Seat seat = new Seat();
			seat.setSeatRow(String.valueOf(row));
			seat.setSeatNumber((i % seatsPerRow) + 1);
			seat.setSeatType(i < 40 ? SeatType.REGULAR : SeatType.PREMIUM);
			seat.setStatus(SeatStatus.AVAILABLE);
			seat.setScreen(screen);

			seatRepository.save(seat);

			if ((i + 1) % seatsPerRow == 0) {
				row++;
			}
		}
	}

	@Override
	public List<SeatResponseDTO> getSeatsByScreen(Long screenId) {
		return seatRepository.findByScreenId(screenId).stream().map(seat -> {
			SeatResponseDTO dto = new SeatResponseDTO();
			dto.setId(seat.getId());
			dto.setSeatRow(seat.getSeatRow());
			dto.setSeatNumber(seat.getSeatNumber());
			dto.setSeatType(seat.getSeatType());
			dto.setStatus(seat.getStatus());
			return dto;
		}).toList();
	}
}
