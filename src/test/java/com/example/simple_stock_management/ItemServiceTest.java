package com.example.simple_stock_management;

import com.example.simple_stock_management.model.Item;
import com.example.simple_stock_management.repository.CustomerOrderRepository;
import com.example.simple_stock_management.repository.InventoryRepository;
import com.example.simple_stock_management.repository.ItemRepository;
import com.example.simple_stock_management.services.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemServiceTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    private ItemService itemService;

    @BeforeEach
    public void setUp() {
        itemService = new ItemService(itemRepository, inventoryRepository, customerOrderRepository);
    }

    @Test
    public void testGetItems() {
        Item item1 = new Item();
        item1.setName("Test Item 1");
        item1.setPrice(5.0);
        entityManager.persistAndFlush(item1);

        Item item2 = new Item();
        item2.setName("Test Item 2");
        item2.setPrice(15.0);
        entityManager.persistAndFlush(item2);

        Page<Item> items = itemService.getItems(PageRequest.of(0, 10), null, null, null);
        assertEquals(2, items.getTotalElements());
    }

    @Test
    public void testGetItemsWithFilter() {
        Item item1 = new Item();
        item1.setName("Pen");
        item1.setPrice(1.5);
        entityManager.persistAndFlush(item1);

        Item item2 = new Item();
        item2.setName("Notebook");
        item2.setPrice(12.0);
        entityManager.persistAndFlush(item2);

        Page<Item> items = itemService.getItems(PageRequest.of(0, 10), "Pen", 1.0, 2.0);
        assertEquals(1, items.getTotalElements());
        assertEquals("Pen", items.getContent().get(0).getName());
    }

    @Test
    public void testGetItemById() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        Item foundItem = itemService.getItemById(item.getId());
        assertEquals(item.getName(), foundItem.getName());
    }

    @Test
    public void testSaveItem() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);

        Item savedItem = itemService.saveItem(item);
        assertNotNull(savedItem.getId());
        assertEquals(item.getName(), savedItem.getName());
    }

    @Test
    public void testUpdateItem() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        Item updatedDetails = new Item();
        updatedDetails.setName("Updated Item");
        updatedDetails.setPrice(15.0);

        Item updatedItem = itemService.updateItem(item.getId(), updatedDetails);
        assertEquals("Updated Item", updatedItem.getName());
        assertEquals(15.0, updatedItem.getPrice());
    }

    @Test
    public void testDeleteItem() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        itemService.deleteItem(item.getId());
        assertFalse(itemRepository.findById(item.getId()).isPresent());
    }
}
