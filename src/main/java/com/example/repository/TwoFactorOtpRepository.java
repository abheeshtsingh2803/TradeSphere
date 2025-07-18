package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.TwoFactorOTP;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String> {
	TwoFactorOTP findByUserId(Long userId);
}
