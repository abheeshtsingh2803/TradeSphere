package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.OrderItem;

public interface OrderItemRespository extends JpaRepository<OrderItem, Long> {

}
