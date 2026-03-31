package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.dto.ShowRequestDTO;
import com.cinesphere.main.dto.ShowResponseDTO;
import com.cinesphere.main.dto.ShowSeatResponseDTO;

public interface ShowService {

	ShowResponseDTO createShow(ShowRequestDTO dto);

	List<ShowResponseDTO> getShowsByScreenId(Long screenId);

	List<ShowResponseDTO> getShowsByMoiveIdAndCity(Long movieId, String city);

	List<ShowSeatResponseDTO> getShowSeatsByShowId(Long showId);

	ShowResponseDTO getShowbyId(Long showId);

	List<ShowResponseDTO> getAllShows();

	void deleteShowById(Long id);
}
