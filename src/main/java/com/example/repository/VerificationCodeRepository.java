package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long>{
	public VerificationCode findByUserId(Long userId);
}
