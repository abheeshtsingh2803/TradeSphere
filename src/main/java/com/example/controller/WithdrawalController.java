package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.model.Wallet;
import com.example.model.Withdrawal;
import com.example.service.UserService;
import com.example.service.WalletService;
import com.example.service.WithdrawalService;

@RestController
public class WithdrawalController {
	
	@Autowired
	private WithdrawalService withdrawalService;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/api/withdrawal/{amount}")
	public ResponseEntity<?> withdrawalRequestEntity (@PathVariable Long amount, @RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByProfileByJwt(jwt);
		Wallet userWallet = walletService.getUserWallet(user);
	
		Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount, user);
		walletService.addBalance(userWallet, -withdrawal.getAmountLong());
	
		return new ResponseEntity<>(withdrawal, HttpStatus.OK);
	}
	
	@PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
	public ResponseEntity<?> proceedWithdrawal(@PathVariable Long id, @PathVariable boolean accept, @RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserByProfileByJwt(jwt);
		
		Withdrawal withdrawal = withdrawalService.proccedWithdrawal(id, accept);
		
		Wallet userWallet = walletService.getUserWallet(user);
		
		if(!accept) {
			walletService.addBalance(userWallet, withdrawal.getAmountLong());
		}
		return new ResponseEntity<>(withdrawal, HttpStatus.OK);
	}
	
	@GetMapping("/api/withdrawal")
	public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByProfileByJwt(jwt);
		List<Withdrawal> withdrawal = withdrawalService.getUsersWithdrawalHistory(user);
		
		return new ResponseEntity<>(withdrawal, HttpStatus.OK);
	}
	
	public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByProfileByJwt(jwt);
		
		List<Withdrawal> withdrawal = withdrawalService.getAllWithdrawalRequest();
		
		return new ResponseEntity<>(withdrawal, HttpStatus.OK);
	}
}
