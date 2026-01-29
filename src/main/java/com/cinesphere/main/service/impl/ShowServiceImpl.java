package com.cinesphere.main.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.ShowResponseDTO;
import com.cinesphere.main.entity.Movie;
import com.cinesphere.main.entity.Screen;
import com.cinesphere.main.entity.Show;
import com.cinesphere.main.repository.MovieRepository;
import com.cinesphere.main.repository.ScreenRepository;
import com.cinesphere.main.repository.ShowRepository;
import com.cinesphere.main.service.ShowService;

@Service
public class ShowServiceImpl implements ShowService {

	private final ShowRepository showRepository;
	private final MovieRepository movieRepository;
	private final ScreenRepository screenRepository;

	public ShowServiceImpl(ShowRepository showRepository, MovieRepository movieRepository,
			ScreenRepository screenRepository) {
		this.showRepository = showRepository;
		this.movieRepository = movieRepository;
		this.screenRepository = screenRepository;
	}

	@Override
	public ShowResponseDTO createShow(Long movieId, Long screenId, Show show) {

		Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));

		Screen screen = screenRepository.findById(screenId).orElseThrow(() -> new RuntimeException("Screen not found"));

		show.setMovie(movie);
		show.setScreen(screen);

		Show savedShow = showRepository.save(show);

		return mapToDTO(savedShow);
	}

	@Override
	public List<ShowResponseDTO> getShowsByScreen(Long screenId) {

		return showRepository.findByScreenId(screenId).stream().map(this::mapToDTO).toList();
	}

	private ShowResponseDTO mapToDTO(Show show) {

		ShowResponseDTO dto = new ShowResponseDTO();
		dto.setShowId(show.getId());
		dto.setMovieTitle(show.getMovie().getTitle());
		dto.setScreenName(show.getScreen().getScreenName());
		dto.setStartTime(show.getStartTime());
		dto.setEndTime(show.getEndTime());
		dto.setPrice(show.getPrice());

		return dto;
	}
}
