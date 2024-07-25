package com.example.simple_stock_management.repository;

import com.example.simple_stock_management.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByNameContaining(String name);
    List<Item> findByPriceBetween(Double minPrice, Double maxPrice);

    @Query("SELECT i FROM Item i WHERE (:name IS NULL OR i.name LIKE %:name%) AND (:minPrice IS NULL OR i.price >= :minPrice) AND (:maxPrice IS NULL OR i.price <= :maxPrice)")
    Page<Item> findByNameAndPriceRange(@Param("name") String name, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);
}
