package com.example.service;

import java.util.List;

import com.example.domain.OrderType;
import com.example.model.Coin;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.User;

public interface OrderService {
	Order createOrder(User user, OrderItem orderItem, OrderType orderType);
	
	Order getOrderById(Long orderId) throws Exception;
	
	List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assestSymbol);
	
	Order processOrder(Coin coin, double quatity, OrderType orderType, User user) throws Exception;
}
