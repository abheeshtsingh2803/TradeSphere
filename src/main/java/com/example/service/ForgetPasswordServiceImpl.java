package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperatorExtensionsKt;

import com.example.model.ForgetPasswordToken;
import com.example.model.User;
import com.example.model.VerificationType;
import com.example.repository.ForgetPasswordRepository;

@Service
public class ForgetPasswordServiceImpl implements ForgetPasswordService{
	@Autowired
	private ForgetPasswordRepository forgetPasswordRepository;

	@Override
	public ForgetPasswordToken createToken(User user, String id, String otp, VerificationType verificationType,
			String sendTo) {
		ForgetPasswordToken token = new ForgetPasswordToken();
		
		token.setUser(user);
		token.setId(id);
		token.setOtp(otp);
		token.setVerificationType(verificationType);
		token.setSendTo(sendTo);
		return forgetPasswordRepository.save(token);
	}

	@Override
	public ForgetPasswordToken findById(String id) {
		Optional<ForgetPasswordToken> token = forgetPasswordRepository.findById(id);
		return token.orElse(null);
	}

	@Override
	public ForgetPasswordToken findByUser(Long userId) {
		return forgetPasswordRepository.findByUserId(userId);
	}

	@Override
	public void deleteToken(ForgetPasswordToken token) {
		forgetPasswordRepository.delete(token);
		
	}
	
	
}
