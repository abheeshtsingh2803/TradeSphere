package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Coin;
import com.example.model.User;
import com.example.model.Watchlist;
import com.example.repository.WatchlistRepository;

@Service
public class WatchListServiceImpl implements WatchlistService {

	@Autowired
	private WatchlistRepository watchlistRepository;
	
	@Override
	public Watchlist findUserWatchlist(Long userId) throws Exception {
		Watchlist watchlist = watchlistRepository.findByUserId(userId);
		if(watchlist==null) {
			throw new Exception("WatchList Not Found");
		}
		return watchlist ;
	}

	@Override
	public Watchlist createWatchlist(User user) {
		Watchlist watchlist = new Watchlist();
		watchlist.setUser(user);
		
		return watchlistRepository.save(watchlist);
	}

	@Override
	public Watchlist findById(Long id) throws Exception {
		Optional<Watchlist> watOptional=watchlistRepository.findById(id);
		if(watOptional.isEmpty()) {
			throw new Exception("Watchlist not found");
		}
		return watOptional.get();
	}

	@Override
	public Coin addItemToWatchlist(Coin coin, User user) throws Exception {
		Watchlist watchlist = findUserWatchlist(user.getId());
		
		if(watchlist.getCoins().contains(coin)) {
			watchlist.getCoins().remove(coin);
		} else {
			watchlist.getCoins().add(coin);
		}
		watchlistRepository.save(watchlist);
		return coin;
	}

}
