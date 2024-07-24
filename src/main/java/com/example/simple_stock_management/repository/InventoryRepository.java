package com.example.simple_stock_management.repository;

import com.example.simple_stock_management.model.Inventory;
import com.example.simple_stock_management.model.InventoryKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, InventoryKey> {
    List<Inventory> findByIdItemId(Long itemId);
}
