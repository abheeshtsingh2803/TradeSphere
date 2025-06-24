package com.example.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.PaymentDetails;
import com.example.model.User;
import com.example.service.PaymentDetailsService;
import com.example.service.UserService;

@RestController
@RequestMapping("/api")
public class PaymentDetailsController {
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private PaymentDetailsService paymentDetailsService;
	
	@PostMapping("/payment-details")
	public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestBody PaymentDetails paymentDetailsRequest, @RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByProfileByJwt(jwt);
		
		PaymentDetails paymentDetails = paymentDetailsService.addPaymentDetails(
					paymentDetailsRequest.getAccountNumber(),
					paymentDetailsRequest.getAccountHolderName(),
					paymentDetailsRequest.getIfscString(),
					paymentDetailsRequest.getBankName(),
					user
				);
		return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED); 
	}
	
	@GetMapping("/payment-details")
	public ResponseEntity<PaymentDetails> getUsersPaymentDetails(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByProfileByJwt(jwt);
		
		PaymentDetails paymentDetails = paymentDetailsService.getUsersPaymentDetails(user);
		
		return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);
	}
}
