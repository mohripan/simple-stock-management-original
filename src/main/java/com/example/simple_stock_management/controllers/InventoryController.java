package com.example.simple_stock_management.controllers;

import com.example.simple_stock_management.dto.request.InventoryRequest;
import com.example.simple_stock_management.dto.response.InventoryGroupedResponse;
import com.example.simple_stock_management.dto.response.InventoryResponse;
import com.example.simple_stock_management.dto.detail.PaginationDetails;
import com.example.simple_stock_management.dto.response.InventoryTypeResponse;
import com.example.simple_stock_management.dto.response.PaginationResponse;
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

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getInventory(@PathVariable Integer itemId) {
        InventoryGroupedResponse inventoryGroupedResponse = inventoryService.getInventoryByItemId(itemId);
        return ResponseEntity.ok(inventoryGroupedResponse);
    }

    @GetMapping
    public ResponseEntity<?> listInventories(Pageable pageable) {
        Page<Inventory> inventories = inventoryService.listInventories(pageable);
        List<InventoryGroupedResponse> inventoryResponses = inventories.getContent().stream()
                .collect(Collectors.groupingBy(inventory -> inventory.getId().getItemId()))
                .entrySet().stream()
                .map(entry -> {
                    Integer itemId = entry.getKey();
                    List<Inventory> itemInventories = entry.getValue();
                    Integer remainingStock = inventoryService.calculateRemainingStock(itemId);
                    List<InventoryTypeResponse> types = itemInventories.stream()
                            .map(inventory -> new InventoryTypeResponse(inventory.getId().getType(), inventory.getQty()))
                            .collect(Collectors.toList());
                    return new InventoryGroupedResponse(itemId, remainingStock, types);
                })
                .collect(Collectors.toList());

        PaginationResponse<InventoryGroupedResponse> response = new PaginationResponse<>(
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
    public ResponseEntity<?> saveInventory(@RequestBody InventoryRequest inventoryRequest) {
        InventoryKey id = new InventoryKey(inventoryRequest.getItemId(), inventoryRequest.getType());
        Inventory inventory = new Inventory(id, inventoryRequest.getQty());
        Inventory savedInventory = inventoryService.saveInventory(inventory);
        Integer remainingStock = inventoryService.calculateRemainingStock(inventoryRequest.getItemId());
        return ResponseEntity.ok(new InventoryResponse(inventory, remainingStock));
    }

    @PutMapping
    public ResponseEntity<?> updateInventory(@RequestBody InventoryRequest inventoryRequest) {
        InventoryKey id = new InventoryKey(inventoryRequest.getItemId(), inventoryRequest.getType());
        Inventory inventory = new Inventory(id, inventoryRequest.getQty());
        Inventory updatedInventory = inventoryService.updateInventory(inventory);
        Integer remainingStock = inventoryService.calculateRemainingStock(inventoryRequest.getItemId());
        return ResponseEntity.ok(new InventoryResponse(updatedInventory, remainingStock));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteInventory(@PathVariable Integer itemId) {
        inventoryService.deleteInventoryByItemId(itemId);
        return ResponseEntity.noContent().build();
    }
}
