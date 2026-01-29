package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.dto.SeatResponseDTO;

public interface SeatService {

	void createSeatsForScreen(Long screenId);

	List<SeatResponseDTO> getSeatsByScreen(Long screenId);
}
