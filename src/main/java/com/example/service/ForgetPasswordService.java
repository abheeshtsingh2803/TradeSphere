package com.example.service;

import com.example.model.ForgetPasswordToken;
import com.example.model.User;
import com.example.model.VerificationType;

public interface ForgetPasswordService {
	ForgetPasswordToken createToken(User user, String id, String otp, 
			VerificationType verificationType, String sendTo);
	
	ForgetPasswordToken findById(String id);
	
	ForgetPasswordToken findByUser(Long userId);
	
	void deleteToken(ForgetPasswordToken token);
}
