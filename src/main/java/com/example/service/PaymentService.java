package com.example.service;

import com.example.domain.PaymentMethod;
import com.example.model.PaymentOrder;
import com.example.model.User;
import com.example.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {
	PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);
	
	PaymentOrder getPaymentOrderById(Long id) throws Exception;
	
	Boolean ProccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;
	
	PaymentResponse createRazorpayPaymentLing(User user, Long amount) throws RazorpayException;
	
	PaymentResponse createStripePaymentLing(User user, Long amount, Long orderId) throws StripeException;
}
