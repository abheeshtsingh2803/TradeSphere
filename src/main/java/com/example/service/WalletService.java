package com.example.service;

import com.example.model.Order;
import com.example.model.User;
import com.example.model.Wallet;

public interface WalletService {
	Wallet getUserWallet(User user);
	Wallet addBalance(Wallet wallet, Long money);
	Wallet findWalletById(Long id) throws Exception;
	Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception;
	Wallet payOrderPayment(Order order, User user) throws Exception;
}
