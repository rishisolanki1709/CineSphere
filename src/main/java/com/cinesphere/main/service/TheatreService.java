package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.dto.TheatreResponseDTO;
import com.cinesphere.main.entity.Theatre;

public interface TheatreService {
	TheatreResponseDTO addTheatre(Theatre theatre);

	List<TheatreResponseDTO> getTheatresByCity(String city);

	List<TheatreResponseDTO> getAllTheatres();

}
