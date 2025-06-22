package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Coin;
import com.example.service.CoinService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/coins")
public class CoinController {
	@Autowired
	private CoinService coinService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@GetMapping
	ResponseEntity<List<Coin>>getCoinList(@RequestParam("page") int page) throws Exception {
		List<Coin> coins = coinService.getCoinList(page);
		return new ResponseEntity<>(coins, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{coinId}/chart")
	ResponseEntity<JsonNode> getMarketChart(@PathVariable String coinId, @RequestParam("days") int days) throws Exception {
		String res = coinService.getMarketChart(coinId, days);
		JsonNode jsonNode = objectMapper.readTree(res);
		return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/search")
	ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword) throws Exception {
		String res = coinService.searchCoin(keyword);
		JsonNode jsonNode = objectMapper.readTree(res);
		return ResponseEntity.ok(jsonNode);
	}
	
	@GetMapping("/top50")
	ResponseEntity<JsonNode> getTop50CoinByMarketCapRank() throws Exception {
		String res = coinService.getTop50CoinsByMarketCapRank();
		JsonNode jsonNode = objectMapper.readTree(res);
		return ResponseEntity.ok(jsonNode);
	}
	
	@GetMapping("/trading")
	ResponseEntity<JsonNode> getTradingCoins() throws Exception {
		String res = coinService.getTradingCoins();
		JsonNode jsonNode = objectMapper.readTree(res);
		return ResponseEntity.ok(jsonNode);
	}
	
	@GetMapping("/details/{coinId}")
	ResponseEntity<JsonNode> getCoinDetils(@PathVariable String coinId) throws Exception {
		String res = coinService.getCoinDetils(coinId);
		JsonNode jsonNode = objectMapper.readTree(res);
		return ResponseEntity.ok(jsonNode);
	}
}
