package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.dto.ScreenRequestDTO;
import com.cinesphere.main.dto.ScreenResponseDTO;

public interface ScreenService {

	void addScreen(ScreenRequestDTO dto);

	List<ScreenResponseDTO> getScreensByTheatre(Long theatreId);

	void deleteScreen(Long id);
}
