package com.example.response;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Watchlist;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long>{
	Watchlist findByUserId(Long userId);
}
