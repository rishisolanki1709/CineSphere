package com.cinesphere.main.dto;

import java.math.BigDecimal;

import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.SeatType;

public class ShowSeatResponseDTO {
	private Long id;
	private Integer rowIndex;
	private Integer colIndex;
	private SeatType seatType;
	private SeatStatus status;
	private BigDecimal price;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Integer getColIndex() {
		return colIndex;
	}

	public void setColIndex(Integer colIndex) {
		this.colIndex = colIndex;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
