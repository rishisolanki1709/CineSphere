package com.cinesphere.main.dto;

import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.SeatType;

public class SeatResponseDTO {

	private Long id;
	private String seatRow;
	private Integer seatNumber;
	private SeatType seatType;
	private SeatStatus status;

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

}
