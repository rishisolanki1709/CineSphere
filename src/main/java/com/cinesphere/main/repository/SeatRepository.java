package com.cinesphere.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinesphere.main.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {

	List<Seat> findByScreenId(Long screenId);
}
