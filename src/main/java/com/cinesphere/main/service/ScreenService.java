package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.dto.ScreenRequestDTO;
import com.cinesphere.main.dto.ScreenResponseDTO;

public interface ScreenService {

	void addScreen(ScreenRequestDTO dto);

	List<ScreenResponseDTO> getScreensByTheatre(Long theatreId);

	void deleteScreen(Long id);

	public void updateScreenLayout(Long screenId, ScreenRequestDTO dto);

	void editScreen(Long id, ScreenRequestDTO dto);

	ScreenResponseDTO getScreenById(Long screenId);
}
