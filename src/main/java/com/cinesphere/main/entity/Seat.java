package com.cinesphere.main.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "seats")
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String seatRow; // A, B, C
	private Integer seatNumber; // 1, 2, 3

	@Enumerated(EnumType.STRING)
	private SeatType seatType; // REGULAR, PREMIUM

	@Enumerated(EnumType.STRING)
	private SeatStatus status; // AVAILABLE, BOOKED

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "screen_id")
	private Screen screen;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSeatRow() {
		return seatRow;
	}

	public void setSeatRow(String seatRow) {
		this.seatRow = seatRow;
	}

	public Integer getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(Integer seatNumber) {
		this.seatNumber = seatNumber;
	}

	public SeatType getSeatType() {
		return seatType;
	}

	public void setSeatType(SeatType seatType) {
		this.seatType = seatType;
	}

	public SeatStatus getStatus() {
		return status;
	}

	public void setStatus(SeatStatus status) {
		this.status = status;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}
}
