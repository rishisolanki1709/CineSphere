package com.cinesphere.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinesphere.main.entity.Show;

public interface ShowRepository extends JpaRepository<Show, Long> {

	List<Show> findByScreenId(Long screenId);
}
