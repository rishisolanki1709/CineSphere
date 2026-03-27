package com.cinesphere.main.dto;

import java.util.List;

public class ScreenRequestDTO {
	private String screenName;
	private Long theatreId;
	private Integer maxRows;
	private Integer maxCols;
	private List<SeatRequestDTO> seats;

	public static class SeatRequestDTO {
		private String seatRow; // "A", "B"
		private Integer seatNumber; // 1, 2
		private Integer rowIndex; // 0 (for the UI grid)
		private Integer colIndex; // 0 (for the UI grid)
		private String seatType; // "REGULAR", "PREMIUM", "VIP"

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

		public String getSeatType() {
			return seatType;
		}

		public void setSeatType(String seatType) {
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