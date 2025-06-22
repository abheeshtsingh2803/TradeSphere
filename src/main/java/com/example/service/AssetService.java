package com.example.service;

import java.util.List;

import com.example.model.Asset;
import com.example.model.Coin;
import com.example.model.User;

public interface AssetService {
	Asset createAsset(User user, Coin coin, double quantity);
	
	Asset getAssetById(Long assetId) throws Exception;
	
	Asset getAssetByUserIdAndId(Long userId, Long AssetId);
	
	List<Asset> getUserAssets(Long userId);
	
	Asset updateAsset(Long assetId, double quantity) throws Exception;
	
	Asset findAssetByUserIdAndCoinId(Long userId, String coinId);
	
	void deleteAsset(Long assetId);
}
