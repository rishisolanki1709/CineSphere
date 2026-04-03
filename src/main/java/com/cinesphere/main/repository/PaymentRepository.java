package com.cinesphere.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinesphere.main.entity.Payment;
import com.cinesphere.main.entity.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Payment findByBookingId(Long bookingId);

	List<Payment> findByOrderId(String orderId);

	List<Payment> findAllByStatusAndCreatedAtBefore(PaymentStatus status, LocalDateTime dateTime);
}
