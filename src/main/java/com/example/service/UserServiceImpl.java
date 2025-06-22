package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.config.JwtProvider;
import com.example.model.TwoFactAuth;
import com.example.model.User;
import com.example.model.VerificationType;
import com.example.repository.UserRepository;

@RestController
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User findUserByProfileByJwt(String jwt) throws Exception {
		String email = JwtProvider.getEmailFromToken(jwt);
		User user = userRepository.findByEmail(email);
		
		if(user == null) {
			throw new Exception("User Not Found");
		}
		return user;
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		
		if(user == null) {
			throw new Exception("User Not Found");
		}
		return user;
	}

	@Override
	public User findUserById(Long userId) throws Exception {
		Optional<User> user = userRepository.findById(userId);
		if(user.isEmpty()) {
			throw new Exception("User Not Found");
		}
		return user.get();
	}

	@Override
	public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {
		TwoFactAuth twoFactAuth = new TwoFactAuth();
		twoFactAuth.setEnabled(true);
		twoFactAuth.setSendTo(verificationType);
		
		user.setTwoFactorAuth(twoFactAuth);
		
		return userRepository.save(user);
	}
	

	@Override
	public User updatePassword(User user, String newPassword) {
		user.setPassword(newPassword);
		return userRepository.save(user);
	}


}
