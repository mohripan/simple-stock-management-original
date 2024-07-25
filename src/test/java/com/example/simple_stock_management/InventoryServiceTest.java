package com.example.simple_stock_management;

import com.example.simple_stock_management.dto.response.InventoryGroupedResponse;
import com.example.simple_stock_management.model.Inventory;
import com.example.simple_stock_management.model.InventoryKey;
import com.example.simple_stock_management.model.Item;
import com.example.simple_stock_management.repository.InventoryRepository;
import com.example.simple_stock_management.repository.ItemRepository;
import com.example.simple_stock_management.services.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class InventoryServiceTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    private InventoryService inventoryService;

    @BeforeEach
    public void setUp() {
        inventoryService = new InventoryService(inventoryRepository, itemRepository);
    }

    @Test
    public void testGetInventory() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        InventoryKey id = new InventoryKey(item.getId(), "T");
        Inventory inventory = new Inventory(id, item, 10);
        entityManager.persistAndFlush(inventory);

        Inventory foundInventory = inventoryService.getInventory(id);
        assertEquals(inventory.getQty(), foundInventory.getQty());
    }

    @Test
    public void testListInventories() {
        Item item1 = new Item();
        item1.setName("Test Item 1");
        item1.setPrice(10.0);
        entityManager.persistAndFlush(item1);

        Item item2 = new Item();
        item2.setName("Test Item 2");
        item2.setPrice(15.0);
        entityManager.persistAndFlush(item2);

        Inventory inventory1 = new Inventory(new InventoryKey(item1.getId(), "T"), item1, 10);
        Inventory inventory2 = new Inventory(new InventoryKey(item2.getId(), "T"), item2, 20);
        entityManager.persistAndFlush(inventory1);
        entityManager.persistAndFlush(inventory2);

        Page<Inventory> inventories = inventoryService.listInventories(PageRequest.of(0, 10));
        assertEquals(2, inventories.getTotalElements());
    }

    @Test
    public void testSaveInventory() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        Inventory inventory = new Inventory(new InventoryKey(item.getId(), "T"), item, 10);
        Inventory savedInventory = inventoryService.saveInventory(inventory);
        assertNotNull(savedInventory.getId());
        assertEquals(inventory.getQty(), savedInventory.getQty());
    }

    @Test
    public void testUpdateInventory() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        Inventory inventory = new Inventory(new InventoryKey(item.getId(), "T"), item, 10);
        entityManager.persistAndFlush(inventory);

        Inventory updatedInventory = new Inventory(new InventoryKey(item.getId(), "T"), item, 20);
        Inventory result = inventoryService.updateInventory(updatedInventory);
        assertEquals(20, result.getQty());
    }

    @Test
    public void testDeleteInventoryByItemId() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        Inventory inventory = new Inventory(new InventoryKey(item.getId(), "T"), item, 10);
        entityManager.persistAndFlush(inventory);

        inventoryService.deleteInventoryByItemId(item.getId());
        List<Inventory> inventories = inventoryRepository.findByIdItemId(item.getId());
        assertTrue(inventories.isEmpty());
    }

    @Test
    public void testGetInventoryByItemId() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        Inventory inventory = new Inventory(new InventoryKey(item.getId(), "T"), item, 10);
        entityManager.persistAndFlush(inventory);

        InventoryGroupedResponse response = inventoryService.getInventoryByItemId(item.getId());
        assertEquals(item.getId(), response.getItemId());
        assertEquals(10, response.getRemainingStock());
    }

    @Test
    public void testCalculateRemainingStock() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        Inventory inventoryT = new Inventory(new InventoryKey(item.getId(), "T"), item, 20);
        Inventory inventoryW = new Inventory(new InventoryKey(item.getId(), "W"), item, 5);
        entityManager.persistAndFlush(inventoryT);
        entityManager.persistAndFlush(inventoryW);

        int remainingStock = inventoryService.calculateRemainingStock(item.getId());
        assertEquals(15, remainingStock);
    }
}