package com.example.simple_stock_management.services;

import com.example.simple_stock_management.dto.OrderResponse;
import com.example.simple_stock_management.model.Inventory;
import com.example.simple_stock_management.model.Item;
import com.example.simple_stock_management.model.Order;
import com.example.simple_stock_management.repository.InventoryRepository;
import com.example.simple_stock_management.repository.ItemRepository;
import com.example.simple_stock_management.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private OrderRepository orderRepository;

    public Page<Item> getItems(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public Integer getRemainingStock(Long itemId) {
        List<Inventory> inventories = inventoryRepository.findByIdItemId(itemId);
        int topup = inventories.stream().filter(i -> i.getId().getType().equals("T")).mapToInt(Inventory::getQyt).sum();
        int withdrawal = inventories.stream().filter(i -> i.getId().getType().equals("W")).mapToInt(Inventory::getQyt).sum();
        return topup - withdrawal;
    }

    public List<OrderResponse> getOrderHistory(Long itemId) {
        return orderRepository.findByItemId(itemId);
    }
}
