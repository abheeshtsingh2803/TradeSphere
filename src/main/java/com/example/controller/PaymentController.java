package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.PaymentMethod;
import com.example.model.PaymentOrder;
import com.example.model.User;
import com.example.response.PaymentResponse;
import com.example.service.PaymentService;
import com.example.service.UserService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping("/api")
public class PaymentController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PaymentService paymentService;

	
	@PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
	public ResponseEntity<PaymentResponse> paymentHandler(
			@PathVariable PaymentMethod paymentMethod,
			@PathVariable Long amount,
			@RequestHeader("Authorization") String jwt) throws Exception, RazorpayException, StripeException {
		User user = userService.findUserByProfileByJwt(jwt);
		
		PaymentResponse paymentResponse;
		PaymentOrder order = paymentService.createOrder(user, amount, paymentMethod);
		
		if(paymentMethod.equals(PaymentMethod.RAZORPAY)) {
			paymentResponse = paymentService.createRazorpayPaymentLing(user, amount);
		}
		else {
			paymentResponse = paymentService.createStripePaymentLing(user, amount, order.getIdLong());
		}
		return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
	}
}
