package com.example.service;

import com.example.model.User;
import com.example.model.VerificationCode;
import com.example.model.VerificationType;

public interface VerificationCodeService {
	VerificationCode sendVerificationCode (User user, VerificationType verificationType);
	
	VerificationCode getVerificationCodeById(Long id) throws Exception;
	
	VerificationCode getVerificationByUser(Long userId);
	
	void deleteVerificationCodeById(VerificationCode verificationCode);
}
