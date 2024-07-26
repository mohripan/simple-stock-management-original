package com.example.simple_stock_management.controllers;

import com.example.simple_stock_management.dto.detail.PaginationDetails;
import com.example.simple_stock_management.dto.response.CustomerOrderResponse;
import com.example.simple_stock_management.dto.response.OrderResponse;
import com.example.simple_stock_management.dto.response.PaginationResponse;
import com.example.simple_stock_management.model.CustomerOrder;
import com.example.simple_stock_management.model.Item;
import com.example.simple_stock_management.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody Map<String, Object> orderRequest) {
        Integer itemId = (Integer) orderRequest.get("itemId");
        Integer qty = (Integer) orderRequest.get("qty");

        CustomerOrder order = new CustomerOrder();
        order.setItem(new Item());
        order.getItem().setId(itemId);
        order.setQty(qty);

        CustomerOrder savedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(new OrderResponse(savedOrder));
    }

    @PutMapping("/{orderNo}")
    public ResponseEntity<?> updateOrder(@PathVariable String orderNo, @RequestBody Map<String, Object> orderRequest) {
        Integer qty = (Integer) orderRequest.get("qty");

        CustomerOrder updatedOrder = orderService.updateOrder(orderNo, qty);
        return ResponseEntity.ok(new OrderResponse(updatedOrder));
    }

    @GetMapping
    public ResponseEntity<?> listOrders(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(required = false) Integer itemId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CustomerOrder> orders = orderService.listOrders(pageable, itemId);
        List<OrderResponse> orderResponses = orders.getContent().stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());

        PaginationResponse<OrderResponse> response = new PaginationResponse<>(
                new PaginationDetails(
                        orders.getTotalElements(),
                        orders.getTotalPages(),
                        pageable.getPageSize(),
                        pageable.getPageNumber() + 1
                ),
                orderResponses
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderNo}")
    public ResponseEntity<?> getOrderDetails(@PathVariable String orderNo) {
        CustomerOrder order = orderService.getOrderDetails(orderNo);
        return ResponseEntity.ok(new OrderResponse(order));
    }

    @DeleteMapping("/{orderNo}")
    public ResponseEntity<?> deleteOrder(@PathVariable String orderNo) {
        orderService.deleteOrder(orderNo);
        return ResponseEntity.noContent().build();
    }
}
