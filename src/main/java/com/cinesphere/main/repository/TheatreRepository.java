package com.cinesphere.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinesphere.main.entity.Theatre;

public interface TheatreRepository extends JpaRepository<Theatre, Long> {
	List<Theatre> findByCity(String city);
}
