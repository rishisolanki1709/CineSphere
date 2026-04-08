package com.cinesphere.main.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.AdminDashboardResponseDTO;
import com.cinesphere.main.dto.MovieStatsDTO;
import com.cinesphere.main.dto.RevenueDTO;
import com.cinesphere.main.dto.StatusCountDTO;
import com.cinesphere.main.entity.User;
import com.cinesphere.main.repository.*;
import com.cinesphere.main.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	private final ShowSeatRepository showSeatRepository;

	private final UserRepository userRepository;

	final private BookingRepository bookingRepository;

	public AdminServiceImpl(BookingRepository bookingRepository, UserRepository userRepository,
			ShowSeatRepository showSeatRepository) {
		this.bookingRepository = bookingRepository;
		this.userRepository = userRepository;
		this.showSeatRepository = showSeatRepository;
	}

	@Override
	public AdminDashboardResponseDTO getDashboard(String range) {
		LocalDateTime startDate = calculateStartDate(range);
		AdminDashboardResponseDTO response = new AdminDashboardResponseDTO();

		// 1. Revenue Data
		response.setRevenueData(bookingRepository.getRevenueStats(startDate).stream().map(
				row -> new RevenueDTO(row.get("date").toString(), ((Number) row.get("totalRevenue")).doubleValue()))
				.collect(Collectors.toList()));

		// 2. Status Data
		response.setStatusData(bookingRepository.getBookingStatusStats(startDate).stream()
				.map(row -> new StatusCountDTO(row.get("status").toString(), ((Number) row.get("count")).longValue()))
				.collect(Collectors.toList()));

		// 3. Movie Data (Fixed Logic)
		response.setMovieData(showSeatRepository.getTopMovies(startDate).stream()
				.map(row -> new MovieStatsDTO(row.get("movieTitle").toString(),
						((Number) row.get("ticketsSold")).longValue()))
				.collect(Collectors.toList()));

		return response;
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findByRole("ROLE_USER");
	}

	private LocalDateTime calculateStartDate(String range) {
		if (range.equals("today")) {
			return LocalDateTime.now().with(LocalTime.MIN);
		} else if (range.equals("last_week")) {
			return LocalDateTime.now().minusWeeks(1);
		} else if (range.equals("last_month")) {
			return LocalDateTime.now().minusMonths(1);
		} else if (range.equals("last_year")) {
			return LocalDateTime.now().minusYears(1);
		} else {
			return LocalDateTime.of(2000, 1, 1, 0, 0); // Overall
		}
	}
}
