package com.cinesphere.main.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.cinesphere.main.entity.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO {

	private Long bookingId;
	private String movieName;
	private String theatreName;
	private String screenName;
	private LocalDateTime showTime;

	private List<String> seats;
	private Double amount;
	private BookingStatus status;

	private LocalDateTime bookedAt;
}
