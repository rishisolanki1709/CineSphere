package com.cinesphere.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cinesphere.main.entity.Screen;

import jakarta.persistence.LockModeType;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
	List<Screen> findByTheatreId(Long theatreId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT s FROM Screen s WHERE s.id = :id")
	Optional<Screen> lockScreen(@Param("id") Long id);
}
