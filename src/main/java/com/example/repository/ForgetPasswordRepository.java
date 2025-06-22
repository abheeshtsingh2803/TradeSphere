package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.ForgetPasswordToken;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPasswordToken, String> {
	ForgetPasswordToken findByUser(Long userId);
}
