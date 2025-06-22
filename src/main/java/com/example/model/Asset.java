package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Asset {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idLong;
	
	private double quantity;
	private double buyPrice;
	
	@ManyToOne
	private Coin coin;
	
	@ManyToOne
	private User user;

	public Long getIdLong() {
		return idLong;
	}

	public void setIdLong(Long idLong) {
		this.idLong = idLong;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quatity) {
		this.quantity = quatity;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public Coin getCoin() {
		return coin;
	}

	public void setCoin(Coin coin) {
		this.coin = coin;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
