package com.cinesphere.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinesphere.main.entity.Screen;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
	List<Screen> findByTheatreId(Long theatreId);
}
