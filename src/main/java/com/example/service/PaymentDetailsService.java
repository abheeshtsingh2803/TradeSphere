package com.example.service;


import com.example.model.PaymentDetails;
import com.example.model.User;

public interface PaymentDetailsService {
	public PaymentDetails addPaymentDetails(
					String accountNumber,
					String accountHolderName,
					String ifsc,
					String bankName,
					User user
				);
	
	public PaymentDetails getUsersPaymentDetails(User user);
}
