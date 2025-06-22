package com.example.request;

import com.example.model.VerificationType;

public class ForgetPasswordTokenRequest {
	private String sendTo;
	private VerificationType verificationType;
	
	public String getSendTo() {
		return sendTo;
	}
	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}
	
	public VerificationType getVerificationType() {
		return verificationType;
	}
	public void setVerificationType(VerificationType verificationType) {
		this.verificationType = verificationType;
	}
}
