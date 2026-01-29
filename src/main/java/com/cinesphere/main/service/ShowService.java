package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.dto.ShowResponseDTO;
import com.cinesphere.main.entity.Show;

public interface ShowService {

	ShowResponseDTO createShow(Long movieId, Long screenId, Show show);

	List<ShowResponseDTO> getShowsByScreen(Long screenId);
}
