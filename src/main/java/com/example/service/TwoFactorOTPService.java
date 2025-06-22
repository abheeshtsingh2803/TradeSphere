package com.example.service;

import com.example.model.TwoFactorOTP;
import com.example.model.User;

public interface TwoFactorOTPService {
	TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwt);
	
	TwoFactorOTP findByUser(Long userId);
	
	TwoFactorOTP findById(String id);
	
	boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp);
	
	void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);
}
