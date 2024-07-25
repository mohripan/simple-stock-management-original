package com.example.simple_stock_management.services;

import com.example.simple_stock_management.exception.InsufficientStockException;
import com.example.simple_stock_management.model.Inventory;
import com.example.simple_stock_management.model.InventoryKey;
import com.example.simple_stock_management.repository.InventoryRepository;
import com.example.simple_stock_management.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    public Inventory getInventory(InventoryKey id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    public Page<Inventory> listInventories(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

    @Transactional
    public Inventory saveInventory(Inventory inventory) {
        if (!itemRepository.existsById(inventory.getId().getItemId())) {
            throw new RuntimeException("Item not found with id " + inventory.getId().getItemId());
        }

        Inventory existingInventory = inventoryRepository.findById(inventory.getId()).orElse(null);
        if (existingInventory != null) {
            if (inventory.getId().getType().equals("W")) {
                validateWithdrawal(inventory.getId().getItemId(), inventory.getQty());
            }
            existingInventory.setQty(existingInventory.getQty() + inventory.getQty());
            inventoryRepository.save(existingInventory);
            return existingInventory;
        } else {
            if (inventory.getId().getType().equals("W")) {
                validateWithdrawal(inventory.getId().getItemId(), inventory.getQty());
            }
            return inventoryRepository.save(inventory);
        }
    }

    @Transactional
    public Inventory updateInventory(Inventory inventory) {
        if (!itemRepository.existsById(inventory.getId().getItemId())) {
            throw new RuntimeException("Item not found with id " + inventory.getId().getItemId());
        }

        validateWithdrawal(inventory.getId().getItemId(), inventory.getQty());
        return inventoryRepository.save(inventory);
    }

    private void validateWithdrawal(Integer itemId, Integer withdrawalQty) {
        List<Inventory> inventories = inventoryRepository.findByIdItemId(itemId);
        int topUp = inventories.stream().filter(i -> i.getId().getType().equals("T")).mapToInt(Inventory::getQty).sum();
        int withdrawal = inventories.stream().filter(i -> i.getId().getType().equals("W")).mapToInt(Inventory::getQty).sum();
        if (withdrawal + withdrawalQty > topUp) {
            throw new InsufficientStockException("Item stock is insufficient");
        }
    }

    @Transactional
    public void deleteInventory(InventoryKey id) {
        inventoryRepository.deleteById(id);
    }

    public Integer calculateRemainingStock(Integer itemId) {
        List<Inventory> inventories = inventoryRepository.findByIdItemId(itemId);
        int topUp = inventories.stream().filter(i -> i.getId().getType().equals("T")).mapToInt(Inventory::getQty).sum();
        int withdrawal = inventories.stream().filter(i -> i.getId().getType().equals("W")).mapToInt(Inventory::getQty).sum();
        return topUp - withdrawal;
    }

    @Transactional
    public void deleteInventoriesByItemId(Integer itemId) {
        inventoryRepository.deleteByIdItemId(itemId);
    }
}

