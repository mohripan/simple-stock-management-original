package com.example.simple_stock_management.repository;

import com.example.simple_stock_management.dto.CustomerOrderResponse;
import com.example.simple_stock_management.model.CustomerOrder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Integer> {
    List<CustomerOrder> findOrderResponsesByItemId(Integer itemId);
    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerOrder o WHERE o.item.id = :itemId")
    void deleteByItemId(Integer itemId);
}
