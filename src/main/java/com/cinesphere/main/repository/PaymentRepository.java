package com.cinesphere.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinesphere.main.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Payment findByBookingId(Long bookingId);
}
