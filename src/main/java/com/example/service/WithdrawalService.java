package com.example.service;

import java.util.List;

import com.example.model.User;
import com.example.model.Withdrawal;

public interface WithdrawalService {
	Withdrawal requestWithdrawal(Long amount, User user);
	
	Withdrawal proccedWithdrawal(Long withdrawalId, boolean accept) throws Exception;
	
	List<Withdrawal> getUsersWithdrawalHistory(User user);
	
	List<Withdrawal> getAllWithdrawalRequest();
}
