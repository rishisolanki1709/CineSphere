package com.cinesphere.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cinesphere.main.entity.Show;
import com.cinesphere.main.entity.ShowPricing;

import jakarta.transaction.Transactional;

import java.util.List;

public interface ShowPricingRepository extends JpaRepository<ShowPricing, Long> {
	List<ShowPricing> findByShow(Show show);

	@Modifying
	@Transactional
	@Query("DELETE FROM ShowPricing sp WHERE sp.show.id = :id")
	void deleteByShowId(@Param("id") Long id);
}
