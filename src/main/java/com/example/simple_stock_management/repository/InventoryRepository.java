package com.example.simple_stock_management.repository;

import com.example.simple_stock_management.model.Inventory;
import com.example.simple_stock_management.model.InventoryKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, InventoryKey> {
    List<Inventory> findByIdItemId(Integer itemId);
    @Transactional
    @Modifying
    @Query("DELETE FROM Inventory i WHERE i.id.itemId = :itemId")
    void deleteByItemId(Integer itemId);
}
