package com.example.simple_stock_management.repository;

import com.example.simple_stock_management.model.OrderNumberCounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderNumberCounterRepository extends JpaRepository<OrderNumberCounter, String> {
}
