package com.example.model;

import java.time.LocalDate;

import com.example.domain.WalletTransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class WalletTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idLong;
	
	@ManyToOne
	private Wallet wallet;
	
	private WalletTransactionType type;
	
	private LocalDate date;
	
	private String transferIdString;
	
	private String purposeString;
	
	private Long amountLong;

	public Long getIdLong() {
		return idLong;
	}

	public void setIdLong(Long idLong) {
		this.idLong = idLong;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public WalletTransactionType getType() {
		return type;
	}

	public void setType(WalletTransactionType type) {
		this.type = type;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getTransferIdString() {
		return transferIdString;
	}

	public void setTransferIdString(String transferIdString) {
		this.transferIdString = transferIdString;
	}

	public String getPurposeString() {
		return purposeString;
	}

	public void setPurposeString(String purposeString) {
		this.purposeString = purposeString;
	}

	public Long getAmountLong() {
		return amountLong;
	}

	public void setAmountLong(Long amountLong) {
		this.amountLong = amountLong;
	}
	
	
}
