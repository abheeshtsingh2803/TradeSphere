package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.PaymentDetails;

public interface PaymentDetailsRepository	extends JpaRepository<PaymentDetails, Long> {
	PaymentDetails findByUserId(Long userId);
}
