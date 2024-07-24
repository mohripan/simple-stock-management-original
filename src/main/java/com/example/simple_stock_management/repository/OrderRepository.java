package com.example.simple_stock_management.repository;

import com.example.simple_stock_management.dto.OrderResponse;
import com.example.simple_stock_management.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {
    List<OrderResponse> findByItemId(Integer itemId);
}
