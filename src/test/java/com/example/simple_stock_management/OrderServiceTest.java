package com.example.simple_stock_management;

import com.example.simple_stock_management.model.CustomerOrder;
import com.example.simple_stock_management.model.Inventory;
import com.example.simple_stock_management.model.InventoryKey;
import com.example.simple_stock_management.model.Item;
import com.example.simple_stock_management.repository.CustomerOrderRepository;
import com.example.simple_stock_management.repository.InventoryRepository;
import com.example.simple_stock_management.repository.ItemRepository;
import com.example.simple_stock_management.repository.OrderNumberCounterRepository;
import com.example.simple_stock_management.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderServiceTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderNumberCounterRepository orderNumberCounterRepository;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService(customerOrderRepository, inventoryRepository, itemRepository, orderNumberCounterRepository);
    }

    @Test
    public void testSaveOrder() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        Inventory inventory = new Inventory(new InventoryKey(item.getId(), "T"), item, 20);
        entityManager.persistAndFlush(inventory);

        CustomerOrder order = new CustomerOrder(null, item, 5);
        CustomerOrder savedOrder = orderService.saveOrder(order);

        assertNotNull(savedOrder.getOrderNo());
        assertEquals(order.getQty(), savedOrder.getQty());

        Inventory updatedInventory = inventoryRepository.findById(new InventoryKey(item.getId(), "W")).orElse(null);
        assertNotNull(updatedInventory);
        assertEquals(5, updatedInventory.getQty());
    }

    @Test
    public void testUpdateOrder() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        Inventory inventory = new Inventory(new InventoryKey(item.getId(), "T"), item, 20);
        entityManager.persistAndFlush(inventory);

        CustomerOrder order = new CustomerOrder("O1", item, 5);
        entityManager.persistAndFlush(order);

        Inventory withdrawalInventory = new Inventory(new InventoryKey(item.getId(), "W"), item, 5);
        entityManager.persistAndFlush(withdrawalInventory);

        CustomerOrder updatedOrder = new CustomerOrder("O1", item, 10);
        CustomerOrder result = orderService.updateOrder("O1", updatedOrder);

        assertEquals(updatedOrder.getQty(), result.getQty());

        Inventory updatedInventory = inventoryRepository.findById(new InventoryKey(item.getId(), "W")).orElse(null);
        assertNotNull(updatedInventory);
        assertEquals(10, updatedInventory.getQty());
    }

    @Test
    public void testDeleteOrder() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        Inventory inventory = new Inventory(new InventoryKey(item.getId(), "T"), item, 20);
        entityManager.persistAndFlush(inventory);

        CustomerOrder order = new CustomerOrder("O1", item, 5);
        entityManager.persistAndFlush(order);

        Inventory withdrawalInventory = new Inventory(new InventoryKey(item.getId(), "W"), item, 5);
        entityManager.persistAndFlush(withdrawalInventory);

        orderService.deleteOrder("O1");

        assertFalse(customerOrderRepository.findById("O1").isPresent());
        assertEquals(0, inventoryRepository.findById(new InventoryKey(item.getId(), "W")).get().getQty());
    }
}
