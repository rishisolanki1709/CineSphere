package com.cinesphere.main.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.cinesphere.main.entity.BookingStatus;
import com.cinesphere.main.entity.Seat;
import com.cinesphere.main.entity.ShowSeat;

public class BookingResponseDTO {
	private Long bookingId;
	private Long showId;
	private BigDecimal totalAmount;
	private BookingStatus status;
	private List<ShowSeat> seats;
	private LocalDateTime bookedAt;
	private String movieName;
	private String theatreName;
	private String screenName;
	private LocalDateTime showTime;

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal double1) {
		this.totalAmount = double1;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public List<ShowSeat> getSeats() {
		return seats;
	}

	public void setSeats(List<ShowSeat> seats) {
		this.seats = seats;
	}

	public LocalDateTime getBookedAt() {
		return bookedAt;
	}

	public void setBookedAt(LocalDateTime bookedAt) {
		this.bookedAt = bookedAt;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public LocalDateTime getShowTime() {
		return showTime;
	}

	public void setShowTime(LocalDateTime showTime) {
		this.showTime = showTime;
	}

}
