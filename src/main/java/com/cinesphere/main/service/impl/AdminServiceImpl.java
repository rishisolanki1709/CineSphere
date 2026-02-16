package com.cinesphere.main.service.impl;

import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.AdminDashboardDTO;
import com.cinesphere.main.repository.BookingRepository;
import com.cinesphere.main.repository.ShowRepository;
import com.cinesphere.main.repository.ShowSeatRepository;
import com.cinesphere.main.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	final private BookingRepository bookingRepository;
	final private ShowRepository showRepository;
	final private ShowSeatRepository showSeatRepository;

	public AdminServiceImpl(BookingRepository bookingRepository, ShowRepository showRepository,
			ShowSeatRepository showSeatRepository) {
		this.bookingRepository = bookingRepository;
		this.showRepository = showRepository;
		this.showSeatRepository = showSeatRepository;
	}

	@Override
	public AdminDashboardDTO getDashboard() {
		AdminDashboardDTO dto = new AdminDashboardDTO();

		dto.setTotalBookings(bookingRepository.totalBookings());

		Double revenue = bookingRepository.totalRevenue();
		dto.setTotalRevenue(revenue == null ? 0 : revenue);

		dto.setActiveShows(showRepository.activeShows());

		dto.setSeatsBookedToday(bookingRepository.bookingsToday());

		long totalSeats = showSeatRepository.totalSeats();
		long bookedSeats = showSeatRepository.bookedSeats();

		double occupancy = totalSeats == 0 ? 0 : ((double) bookedSeats / totalSeats) * 100;

		dto.setOccupancyRate(occupancy);

		return dto;

	}

}
