package com.example.service;

import com.example.model.Coin;
import com.example.model.User;
import com.example.model.Watchlist;

public interface WatchlistService {
	
	Watchlist findUserWatchlist(Long userId) throws Exception;
	Watchlist createWatchlist(User user);
	Watchlist findById(Long id) throws Exception;
	
	Coin addItemToWatchlist(Coin coin, User user) throws Exception;
}
