package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody User users) {
		User newUser = new User();
		newUser.setFullName(users.getFullName());
		newUser.setEmail(users.getEmail());
		newUser.setPassword(users.getPassword());
		newUser.setMobile(users.getMobile());
		
		User savedUser = userRepository.save(newUser);
		
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}
}
