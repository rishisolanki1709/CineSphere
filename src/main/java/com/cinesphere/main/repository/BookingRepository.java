package com.cinesphere.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinesphere.main.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
