package com.example.simple_stock_management.controllers;


import com.example.simple_stock_management.dto.ItemResponse;
import com.example.simple_stock_management.dto.PaginationResponse;
import com.example.simple_stock_management.model.Item;
import com.example.simple_stock_management.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;
    @GetMapping
    public ResponseEntity<?> getItems(Pageable pageable,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) Double minPrice,
                                      @RequestParam(required = false) Double maxPrice,
                                      @RequestParam(required = false) Boolean includeStock) {
        Page<Item> items = itemService.getItems(pageable);
        List<ItemResponse> itemResponses = items.getContent().stream().map(item -> {
            ItemResponse response = new ItemResponse(item);
            if (includeStock != null && includeStock) {
                response.setRemainingStock(itemService.getRemainingStock(item.getId()));
            }
            return response;
        }).collect(Collectors.toList());

        PaginationResponse<ItemResponse> response = new PaginationResponse<>(
                items.getTotalElements(),
                items.getTotalPages(),
                pageable.getPageSize(),
                pageable.getPageNumber(),
                itemResponses
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Integer id,
                                         @RequestParam(required = false) Boolean includeStock,
                                         @RequestParam(required = false) Boolean includeOrderHistory) {
        Item item = itemService.getItemById(id);
        ItemResponse response = new ItemResponse(item);

        if (includeStock != null && includeStock) {
            response.setRemainingStock(itemService.getRemainingStock(id));
        }

        if (includeOrderHistory != null && includeOrderHistory) {
            response.setOrderHistory(itemService.getOrderHistory(id));
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item savedItem = itemService.saveItem(item);
        return ResponseEntity.ok(savedItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Integer id, @RequestBody Item itemDetails) {
        Item updatedItem = itemService.updateItem(id, itemDetails);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
