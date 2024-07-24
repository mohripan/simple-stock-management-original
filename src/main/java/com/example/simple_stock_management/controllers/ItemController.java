package com.example.simple_stock_management.controllers;


import com.example.simple_stock_management.model.Item;
import com.example.simple_stock_management.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    }
}
