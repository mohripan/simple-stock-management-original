package com.example.simple_stock_management.repository;

import com.example.simple_stock_management.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByNameContaining(String name);
    List<Item> findByPriceBetween(Double minPrice, Double maxPrice);
}
