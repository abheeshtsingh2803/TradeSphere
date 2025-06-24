package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Order;
import com.example.model.PaymentOrder;
import com.example.model.User;
import com.example.model.Wallet;
import com.example.model.WalletTransaction;
import com.example.response.PaymentResponse;
import com.example.service.OrderService;
import com.example.service.PaymentService;
import com.example.service.UserService;
import com.example.service.WalletService;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/api/wallet")
	public ResponseEntity<Wallet> getUserWalletEntity (@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByProfileByJwt(jwt);
		Wallet wallet =  walletService.getUserWallet(user);
		return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/api/wallet/{walletId}/transfer")
	public ResponseEntity<Wallet> walletToWalletTransfer (@RequestHeader("Authorization") String jwt, @PathVariable Long walletId, @RequestBody WalletTransaction req) throws Exception {
		User senderUser= userService.findUserByProfileByJwt(jwt);
		Wallet recieverWallet = walletService.findWalletById(walletId);
		Wallet wallet = walletService.walletToWalletTransfer(senderUser, recieverWallet, req.getAmountLong());
		
		return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/api/wallet/order/{orderId}/pay")
	public ResponseEntity<Wallet> payOrderPament (@RequestHeader("Authorization") String jwt,  @PathVariable Long orderId) throws Exception {
		User user= userService.findUserByProfileByJwt(jwt);
		Order order = orderService.getOrderById(orderId);
		Wallet wallet = walletService.payOrderPayment(order, user);
		
		return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/api/wallet/deposit")
	public ResponseEntity<Wallet> addBalanceToWallet (@RequestHeader("Authorization") String jwt, @RequestParam(name = "order_id") Long orderId, @RequestParam(name = "payment_id") String paymentId) throws Exception {
		User user = userService.findUserByProfileByJwt(jwt);
		Wallet wallet = walletService.getUserWallet(user);
		PaymentOrder order = paymentService.getPaymentOrderById(orderId);
		Boolean status = paymentService.ProccedPaymentOrder(order, paymentId);

		if(status) {
			wallet = walletService.addBalance(wallet,  order.getAmountLong());
		}
		
		return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
	}
}
