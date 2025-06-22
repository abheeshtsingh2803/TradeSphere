package com.example.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import com.example.tradesphere.TradeSphereApplication;
import com.example.utils.OtpUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.config.JwtProvider;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.response.AuthResponse;
import com.example.service.CustomUserDetailsService;
import com.example.service.EmailService;
import com.example.service.TwoFactorOTPService;
import com.example.model.TwoFactAuth;
import com.example.model.TwoFactorOTP;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final HomeController homeController;

    private final TradeSphereApplication tradeSphereApplication;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private TwoFactorOTPService twoFactorOTPService;
	
	@Autowired
	private EmailService emailService;

    AuthController(TradeSphereApplication tradeSphereApplication, HomeController homeController) {
        this.tradeSphereApplication = tradeSphereApplication;
        this.homeController = homeController;
    }
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> register(@RequestBody User users) throws Exception {
		
		User isEmailExistUser = userRepository.findByEmail(users.getEmail());
		
		if(isEmailExistUser!=null) {
			throw new Exception("Email is already used with another account");
		}
		
		User newUser = new User();
		newUser.setFullName(users.getFullName());
		newUser.setEmail(users.getEmail());
		newUser.setPassword(users.getPassword());
		newUser.setMobile(users.getMobile());
		
		User savedUser = userRepository.save(newUser);
		
		
		List<GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList("authoroties");
		
		Authentication auth = new UsernamePasswordAuthenticationToken(
				users.getEmail(),
				users.getPassword()
		);
				
		SecurityContextHolder.getContext().setAuthentication(auth);	
		
		String jwt = JwtProvider.generateToken(auth);
		
		AuthResponse res= new AuthResponse();
		res.setJwt(jwt);
		res.setStatus(true);
		res.setMessage("Register Success");
		
		
		
		return new ResponseEntity<>(res, HttpStatus.CREATED);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> login(@RequestBody User users) throws Exception {
		
		String userName = users.getEmail();
		String password= users.getPassword();
		
		Authentication auth = Authenticate(userName, password);
				
		SecurityContextHolder.getContext().setAuthentication(auth);	
		
		String jwt = JwtProvider.generateToken(auth);
		
		User authUser = userRepository.findByEmail(userName);
		
		if(users.getTwoFactorAuth().isEnabled()) {
			AuthResponse response = new AuthResponse();
			response.setMessage("Two Factor Auth is Enabled");
			response.setTwoFactorAuthEnabled(true);
			String otp = OtpUtils.generatOtp();
			
			TwoFactorOTP oldTwoFactorOTP = twoFactorOTPService.findByUser(authUser.getId());
			if(oldTwoFactorOTP != null) {
				twoFactorOTPService.deleteTwoFactorOtp(oldTwoFactorOTP);
			}
			
			TwoFactorOTP newTwoFactorOTP = twoFactorOTPService.createTwoFactorOTP(authUser, otp, jwt);
			
			emailService.sendVerficationOtpEmail(userName, otp);
			
			response.setSession(newTwoFactorOTP.getId());
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		}
		
		AuthResponse res= new AuthResponse();
		res.setJwt(jwt);
		res.setStatus(true);
		res.setMessage("Login Success");
		
		return new ResponseEntity<>(res, HttpStatus.CREATED);
	}

	private Authentication Authenticate(String userName, String password) {
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
		
		if(userName == null) {
			throw new BadCredentialsException("Invalid Username");
		}
		
		if(!password.equals(userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Password");
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}
	
	@PostMapping("/two-factor/otp/{otp}")
	public ResponseEntity<AuthResponse> verifySigningOtp(@PathVariable String otp, @RequestBody String id) throws Exception{
		TwoFactorOTP twoFactorOTP = twoFactorOTPService.findById(id);
		
		if(twoFactorOTPService.verifyTwoFactorOtp(twoFactorOTP, otp)) {
			AuthResponse response = new AuthResponse();
			response.setMessage("Two factor authentication verified");
			response.setTwoFactorAuthEnabled(true);
			response.setJwt(twoFactorOTP.getJwt());
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		throw new Exception("Invalid OTP");
	}
}
