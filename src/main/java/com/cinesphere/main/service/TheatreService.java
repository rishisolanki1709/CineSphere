package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.dto.TheatreRequestDTO;
import com.cinesphere.main.dto.TheatreResponseDTO;

import jakarta.validation.Valid;

public interface TheatreService {
	void addTheatre(@Valid TheatreRequestDTO theatre);

	List<TheatreResponseDTO> getTheatresByCity(String city);

	List<TheatreResponseDTO> getAllTheatres();

	void updateStatus(Long id, boolean newStatus);

	void deleteTheatre(Long id);

	void updateTheatre(Long id, @Valid TheatreRequestDTO theatre);

	TheatreResponseDTO getTheatresById(Long id);

}
