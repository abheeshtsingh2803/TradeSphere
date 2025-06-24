package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.PaymentOrder;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long>{

}
