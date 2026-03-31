package com.cinesphere.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.ShowRequestDTO;
import com.cinesphere.main.dto.ShowResponseDTO;
import com.cinesphere.main.dto.ShowSeatResponseDTO;
import com.cinesphere.main.entity.Movie;
import com.cinesphere.main.entity.Screen;
import com.cinesphere.main.entity.Seat;
import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.SeatType;
import com.cinesphere.main.entity.Show;
import com.cinesphere.main.entity.ShowPricing;
import com.cinesphere.main.entity.ShowSeat;
import com.cinesphere.main.repository.MovieRepository;
import com.cinesphere.main.repository.ScreenRepository;
import com.cinesphere.main.repository.ShowPricingRepository;
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
	private final ShowPricingRepository pricingRepository;

	public ShowServiceImpl(ShowRepository showRepository, MovieRepository movieRepository,
			ScreenRepository screenRepository, ShowSeatRepository showSeatRepository,
			ShowPricingRepository showPricing) {
		this.showRepository = showRepository;
		this.movieRepository = movieRepository;
		this.screenRepository = screenRepository;
		this.showSeatRepository = showSeatRepository;
		this.pricingRepository = showPricing;
	}

	@Override
	@Transactional
	public ShowResponseDTO createShow(ShowRequestDTO dto) {

		Movie movie = movieRepository.findById(dto.getMovieId())
				.orElseThrow(() -> new RuntimeException("Movie Not Found"));

		// Lock screen (prevents concurrent overlapping shows)
		Screen screen = screenRepository.lockScreen(dto.getScreenId())
				.orElseThrow(() -> new RuntimeException("Screen Not Found"));

//		 Overlap check

		List<Show> overlappingShows = showRepository.findOverlappingShows(dto.getScreenId(), dto.getStartTime(),
				dto.getStartTime().plusMinutes(movie.getDurationMinutes()));

		if (!overlappingShows.isEmpty()) {
			throw new RuntimeException("Show time overlaps with an existing show on this screen");
		}

		Show show = new Show();
		show.setStartTime(dto.getStartTime());
		show.setMovie(movie);
		show.setScreen(screen);
		show.setEndTime(dto.getStartTime().plusMinutes(movie.getDurationMinutes()));

		// Save show first
		Show savedShow = showRepository.save(show);

		ShowPricing sp1 = new ShowPricing();
		sp1.setPrice(dto.getPremiumPrice());
		sp1.setSeatType(SeatType.PREMIUM);
		sp1.setShow(savedShow);

		ShowPricing sp2 = new ShowPricing();
		sp2.setPrice(dto.getRegularPrice());
		sp2.setSeatType(SeatType.REGULAR);
		sp2.setShow(savedShow);

		ShowPricing sp3 = new ShowPricing();
		sp3.setPrice(dto.getVipPrice());
		sp3.setSeatType(SeatType.VIP);
		sp3.setShow(savedShow);

		List<Seat> seats = screen.getSeats();
		if (seats == null || seats.isEmpty()) {
			throw new RuntimeException("No seats found for this screen");
		}

		// Create show seats
		List<ShowSeat> showSeats = new ArrayList<>();

		for (Seat seat : seats) {
			ShowSeat ss = new ShowSeat();
			ss.setShow(savedShow);
			ss.setSeat(seat);
			ss.setStatus(SeatStatus.AVAILABLE);
			ss.setLockedAt(null);
			// Assign price based on the seat's category
			if (seat.getSeatType() == SeatType.VIP)
				ss.setPrice(dto.getVipPrice());
			else if (seat.getSeatType() == SeatType.PREMIUM)
				ss.setPrice(dto.getPremiumPrice());
			else
				ss.setPrice(dto.getRegularPrice());

			showSeats.add(ss);
			showSeats.add(ss);
		}

		pricingRepository.saveAll(List.of(sp1, sp2, sp3));

		// Attach to parent (🔥 MOST IMPORTANT)
		savedShow.setShowSeats(showSeats);

		// Save ONLY parent (cascade handles children)
		showRepository.save(savedShow);

		return mapToDTO(savedShow);
	}

	private ShowResponseDTO mapToDTO(Show show) {

		ShowResponseDTO dto = new ShowResponseDTO();
		dto.setShowId(show.getId());
		dto.setMovieTitle(show.getMovie().getTitle());
		dto.setScreenName(show.getScreen().getScreenName());
		dto.setStartTime(show.getStartTime());
		dto.setEndTime(show.getEndTime());
		dto.setTheatreName(show.getScreen().getTheatre().getName());
		List<ShowPricing> listSP = pricingRepository.findByShow(show);
		listSP.forEach((p) -> {
			if (p.getSeatType() == SeatType.VIP)
				dto.setVIP_price(p.getPrice());
			else if (p.getSeatType() == SeatType.PREMIUM)
				dto.setPREMIUM_price(p.getPrice());
			else
				dto.setREGULAR_price(p.getPrice());
		});
		return dto;
	}

	@Override
	public List<ShowResponseDTO> getAllShows() {
		List<Show> shows = showRepository.findAllActiveShows();
		return shows.stream().map(this::mapToDTO).toList();
	}

	@Override
	public List<ShowResponseDTO> getShowsByScreenId(Long screenId) {
		return showRepository.findByScreenId(screenId).stream().map(this::mapToDTO).toList();
	}

	@Override
	public List<ShowResponseDTO> getShowsByMoiveIdAndCity(Long movieId, String city) {

		return showRepository.findAvailableShows(movieId, city).stream().map(this::mapToDTO).toList();
	}

	@Override
	public List<ShowSeatResponseDTO> getShowSeatsByShowId(Long showId) {

		return showSeatRepository.findByShowId(showId).stream().map(this::mapToDTO).toList();
	}

	private ShowSeatResponseDTO mapToDTO(ShowSeat showSeat) {
		ShowSeatResponseDTO dto = new ShowSeatResponseDTO();
		dto.setColIndex(showSeat.getSeat().getColIndex());
		dto.setId(showSeat.getId());
		dto.setPrice(showSeat.getPrice());
		dto.setRowIndex(showSeat.getSeat().getRowIndex());
		dto.setSeatType(showSeat.getSeat().getSeatType());
		dto.setStatus(showSeat.getStatus());
		return dto;
	}

	@Override
	public ShowResponseDTO getShowbyId(Long showId) {
		Show show = showRepository.findById(showId).orElseThrow(() -> new RuntimeException("Show Not Found"));
		return mapToDTO(show);

	}

	@Override
	public void deleteShowById(Long id) {
		Show show = showRepository.findById(id).orElseThrow(() -> new RuntimeException("Show not found"));

		// 2. If you don't have CascadeType.ALL, you'd have to do this:
		pricingRepository.deleteByShowId(id);
		showSeatRepository.deleteByShowId(id);

		// 3. Delete the show (With CascadeType.ALL, this handles 1 & 2 automatically)
		showRepository.delete(show);
	}
}
