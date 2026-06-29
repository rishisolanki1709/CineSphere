package com.cinesphere.main.dto;

import java.util.List;

import com.cinesphere.main.entity.SeatType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ScreenRequestDTO {
	@NotBlank(message = "Screen name is required")
	private String screenName;

	@NotNull(message = "Theatre ID is required")
	@Positive(message = "Invalid theatre ID")
	private Long theatreId;

	private Integer maxRows;

	private Integer maxCols;

	@NotEmpty(message = "Seat layout cannot be empty")
	@Valid
	private List<SeatRequestDTO> seats;

	public static class SeatRequestDTO {
		@NotBlank(message = "Seat row is required")
		@Pattern(regexp = "^[A-Z]+$", message = "Seat row must contain only uppercase letters")
		private String seatRow;

		@NotNull
		@Positive
		private Integer seatNumber;

		@PositiveOrZero
		private Integer rowIndex;

		@PositiveOrZero
		private Integer colIndex;

		@NotNull
		private SeatType seatType; // "REGULAR", "PREMIUM", "VIP"

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

	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Long getTheatreId() {
		return theatreId;
	}

	public void setTheatreId(Long theatreId) {
		this.theatreId = theatreId;
	}

	public Integer getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}

	public Integer getMaxCols() {
		return maxCols;
	}

	public void setMaxCols(Integer maxCols) {
		this.maxCols = maxCols;
	}

	public List<SeatRequestDTO> getSeats() {
		return seats;
	}

	public void setSeats(List<SeatRequestDTO> seats) {
		this.seats = seats;
	}

}