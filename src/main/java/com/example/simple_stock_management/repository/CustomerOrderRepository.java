package com.example.simple_stock_management.repository;

import com.example.simple_stock_management.dto.CustomerOrderResponse;
import com.example.simple_stock_management.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Integer> {
    List<CustomerOrderResponse> findByItemId(Integer itemId);
    void deleteItemById(Integer itemId);
}
