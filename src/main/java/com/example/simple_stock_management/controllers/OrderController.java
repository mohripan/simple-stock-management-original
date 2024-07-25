package com.example.simple_stock_management.controllers;

import com.example.simple_stock_management.dto.detail.PaginationDetails;
import com.example.simple_stock_management.dto.response.CustomerOrderResponse;
import com.example.simple_stock_management.dto.response.OrderResponse;
import com.example.simple_stock_management.dto.response.PaginationResponse;
import com.example.simple_stock_management.model.CustomerOrder;
import com.example.simple_stock_management.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody CustomerOrder order) {
        CustomerOrder savedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(new OrderResponse(savedOrder));
    }

    @PutMapping("/{orderNo}")
    public ResponseEntity<?> updateOrder(@PathVariable String orderNo, @RequestBody CustomerOrder order) {
        CustomerOrder updatedOrder = orderService.updateOrder(orderNo, order);
        return ResponseEntity.ok(new OrderResponse(updatedOrder));
    }

    @GetMapping
    public ResponseEntity<?> listOrders(Pageable pageable, @RequestParam(required = false) Integer itemId) {
        Page<CustomerOrder> orders = orderService.listOrders(pageable, itemId);
        List<OrderResponse> orderResponses = orders.getContent().stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());

        PaginationResponse<OrderResponse> response = new PaginationResponse<>(
                new PaginationDetails(
                        orders.getTotalElements(),
                        orders.getTotalPages(),
                        pageable.getPageSize(),
                        pageable.getPageNumber()
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
