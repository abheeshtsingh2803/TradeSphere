package com.example.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.OrderStatus;
import com.example.domain.OrderType;
import com.example.model.Asset;
import com.example.model.Coin;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.User;
import com.example.repository.OrderItemRespository;
import com.example.repository.OrderRepository;


@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private OrderItemRespository orderItemRespository;
	
	@Autowired
	private AssetService assetService;

	@Override
	public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
		
		double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();
		
		Order order = new Order();
		order.setUser(user);
		order.setOrderItem(orderItem);
		order.setOrderType(orderType);
		order.setPrice(BigDecimal.valueOf(price));
		order.setTimestamp(LocalDateTime.now());
		order.setStatus(OrderStatus.PENDING);
		
		return orderRepository.save(order);
	}

	@Override
	public Order getOrderById(Long orderId) throws Exception {
		return orderRepository.findById(orderId).orElseThrow(() -> new Exception("Order Not Found "));
	}

	@Override
	public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assestSymbol) {
		return orderRepository.findByUserId(userId);
	}

	private OrderItem createOrderItem(Coin coin, double quantity, double buyPrice, double sellPrice) {
		OrderItem orderItem = new OrderItem();
		orderItem.setCoin(coin);
		orderItem.setQuantity(quantity);
		orderItem.setBuyPrice(buyPrice);
		orderItem.setSellPrice(sellPrice);
		
		return orderItemRespository.save(orderItem);
	}
	
	@Transactional
	public Order buyAssest(Coin coin, double quantity, User user) throws Exception {
		if(quantity <= 0) {
			throw new Exception("Quantity should be greater than zero");
		}
		double buyPrice = coin.getCurrentPrice();
		
		OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);
		Order order = createOrder(user, orderItem, OrderType.BUY);
		orderItem.setOrder(order);
		walletService.payOrderPayment(order, user);
		
		order.setStatus(OrderStatus.SUCCESS);
		order.setOrderType(OrderType.BUY);
		Order savedOrder = orderRepository.save(order);
		
		// Create Assest
		Asset oldAsset = assetService.findAssetByUserIdAndCoinId(order.getUser().getId(), order.getOrderItem().getCoin().getId());
		
		if(oldAsset == null) {
			assetService.createAsset(user, orderItem.getCoin(), orderItem.getQuantity());
		} else {
			assetService.updateAsset(oldAsset.getIdLong(), quantity);
		}
		
		
		return savedOrder;
	}
	
	@Transactional
	public Order sellAssest(Coin coin, double quantity, User user) throws Exception {
		if(quantity <= 0) {
			throw new Exception("Quantity should be greater than zero");
		}
		double sellPrice = coin.getCurrentPrice();
		
		Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());
		
		double buyPrice = assetToSell.getBuyPrice();
		
		if(assetToSell != null) {
			OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);
			
			Order order = createOrder(user, orderItem, OrderType.SELL);
			
			orderItem.setOrder(order);
			
			if(assetToSell.getQuantity() >= quantity) {
				order.setStatus(OrderStatus.SUCCESS);
				order.setOrderType(OrderType.SELL);
				Order savedOrder = orderRepository.save(order);
				
				walletService.payOrderPayment(order, user);
				
				Asset updatesAsset = assetService.updateAsset(assetToSell.getIdLong(), -quantity);
				if(updatesAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
					assetService.deleteAsset(updatesAsset.getIdLong());
				}
				return savedOrder;
			}
			 throw new Exception("Insuffiecient quantity to sell");
		}
		throw new Exception("Asset Not Found");
	} 
	
	@Override
	@Transactional
	public Order processOrder(Coin coin, double quatity, OrderType orderType, User user) throws Exception {
		if(orderType.equals(OrderType.BUY)) {
			return buyAssest(coin, quatity, user);
		} else if(orderType.equals(OrderType.SELL)) {
			return sellAssest(coin, quatity, user);
		}
		
		throw new Exception("Invalid Order Type");
	}

}
