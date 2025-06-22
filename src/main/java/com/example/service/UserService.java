package com.example.service;

import com.example.model.User;
import com.example.model.VerificationType;

public interface UserService {
	public User findUserByProfileByJwt(String jwt) throws Exception;
	public User findUserByEmail(String email) throws Exception;
	public User findUserById(Long userId) throws Exception;
	
	public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user);
	
	User updatePassword(User user, String newPassword);
	
}
