package com.example.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import com.example.model.Coin;
import com.example.repository.CoinRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CoinServiceImpl implements CoinService {

	@Autowired
	private CoinRepository coinRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public List<Coin> getCoinList(int page) throws Exception {
		String urlString="https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page="+page;
		
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			HttpHeaders headers = new HttpHeaders();
			
			HttpEntity<String> entity = new HttpEntity<String>("Parameters", headers);
			
			ResponseEntity<String> response = restTemplate.exchange(urlString, HttpMethod.GET, entity, String.class);
			
			List<Coin> coinList = objectMapper.readValue(response.getBody(), 
					new TypeReference<List<Coin>>() {});
			return coinList;
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public String getMarketChart(String coinId, int days) throws Exception {
		String urlString="https://api.coingecko.com/api/v3/coins/"+coinId+"market_chart?vs_currency=usd&days="+days;
		
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			HttpHeaders headers = new HttpHeaders();
			
			HttpEntity<String> entity = new HttpEntity<String>("Parameters", headers);
			
			ResponseEntity<String> response = restTemplate.exchange(urlString, HttpMethod.GET, entity, String.class);
			
			return response.getBody();
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public String getCoinDetils(String coinId) throws Exception {
		String urlString="https://api.coingecko.com/api/v3/coins/"+coinId;
		
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			HttpHeaders headers = new HttpHeaders();
			
			HttpEntity<String> entity = new HttpEntity<String>("Parameters", headers);
			
			ResponseEntity<String> response = restTemplate.exchange(urlString, HttpMethod.GET, entity, String.class);
			
			JsonNode jsonNode=objectMapper.readTree(response.getBody());
			
			Coin coin = new Coin(); 
			coin.setId(jsonNode.get("id").asText());
			coin.setName(jsonNode.get("name").asText());
			coin.setSymbol(jsonNode.get("symbol").asText());
			coin.setImageUrl(jsonNode.get("image").get("large").asText());
			
			JsonNode marketData = jsonNode.get("market_data");
			
			coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
			coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
			coin.setMarketCapRank(marketData.get("market_cap_rank").get("usd").asInt());
			coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
			coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
			coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
			coin.setPriceChange24h(marketData.get("price_change_24h").get("usd").asDouble());
			coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").get("usd").asDouble());
			
			coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
			coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asLong());
			coin.setTotalSupply(marketData.get("total_supply").get("usd").asLong());
			coinRepository.save(coin);
			return response.getBody();
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public Coin findById(String coinId) throws Exception {
		Optional<Coin> optionalCoin = coinRepository.findById(coinId);
		if(optionalCoin.isEmpty()) throw new Exception("Coin Not Found");
		return optionalCoin.get();
	}

	@Override
	public String searchCoin(String keyword) throws Exception {
		String urlString="https://api.coingecko.com/api/v3/search?query="+keyword;
		
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			HttpHeaders headers = new HttpHeaders();
			
			HttpEntity<String> entity = new HttpEntity<String>("Parameters", headers);
			
			ResponseEntity<String> response = restTemplate.exchange(urlString, HttpMethod.GET, entity, String.class);
			
			return response.getBody();
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public String getTop50CoinsByMarketCapRank() throws Exception {
String urlString="https://api.coingecko.com/api/v3/coins/markets/vs_currency=usd&per_page=50&page=1";
		
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			HttpHeaders headers = new HttpHeaders();
			
			HttpEntity<String> entity = new HttpEntity<String>("Parameters", headers);
			
			ResponseEntity<String> response = restTemplate.exchange(urlString, HttpMethod.GET, entity, String.class);
			
			return response.getBody();
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw new Exception(e.getMessage());
		} 
	}

	@Override
	public String getTradingCoins() throws Exception {
String urlString="https://api.coingecko.com/api/v3/search/trading";
		
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			HttpHeaders headers = new HttpHeaders();
			
			HttpEntity<String> entity = new HttpEntity<String>("Parameters", headers);
			
			ResponseEntity<String> response = restTemplate.exchange(urlString, HttpMethod.GET, entity, String.class);
			
			return response.getBody();
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw new Exception(e.getMessage());
		}
	}

}
