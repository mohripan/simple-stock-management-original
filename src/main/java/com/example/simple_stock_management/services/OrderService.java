package com.example.simple_stock_management.services;

import com.example.simple_stock_management.exception.InsufficientStockException;
import com.example.simple_stock_management.exception.ResourceNotFoundException;
import com.example.simple_stock_management.model.CustomerOrder;
import com.example.simple_stock_management.model.Inventory;
import com.example.simple_stock_management.model.InventoryKey;
import com.example.simple_stock_management.model.OrderNumberCounter;
import com.example.simple_stock_management.repository.CustomerOrderRepository;
import com.example.simple_stock_management.repository.InventoryRepository;
import com.example.simple_stock_management.repository.ItemRepository;
import com.example.simple_stock_management.repository.OrderNumberCounterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderNumberCounterRepository orderNumberCounterRepository;

    public OrderService() {
    }

    public OrderService(CustomerOrderRepository customerOrderRepository, InventoryRepository inventoryRepository, ItemRepository itemRepository, OrderNumberCounterRepository orderNumberCounterRepository) {
        this.customerOrderRepository = customerOrderRepository;
        this.inventoryRepository = inventoryRepository;
        this.itemRepository = itemRepository;
        this.orderNumberCounterRepository = orderNumberCounterRepository;
    }

    @Transactional
    public CustomerOrder saveOrder(CustomerOrder order) {
        validateOrder(order);

        order.setOrderNo(generateOrderNumber());

        int remainingStock = calculateRemainingStock(order.getItem().getId());
        if (remainingStock < order.getQty()) {
            throw new InsufficientStockException("Item stock is insufficient");
        }

        CustomerOrder savedOrder = customerOrderRepository.save(order);

        InventoryKey inventoryKey = new InventoryKey(order.getItem().getId(), "W");
        Inventory inventory = inventoryRepository.findById(inventoryKey).orElse(new Inventory(inventoryKey, order.getItem(), 0));
        inventory.setQty(inventory.getQty() + order.getQty());
        inventoryRepository.save(inventory);

        return savedOrder;
    }

    @Transactional
    public CustomerOrder updateOrder(String orderNo, CustomerOrder updatedOrder) {
        validateOrder(updatedOrder);

        CustomerOrder existingOrder = customerOrderRepository.findById(orderNo)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with orderNo " + orderNo));

        int qtyBefore = existingOrder.getQty();
        int qtyAfter = updatedOrder.getQty();
        int qtyDifference = qtyAfter - qtyBefore;

        if (qtyDifference > 0) {
            int remainingStock = calculateRemainingStock(updatedOrder.getItem().getId());
            if (remainingStock < qtyDifference) {
                throw new InsufficientStockException("Item stock is insufficient");
            }
        }

        existingOrder.setQty(updatedOrder.getQty());
        existingOrder.setItem(updatedOrder.getItem());
        CustomerOrder savedOrder = customerOrderRepository.save(existingOrder);

        InventoryKey inventoryKey = new InventoryKey(updatedOrder.getItem().getId(), "W");
        Inventory inventory = inventoryRepository.findById(inventoryKey)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with itemId " + updatedOrder.getItem().getId()));
        inventory.setQty(inventory.getQty() + qtyDifference);
        inventoryRepository.save(inventory);

        return savedOrder;
    }

    public Page<CustomerOrder> listOrders(Pageable pageable, Integer itemId) {
        if (itemId != null) {
            return customerOrderRepository.findByItemId(itemId, pageable);
        } else {
            return customerOrderRepository.findAll(pageable);
        }
    }

    public CustomerOrder getOrderDetails(String orderNo) {
        return customerOrderRepository.findById(orderNo)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with orderNo " + orderNo));
    }

    private void validateOrder(CustomerOrder order) {
        if (order.getQty() < 1) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (!itemRepository.existsById(order.getItem().getId())) {
            throw new ResourceNotFoundException("Item not found with id " + order.getItem().getId());
        }
    }

    private int calculateRemainingStock(Integer itemId) {
        List<Inventory> inventories = inventoryRepository.findByIdItemId(itemId);
        int topUp = inventories.stream().filter(i -> i.getId().getType().equals("T")).mapToInt(Inventory::getQty).sum();
        int withdrawal = inventories.stream().filter(i -> i.getId().getType().equals("W")).mapToInt(Inventory::getQty).sum();
        return topUp - withdrawal;
    }

    private synchronized String generateOrderNumber() {
        OrderNumberCounter counter = orderNumberCounterRepository.findById("ORDER").orElse(new OrderNumberCounter(0));
        int nextOrderNumber = counter.getLastOrderNumber() + 1;
        counter.setLastOrderNumber(nextOrderNumber);
        orderNumberCounterRepository.save(counter);
        return "O" + nextOrderNumber;
    }
    @Transactional
    public void deleteOrder(String orderNo) {
        CustomerOrder existingOrder = customerOrderRepository.findById(orderNo)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with orderNo " + orderNo));

        InventoryKey inventoryKey = new InventoryKey(existingOrder.getItem().getId(), "W");
        Inventory inventory = inventoryRepository.findById(inventoryKey)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with itemId " + existingOrder.getItem().getId()));

        inventory.setQty(inventory.getQty() - existingOrder.getQty());
        inventoryRepository.save(inventory);

        customerOrderRepository.delete(existingOrder);
    }

}


