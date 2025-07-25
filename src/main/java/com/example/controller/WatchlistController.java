package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Coin;
import com.example.model.User;
import com.example.model.Watchlist;
import com.example.service.CoinService;
import com.example.service.UserService;
import com.example.service.WatchlistService;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {
	
	@Autowired
	private WatchlistService watchlistService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CoinService coinService;
	
	@GetMapping("/user")
	public ResponseEntity<Watchlist> getUserWatchlist(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByProfileByJwt(jwt);
		Watchlist watchlist = watchlistService.findUserWatchlist(user.getId());
		return ResponseEntity.ok(watchlist);
	}
	
	@PatchMapping("/add/coin/{coinId}")
	public ResponseEntity<Coin> addItemToWatchlist(@RequestHeader("Authorization") String jwt, @PathVariable String coinId) throws Exception{
		User user  =userService.findUserByProfileByJwt(jwt);
		Coin coin = coinService.findById(coinId);
		Coin addedCoin = watchlistService.addItemToWatchlist(coin, user);
		return ResponseEntity.ok(addedCoin);
	}
}
