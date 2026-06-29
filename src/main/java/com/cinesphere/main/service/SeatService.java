package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.dto.SeatResponseDTO;

public interface SeatService {

	List<SeatResponseDTO> getSeatsByScreenId(Long screenId);
}
