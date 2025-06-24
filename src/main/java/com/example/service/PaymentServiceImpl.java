package com.example.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.controller.PaymentDetailsController;
import com.example.domain.PaymentMethod;
import com.example.domain.PaymentOrderStatus;
import com.example.model.PaymentOrder;
import com.example.model.User;
import com.example.repository.PaymentOrderRepository;
import com.example.response.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentOrderRepository paymentOrderRepository;
	
	@Value("${stripe.api.key}")
	private String stripeSecretKey;
	
	@Value("${razorpay.api.key}")
	private String apiKey;
	
	@Value("${razorpay.api.secret}")
	private String apiSecretKey;
	
	@Override
	public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {
		
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setUser(user);
		paymentOrder.setAmountLong(amount);
		paymentOrder.setPaymentMethod(paymentMethod);
		
		return paymentOrderRepository.save(paymentOrder);
	}

	@Override
	public PaymentOrder getPaymentOrderById(Long id) throws Exception {
		return paymentOrderRepository.findById(id).orElseThrow(() -> new Exception("Payment order not found"));
	}

	@Override
	public Boolean ProccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
		if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING) ) {
			if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
				RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecretKey);
				Payment payment = razorpayClient.payments.fetch(paymentId);
				
				Integer amount = payment.get("amount");
				String status = payment.get("status");
				
				if(status.equals("captured")) {
					paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
					return true;
				}
				
				paymentOrder.setStatus(PaymentOrderStatus.FAILED);
				paymentOrderRepository.save(paymentOrder);
				
				return false;
			}
			paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
			paymentOrderRepository.save(paymentOrder);
			return true;
		}
		return false;
	}

	@Override
	public PaymentResponse createRazorpayPaymentLing(User user, Long amount) throws RazorpayException {
		
		Long Amount = amount * 100;
		try {
			// Instantiate a Razorpay Client with your key ID and user
			RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecretKey);
			
			// Create a JSON object with the payment link request paramenter
			JSONObject paymentLinkRequest = new JSONObject();
			paymentLinkRequest.put("amount", Amount);
			paymentLinkRequest.put("currency", "INR");
			
			// Create a JSON object with the customer details
			JSONObject customer = new JSONObject();
			customer.put("name", user.getFullName());
			
			customer.put("email", user.getEmail());
			paymentLinkRequest.put("customer", customer);
			
			// Create a JSON object with the notification settings
			JSONObject notify = new JSONObject();
			notify.put("email", true);
			paymentLinkRequest.put("notify", notify);
			
			// Set the reminder settings
			paymentLinkRequest.put("reminder_enable", true);
			
			// Set the callback URL and method
			paymentLinkRequest.put("callback_url", "http://localhost:5173/wallet");
			paymentLinkRequest.put("callback_method", "get");
			
			// Create the payment link using the paymentLink.create() method
			PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
			
			String paymentLinkId= payment.get("id");
			String paymentLinkUrl = payment.get("short_url");
			
			PaymentResponse response = new PaymentResponse();
			response.setPayment_url(paymentLinkUrl);
			
			return response;
		} catch(RazorpayException e) {
			System.out.println("Error creating payment link: "+e.getMessage());
			throw new RazorpayException(e.getMessage());
		}
	}

	@Override
	public PaymentResponse createStripePaymentLing(User user, Long amount, Long orderId) throws StripeException {
		Stripe.apiKey = stripeSecretKey;
		
		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:5173/wallet?order_id="+orderId)
				.setCancelUrl("http://localhost:5173/payment/cancel")
				.addLineItem(SessionCreateParams.LineItem.builder()
						.setQuantity(1L)
						.setPriceData(SessionCreateParams.LineItem.PriceData.builder()
								.setCurrency("usd")
								.setUnitAmount(amount*100)
								.setProductData(SessionCreateParams
										.LineItem
										.PriceData
										.ProductData
										.builder()
										.setName("Top up wallet")
										.build()
								).build()
						).build()
				).build();
		
		Session session = Session.create(params);
		System.out.println("Session ______ " + session);
		
		PaymentResponse response = new PaymentResponse();
		response.setPayment_url(session.getUrl());
		
		return response;
		
	}

}
