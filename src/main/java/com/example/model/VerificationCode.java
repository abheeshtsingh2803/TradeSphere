package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class VerificationCode {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idLong;
	
	private String otp;
	
	@OneToOne
	private User user;
	
	
	private String email;
	
	private String mobile;
	
	private VerificationType verificationType;

	public Long getIdLong() {
		return idLong;
	}

	public void setIdLong(Long idLong) {
		this.idLong = idLong;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public VerificationType getVerificationType() {
		return verificationType;
	}

	public void setVerificationType(VerificationType verificationType) {
		this.verificationType = verificationType;
	}
	
	
}
