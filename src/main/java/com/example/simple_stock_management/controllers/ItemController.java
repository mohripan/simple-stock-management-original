package com.example.simple_stock_management.controllers;


import com.example.simple_stock_management.dto.response.CustomerOrderResponse;
import com.example.simple_stock_management.dto.response.ItemResponse;
import com.example.simple_stock_management.dto.detail.PaginationDetails;
import com.example.simple_stock_management.dto.response.PaginationResponse;
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
                                      @RequestParam(required = false) Boolean includeStock,
                                      @RequestParam(required = false) Boolean includeOrderHistory) {
        Page<Item> items = itemService.getItems(pageable, name, minPrice, maxPrice);
        List<ItemResponse> itemResponses = items.getContent().stream()
                .map(item -> {
                    ItemResponse response = new ItemResponse(item);
                    if (includeStock != null && includeStock) {
                        response.setRemainingStock(itemService.getRemainingStock(item.getId()));
                    }
                    if (includeOrderHistory != null && includeOrderHistory) {
                        response.setOrderHistory(itemService.getOrderHistory(item.getId()).stream()
                                .map(CustomerOrderResponse::new)
                                .collect(Collectors.toList()));
                    }
                    return response;
                })
                .collect(Collectors.toList());

        PaginationResponse<ItemResponse> response = new PaginationResponse<>(
                new PaginationDetails(
                        items.getTotalElements(),
                        items.getTotalPages(),
                        pageable.getPageSize(),
                        pageable.getPageNumber()
                ),
                itemResponses
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Integer id,
                                         @RequestParam(required = false) Boolean includeStock,
                                         @RequestParam(required = false) Boolean includeOrderHistory) {
        ItemResponse response = itemService.getItemResponseById(id, includeStock, includeOrderHistory);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestBody Item item) {
        Item savedItem = itemService.saveItem(item);
        return ResponseEntity.ok(new ItemResponse(savedItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Integer id, @RequestBody Item itemDetails) {
        Item updatedItem = itemService.updateItem(id, itemDetails);
        return ResponseEntity.ok(new ItemResponse(updatedItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}