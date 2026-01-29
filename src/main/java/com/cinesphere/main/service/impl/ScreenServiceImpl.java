package com.cinesphere.main.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.ScreenResponseDTO;
import com.cinesphere.main.entity.Screen;
import com.cinesphere.main.entity.Theatre;
import com.cinesphere.main.repository.ScreenRepository;
import com.cinesphere.main.repository.TheatreRepository;
import com.cinesphere.main.service.ScreenService;

@Service
public class ScreenServiceImpl implements ScreenService {

	private final ScreenRepository screenRepository;
	private final TheatreRepository theatreRepository;

	public ScreenServiceImpl(ScreenRepository screenRepository, TheatreRepository theatreRepository) {
		this.screenRepository = screenRepository;
		this.theatreRepository = theatreRepository;
	}

	@Override
	public ScreenResponseDTO addScreen(Long theatreId, Screen screen) {

		Theatre theatre = theatreRepository.findById(theatreId)
				.orElseThrow(() -> new RuntimeException("Theatre not found"));

		screen.setTheatre(theatre);

		Screen savedScreen = screenRepository.save(screen);

		return mapToDTO(savedScreen);
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
}
