package com.example.simple_stock_management.services;

import com.example.simple_stock_management.dto.response.CustomerOrderResponse;
import com.example.simple_stock_management.dto.response.ItemResponse;
import com.example.simple_stock_management.exception.ResourceNotFoundException;
import com.example.simple_stock_management.model.CustomerOrder;
import com.example.simple_stock_management.model.Inventory;
import com.example.simple_stock_management.model.Item;
import com.example.simple_stock_management.repository.InventoryRepository;
import com.example.simple_stock_management.repository.ItemRepository;
import com.example.simple_stock_management.repository.CustomerOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    public ItemService() {
    }

    public ItemService(ItemRepository itemRepository, InventoryRepository inventoryRepository, CustomerOrderRepository customerOrderRepository) {
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
        this.customerOrderRepository = customerOrderRepository;
    }

    public Page<Item> getItems(Pageable pageable, String name, Double minPrice, Double maxPrice) {
        return itemRepository.findByNameAndPriceRange(name, minPrice, maxPrice, pageable);
    }

    public Item getItemById(Integer id) {
        return itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + id));
    }

    public ItemResponse getItemResponseById(Integer id, Boolean includeStock, Boolean includeOrderHistory) {
        Item item = getItemById(id);
        ItemResponse response = new ItemResponse(item);

        if (includeStock != null && includeStock) {
            response.setRemainingStock(getRemainingStock(id));
        }

        if (includeOrderHistory != null && includeOrderHistory) {
            response.setOrderHistory(getOrderHistory(id).stream()
                    .map(order -> new CustomerOrderResponse(order))
                    .collect(Collectors.toList()));
        }

        return response;
    }

    public Integer getRemainingStock(Integer itemId) {
        List<Inventory> inventories = inventoryRepository.findByIdItemId(itemId);
        int topUp = inventories.stream().filter(i -> i.getId().getType().equals("T")).mapToInt(Inventory::getQty).sum();
        int withdrawal = inventories.stream().filter(i -> i.getId().getType().equals("W")).mapToInt(Inventory::getQty).sum();
        return topUp - withdrawal;
    }

    public List<CustomerOrder> getOrderHistory(Integer itemId) {
        return customerOrderRepository.findByItemId(itemId);
    }

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public Item updateItem(Integer id, Item itemDetails) {
        Item item = getItemById(id);
        item.setName(itemDetails.getName());
        item.setPrice(itemDetails.getPrice());
        return itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(Integer id) {
        Item item = getItemById(id);
        customerOrderRepository.deleteByItemId(id);
        inventoryRepository.deleteByIdItemId(id);
        itemRepository.delete(item);
    }
}
