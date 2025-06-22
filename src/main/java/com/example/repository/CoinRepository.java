package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.Coin;

public interface CoinRepository extends JpaRepository<Coin, String>{

}
