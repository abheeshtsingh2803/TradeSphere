package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.model.VerificationCode;
import com.example.model.VerificationType;
import com.example.repository.VerificationCodeRepository;
import com.example.utils.OtpUtils;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

	@Autowired
	private VerificationCodeRepository verificationCodeRepository;
	
	@Override
	public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
		VerificationCode verificationCode1 = new VerificationCode();
		verificationCode1.setOtp(OtpUtils.generatOtp());
		verificationCode1.setVerificationType(verificationType);
		
		return verificationCodeRepository.save(verificationCode1);
	}

	@Override
	public VerificationCode getVerificationCodeById(Long id) throws Exception {
		Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(id);
		if(verificationCode.isPresent()) {
			return verificationCode.get();
		}
		throw new Exception("Verification Code Not Found");
	}

	@Override
	public VerificationCode getVerificationByUser(Long userId) {
		return verificationCodeRepository.findByUserId(userId);
	}

	@Override
	public void deleteVerificationCodeById(VerificationCode verificationCode) {
		verificationCodeRepository.delete(verificationCode);
	}
	
}
