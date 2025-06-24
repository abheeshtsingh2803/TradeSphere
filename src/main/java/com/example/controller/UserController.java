package com.example.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.ForgetPasswordToken;
import com.example.model.User;
import com.example.model.VerificationCode;
import com.example.model.VerificationType;
import com.example.request.ForgetPasswordTokenRequest;
import com.example.request.ResetPasswordRequest;
import com.example.response.ApiResponse;
import com.example.response.AuthResponse;
import com.example.service.EmailService;
import com.example.service.ForgetPasswordService;
import com.example.service.UserService;
import com.example.service.VerificationCodeService;
import com.example.utils.OtpUtils;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private VerificationCodeService verificationCodeService;
	
	@Autowired
	private EmailService emailService;
	private String jwt;
	
	@Autowired
	private ForgetPasswordService forgetPasswordService;
	
	@GetMapping("/api/users/profile")
	public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByProfileByJwt(jwt);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PostMapping("/api/users/verification/{verificationType}/send-otp")
	public ResponseEntity<String> sendVerificationOtp(
			@RequestHeader("Authorization") String jwt,
			@PathVariable VerificationType verificationType) throws Exception {
		User user = userService.findUserByProfileByJwt(jwt);
		
		VerificationCode verificationCode = verificationCodeService.getVerificationByUser(user.getId());
		if(verificationCode == null) {
			verificationCode=verificationCodeService.sendVerificationCode(user, verificationType);
		}
		
		if(verificationType.equals(VerificationType.EMAIL)) {
			emailService.sendVerficationOtpEmail(user.getEmail(), verificationCode.getOtp());
		}
		
		return new ResponseEntity<>("Verification Code Send Successfully", HttpStatus.OK);
	}
	
	@PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
	public ResponseEntity<User> enableTwoFactorAuthentication(
			@PathVariable String otp,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByProfileByJwt(jwt);
		
		VerificationCode verificationCode = verificationCodeService.getVerificationByUser(user.getId());
		
		String sendTo = verificationCode.getVerificationType()
				.equals(VerificationType.EMAIL)?verificationCode.getEmail():verificationCode.getMobile();
		
		boolean isVerified = verificationCode.getOtp().equals(otp);
		
		if(isVerified) {
			User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
		
			verificationCodeService.deleteVerificationCodeById(verificationCode);
			return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
		}
		
		throw new Exception("Wrong OTP");
	}
	
	@PostMapping("/auth/users/reset-password/send-otp")
	public ResponseEntity<AuthResponse> sendForgetPasswordOtp(@RequestBody ForgetPasswordTokenRequest req) throws Exception {
		User user = userService.findUserByEmail(req.getSendTo());
		
		String otp=OtpUtils.generatOtp();
		UUID uuid = UUID.randomUUID();
		String idString = uuid.toString();
		
		ForgetPasswordToken token = forgetPasswordService.findByUser(user.getId());
		
		if(token == null) {
			token=forgetPasswordService.createToken(user, idString, otp, req.getVerificationType(), req.getSendTo());
		}
		
		if(req.getVerificationType().equals(VerificationType.EMAIL)) {
			emailService.sendVerficationOtpEmail(user.getEmail(), token.getOtp());
		}
		
		AuthResponse response = new AuthResponse();
		response.setSession(token.getId());
		response.setMessage("Password reset OTP send successfully");
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/auth/users/reset-password/verify-otp")
	public ResponseEntity<ApiResponse> resetPassword(
			@RequestPart String id,
			@RequestBody ResetPasswordRequest req,
			@RequestHeader("Authorization") String jwt) throws Exception {
		
		ForgetPasswordToken forgetPasswordToken = forgetPasswordService.findById(id);
		
		boolean isVerified = forgetPasswordToken.getOtp().equals(req.getOtp());
		
		if(isVerified) {
			userService.updatePassword(forgetPasswordToken.getUser(), req.getPassword());
			ApiResponse response = new ApiResponse();
			response.setMessage("Password Updated Successfully");
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		}
		throw new Exception("Wrong OTP");
	}
}
