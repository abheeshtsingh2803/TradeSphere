package com.example.controller;

import java.util.List;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.OrderType;
import com.example.model.Coin;
import com.example.model.Order;
import com.example.model.User;
import com.example.request.CreateOrderRequest;
import com.example.service.CoinService;
import com.example.service.OrderService;
import com.example.service.UserService;
import com.example.service.WalletService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CoinService coinService;
	
//	private WalletTransactionService walletTransactionService
	
	@PostMapping("/pay")
	public ResponseEntity<Order> payOrderPayment(
			@RequestHeader("Authorization") String jwt,
			@RequestBody CreateOrderRequest req) throws Exception {
		User user = userService.findUserByProfileByJwt(jwt);
		Coin coin = coinService.findById(req.getCoinIdString());
		
		Order order = orderService.processOrder(coin, req.getQuantity(), req.getOrderType(), user);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(
			@RequestHeader("Authorization") String jwtToken,
			@PathVariable Long orderIdLong
		) throws Exception { 
		
		User user = userService.findUserByProfileByJwt(jwtToken);
		
		Order order = orderService.getOrderById(orderIdLong);
		if(order.getUser().getId().equals(user.getId())) {
			return ResponseEntity.ok(order);
		} else {
			throw new Exception("You don't have Access");
		}
	}

	@GetMapping()
	public ResponseEntity<List<Order>> getAllOrdersForUser(
			@RequestHeader("Authorization") String jwt,
			@RequestParam(required = false) OrderType orderType,
			@RequestParam(required = false) String asset_symbol
		) throws Exception {
		Long userIdLong=userService.findUserByProfileByJwt(jwt).getId();
		List<Order> userOrders=orderService.getAllOrdersOfUser(userIdLong, orderType, asset_symbol);
		return ResponseEntity.ok(userOrders);
	}
	
}
