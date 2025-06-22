package com.example.service;

import java.util.List;

import com.example.model.Coin;

public interface CoinService {
	List<Coin> getCoinList(int page) throws Exception;
	
	String getMarketChart(String coinId, int days) throws Exception;
	
	String getCoinDetils(String coinId) throws Exception;
	
	Coin findById(String coinId) throws Exception;
	
	String searchCoin(String keyword) throws Exception;
	
	String getTop50CoinsByMarketCapRank() throws Exception;
	
	String getTradingCoins() throws Exception;
}
