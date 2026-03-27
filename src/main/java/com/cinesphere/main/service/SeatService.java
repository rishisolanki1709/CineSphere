package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.entity.Seat;


public interface SeatService {

	List<Seat> getSeatsByScreenId(Long screenId);
}
