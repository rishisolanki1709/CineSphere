package com.cinesphere.main.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.ShowResponseDTO;
import com.cinesphere.main.dto.ShowSeatResponseDTO;
import com.cinesphere.main.entity.Movie;
import com.cinesphere.main.entity.Screen;
import com.cinesphere.main.entity.Seat;
import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.Show;
import com.cinesphere.main.entity.ShowSeat;
import com.cinesphere.main.repository.MovieRepository;
import com.cinesphere.main.repository.ScreenRepository;
import com.cinesphere.main.repository.ShowRepository;
import com.cinesphere.main.repository.ShowSeatRepository;
import com.cinesphere.main.service.ShowService;

import jakarta.transaction.Transactional;

@Service
public class ShowServiceImpl implements ShowService {

	private final ShowRepository showRepository;
	private final MovieRepository movieRepository;
	private final ScreenRepository screenRepository;
	private final ShowSeatRepository showSeatRepository;

	public ShowServiceImpl(ShowRepository showRepository, MovieRepository movieRepository,
			ScreenRepository screenRepository, ShowSeatRepository showSeatRepository) {
		this.showRepository = showRepository;
		this.movieRepository = movieRepository;
		this.screenRepository = screenRepository;
		this.showSeatRepository = showSeatRepository;
	}

	@Override
	@Transactional
	public ShowResponseDTO createShow(Long movieId, Long screenId, Show show) {
		Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));

		Screen screen = screenRepository.findById(screenId).orElseThrow(() -> new RuntimeException("Screen not found"));

		show.setMovie(movie);
		show.setScreen(screen);

		Show savedShow = showRepository.save(show);

		List<Seat> seats = screen.getSeats();

		if (seats == null || seats.isEmpty()) {
			throw new RuntimeException("No seats found for this screen");
		}

		List<ShowSeat> showSeats = seats.stream().map(seat -> {
			ShowSeat ss = new ShowSeat();
			ss.setShow(savedShow);
			ss.setSeat(seat);
			ss.setStatus(SeatStatus.AVAILABLE);
			ss.setLockedAt(null);
			return ss;
		}).toList();

		showSeatRepository.saveAll(showSeats);
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

	@Override
	public List<ShowResponseDTO> getShows(Long movieId, String city) {

		return showRepository.findAvailableShows(movieId, city).stream().map(this::mapToDTO).toList();
	}

	@Override
	public List<ShowSeatResponseDTO> getShowSeats(Long showId) {

		return showSeatRepository.findByShowId(showId).stream().map(seat -> {
			ShowSeatResponseDTO dto = new ShowSeatResponseDTO();
			dto.setId(seat.getId());
			dto.setSeatRow(seat.getSeat().getSeatRow());
			dto.setSeatNumber(seat.getSeat().getSeatNumber());
			dto.setStatus(seat.getStatus());
			return dto;
		}).toList();
	}
}
