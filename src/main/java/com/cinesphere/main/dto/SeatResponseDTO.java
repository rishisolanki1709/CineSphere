package com.cinesphere.main.dto;

import com.cinesphere.main.entity.SeatStatus;
import com.cinesphere.main.entity.SeatType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatResponseDTO {

	private Long id;

	private Integer rowIndex;

	private Integer colIndex;

	private SeatType seatType;

	private SeatStatus status;

	private Long screenId;
}