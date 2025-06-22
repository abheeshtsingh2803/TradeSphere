package com.example.request;

import com.example.domain.OrderType;

import lombok.Data;

@Data
public class CreateOrderRequest {
	private String coinIdString;
	private double quantity;
	private OrderType orderType;
	
	public String getCoinIdString() {
		return coinIdString;
	}
	public void setCoinIdString(String coinIdString) {
		this.coinIdString = coinIdString;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public OrderType getOrderType() {
		return orderType;
	}
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	
}
