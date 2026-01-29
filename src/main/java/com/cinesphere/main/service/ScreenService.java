package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.dto.ScreenResponseDTO;
import com.cinesphere.main.entity.Screen;

public interface ScreenService {

	ScreenResponseDTO addScreen(Long theatreId, Screen screen);

	List<ScreenResponseDTO> getScreensByTheatre(Long theatreId);
}
