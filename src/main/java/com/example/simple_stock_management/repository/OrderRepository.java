package com.example.simple_stock_management.repository;

import com.example.simple_stock_management.dto.OrderResponse;
import com.example.simple_stock_management.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<OrderResponse> findByItemId(Long itemId);
}
