package com.example.simple_stock_management.services;

import com.example.simple_stock_management.dto.response.InventoryGroupedResponse;
import com.example.simple_stock_management.dto.response.InventoryTypeResponse;
import com.example.simple_stock_management.exception.InsufficientStockException;
import com.example.simple_stock_management.exception.ResourceNotFoundException;
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
import java.util.stream.Collectors;

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
            throw new ResourceNotFoundException("Item not found with id " + inventory.getId().getItemId());
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
            throw new ResourceNotFoundException("Item not found with id " + inventory.getId().getItemId());
        }

        Inventory existingInventory = inventoryRepository.findById(inventory.getId()).orElse(null);
        if (existingInventory == null) {
            throw new ResourceNotFoundException("Item with id " + inventory.getId().getItemId() + " hasn't been registered");
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
    public void deleteInventoryByItemId(Integer itemId) {
        if (!inventoryRepository.existsById(new InventoryKey(itemId, "T")) &&
                !inventoryRepository.existsById(new InventoryKey(itemId, "W"))) {
            throw new ResourceNotFoundException("Item with id " + itemId + " not found");
        }
        inventoryRepository.deleteByIdItemId(itemId);
    }

    public Integer calculateRemainingStock(Integer itemId) {
        List<Inventory> inventories = inventoryRepository.findByIdItemId(itemId);
        int topUp = inventories.stream().filter(i -> i.getId().getType().equals("T")).mapToInt(Inventory::getQty).sum();
        int withdrawal = inventories.stream().filter(i -> i.getId().getType().equals("W")).mapToInt(Inventory::getQty).sum();
        return topUp - withdrawal;
    }

    public InventoryGroupedResponse getInventoryByItemId(Integer itemId) {
        List<Inventory> inventories = inventoryRepository.findByIdItemId(itemId);
        if (inventories.isEmpty()) {
            throw new ResourceNotFoundException("Item with id " + itemId + " hasn't been registered");
        }
        Integer remainingStock = calculateRemainingStock(itemId);
        List<InventoryTypeResponse> types = inventories.stream()
                .map(inventory -> new InventoryTypeResponse(inventory.getId().getType(), inventory.getQty()))
                .collect(Collectors.toList());
        return new InventoryGroupedResponse(itemId, remainingStock, types);
    }
}
