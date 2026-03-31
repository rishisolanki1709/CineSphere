package com.cinesphere.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.ScreenRequestDTO;
import com.cinesphere.main.dto.ScreenRequestDTO.SeatRequestDTO;
import com.cinesphere.main.dto.ScreenResponseDTO;
import com.cinesphere.main.entity.Screen;
import com.cinesphere.main.entity.Seat;
import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.SeatType;
import com.cinesphere.main.entity.Theatre;
import com.cinesphere.main.repository.ScreenRepository;
import com.cinesphere.main.repository.SeatRepository;
import com.cinesphere.main.repository.TheatreRepository;
import com.cinesphere.main.service.ScreenService;

import jakarta.transaction.Transactional;

@Service
public class ScreenServiceImpl implements ScreenService {

	private final SeatRepository seatRepository;

	private final ScreenRepository screenRepository;
	private final TheatreRepository theatreRepository;

	public ScreenServiceImpl(ScreenRepository screenRepository, TheatreRepository theatreRepository,
			SeatRepository seatRepository) {
		this.screenRepository = screenRepository;
		this.theatreRepository = theatreRepository;
		this.seatRepository = seatRepository;
	}

	@Override
	public void addScreen(ScreenRequestDTO dto) {
		// 1. Fetch Theatre
		Theatre theatre = theatreRepository.findById(dto.getTheatreId())
				.orElseThrow(() -> new RuntimeException("Theatre not found"));

		// 2. Create Screen Entity
		Screen screen = new Screen();
		screen.setScreenName(dto.getScreenName());
		screen.setTheatre(theatre);
		screen.setTotalSeats(dto.getSeats().size());
		screen.setMaxRows(dto.getMaxRows());
		screen.setMaxCols(dto.getMaxCols());

		// 3. Convert SeatDTOs to Seat Entities
		List<Seat> seatEntities = new ArrayList<>();

		for (ScreenRequestDTO.SeatRequestDTO seatDto : dto.getSeats()) {
			Seat seat = new Seat();
			seat.setRowIndex(seatDto.getRowIndex()); // Grid position
			seat.setColIndex(seatDto.getColIndex()); // Grid position

			// Convert String from DTO to your Enum
			seat.setSeatType(SeatType.valueOf(seatDto.getSeatType()));
			seat.setStatus(SeatStatus.AVAILABLE); // Default status

			// Link Seat to Screen
			seat.setScreen(screen);
			seatEntities.add(seat);
		}

		// 4. Link the list to the screen (important for CascadeType.ALL)
		screen.setSeats(seatEntities);

		// 5. Save the Screen (This will save all seats automatically due to Cascade)
		screenRepository.save(screen);
	}

	@Override
	public List<ScreenResponseDTO> getScreensByTheatre(Long theatreId) {

		return screenRepository.findByTheatreId(theatreId).stream().map(this::mapToDTO).toList();
	}

	private ScreenResponseDTO mapToDTO(Screen screen) {

		ScreenResponseDTO dto = new ScreenResponseDTO();
		dto.setId(screen.getId());
		dto.setScreenName(screen.getScreenName());
		dto.setTotalSeats(screen.getTotalSeats());
		dto.setTheatreId(screen.getTheatre().getId());
		dto.setTheatreName(screen.getTheatre().getName());

		return dto;
	}

	@Override
	public void deleteScreen(Long id) {
		screenRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void updateScreenLayout(Long screenId, ScreenRequestDTO dto) {
		Screen screen = screenRepository.findById(screenId).orElseThrow(() -> new RuntimeException("Screen not found"));

		// 1. Update basic info
		screen.setScreenName(dto.getScreenName());

		// 2. Clear old seats (OrphanRemoval will handle DB deletion)
		screen.getSeats().clear();

		// 3. Add new seats from DTO
		for (SeatRequestDTO s : dto.getSeats()) {
			Seat seat = new Seat();
			seat.setRowIndex(s.getRowIndex());
			seat.setColIndex(s.getColIndex());
			seat.setSeatType(SeatType.valueOf(s.getSeatType()));
			seat.setStatus(SeatStatus.AVAILABLE);
			seat.setScreen(screen);
			screen.getSeats().add(seat);
		}

		screenRepository.save(screen);
	}

	@Override
	public void editScreen(Long id, ScreenRequestDTO dto) {
		Screen screen = screenRepository.findById(id).orElseThrow(() -> new RuntimeException("Screen not found"));
		screen.setScreenName(dto.getScreenName());
		screen.getSeats().clear();

		dto.getSeats().forEach(s -> {
			Seat seat = new Seat();
			seat.setRowIndex(s.getRowIndex());
			seat.setColIndex(s.getColIndex());
			seat.setSeatType(SeatType.valueOf(s.getSeatType()));
			seat.setStatus(SeatStatus.AVAILABLE);
			seat.setScreen(screen);
			screen.getSeats().add(seat);
		});
		screen.setTotalSeats(dto.getSeats().size());
		screenRepository.save(screen);

	}

	@Override
	public Screen getScreenById(Long screenId) {
		Screen screen = screenRepository.findById(screenId).orElseThrow(() -> new RuntimeException("Screen Not Found"));
		screen.setSeats(seatRepository.findByScreenId(screenId));
		return screen;
	}
}
