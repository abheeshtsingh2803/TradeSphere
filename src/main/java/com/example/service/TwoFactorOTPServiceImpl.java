package com.example.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.model.TwoFactorOTP;
import com.example.model.User;
import com.example.repository.TwoFactorOtpRepository;

@Service
public class TwoFactorOTPServiceImpl implements TwoFactorOTPService {

	
	private TwoFactorOtpRepository twoFactorOtpRepository;
	@Override
	public TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwt) {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		
		TwoFactorOTP twoFactorOTP = new TwoFactorOTP();
		twoFactorOTP.setOtp(otp);
		twoFactorOTP.setJwt(jwt);
		twoFactorOTP.setId(id);
		twoFactorOTP.setUser(user);
		
		return twoFactorOtpRepository.save(twoFactorOTP);
	}

	@Override
	public TwoFactorOTP findByUser(Long userId) {
		return twoFactorOtpRepository.findByUserId(userId);
	}

	@Override
	public TwoFactorOTP findById(String id) {
		Optional<TwoFactorOTP> otp= twoFactorOtpRepository.findById(id);
		return otp.orElse(null);
	}

	@Override
	public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp) {
		return twoFactorOTP.getOtp().equals(otp);
	}

	@Override
	public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp) {
		twoFactorOtpRepository.delete(twoFactorOtp);
		
	}

}
