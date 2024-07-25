package com.example.simple_stock_management.controllers;

import com.example.simple_stock_management.dto.InventoryResponse;
import com.example.simple_stock_management.dto.PaginationDetails;
import com.example.simple_stock_management.dto.PaginationResponse;
import com.example.simple_stock_management.model.Inventory;
import com.example.simple_stock_management.model.InventoryKey;
import com.example.simple_stock_management.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventories")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{itemId}/{type}")
    public ResponseEntity<?> getInventory(@PathVariable Integer itemId, @PathVariable String type) {
        InventoryKey id = new InventoryKey(itemId, type);
        Inventory inventory = inventoryService.getInventory(id);
        return ResponseEntity.ok(new InventoryResponse(inventory));
    }

    @GetMapping
    public ResponseEntity<?> listInventories(Pageable pageable) {
        Page<Inventory> inventories = inventoryService.listInventories(pageable);
        List<InventoryResponse> inventoryResponses = inventories.getContent().stream()
                .map(InventoryResponse::new)
                .collect(Collectors.toList());
        PaginationResponse<InventoryResponse> response = new PaginationResponse<>(
                new PaginationDetails(
                        inventories.getTotalElements(),
                        inventories.getTotalPages(),
                        pageable.getPageSize(),
                        pageable.getPageNumber()
                ),
                inventoryResponses
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> saveInventory(@RequestBody Inventory inventory) {
        Inventory savedInventory = inventoryService.saveInventory(inventory);
        return ResponseEntity.ok(new InventoryResponse(savedInventory));
    }

    @PutMapping
    public ResponseEntity<?> updateInventory(@RequestBody Inventory inventory) {
        Inventory updatedInventory = inventoryService.updateInventory(inventory);
        return ResponseEntity.ok(new InventoryResponse(updatedInventory));
    }

    @DeleteMapping("/{itemId}/{type}")
    public ResponseEntity<?> deleteInventory(@PathVariable Integer itemId, @PathVariable String type) {
        InventoryKey id = new InventoryKey(itemId, type);
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}