package com.example.simple_stock_management.repository;

import com.example.simple_stock_management.model.CustomerOrder;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, String> {
    List<CustomerOrder> findByItemId(Integer itemId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerOrder o WHERE o.item.id = :itemId")
    void deleteByItemId(Integer itemId);

    @Query("SELECT o FROM CustomerOrder o WHERE o.item.id = :itemId")
    Page<CustomerOrder> findByItemId(Integer itemId, Pageable pageable);
}
