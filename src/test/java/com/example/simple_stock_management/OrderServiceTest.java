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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
    public void testGetOrderDetails() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        entityManager.persistAndFlush(item);

        CustomerOrder order = new CustomerOrder("O1", item, 5);
        entityManager.persistAndFlush(order);

        CustomerOrder foundOrder = orderService.getOrderDetails("O1");

        assertNotNull(foundOrder);
        assertEquals("O1", foundOrder.getOrderNo());
        assertEquals(5, foundOrder.getQty());
        assertEquals(item.getId(), foundOrder.getItem().getId());
    }

    @Test
    public void testListOrders() {
        Item item1 = new Item();
        item1.setName("Test Item 1");
        item1.setPrice(10.0);
        entityManager.persistAndFlush(item1);

        Item item2 = new Item();
        item2.setName("Test Item 2");
        item2.setPrice(15.0);
        entityManager.persistAndFlush(item2);

        CustomerOrder order1 = new CustomerOrder("O1", item1, 5);
        entityManager.persistAndFlush(order1);

        CustomerOrder order2 = new CustomerOrder("O2", item2, 3);
        entityManager.persistAndFlush(order2);

        Page<CustomerOrder> orders = orderService.listOrders(PageRequest.of(0, 10), null);

        assertNotNull(orders);
        assertEquals(2, orders.getTotalElements());
    }

    @Test
    public void testListOrdersWithItemId() {
        Item item1 = new Item();
        item1.setName("Test Item 1");
        item1.setPrice(10.0);
        entityManager.persistAndFlush(item1);

        Item item2 = new Item();
        item2.setName("Test Item 2");
        item2.setPrice(15.0);
        entityManager.persistAndFlush(item2);

        CustomerOrder order1 = new CustomerOrder("O1", item1, 5);
        entityManager.persistAndFlush(order1);

        CustomerOrder order2 = new CustomerOrder("O2", item2, 3);
        entityManager.persistAndFlush(order2);

        Page<CustomerOrder> orders = orderService.listOrders(PageRequest.of(0, 10), item1.getId());

        assertNotNull(orders);
        assertEquals(1, orders.getTotalElements());
        assertEquals(item1.getId(), orders.getContent().get(0).getItem().getId());
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

        int updatedQty = 10;
        CustomerOrder result = orderService.updateOrder("O1", updatedQty);

        assertEquals(updatedQty, result.getQty());

        Inventory updatedInventory = inventoryRepository.findById(new InventoryKey(item.getId(), "W")).orElse(null);
        assertNotNull(updatedInventory);
        assertEquals(updatedQty, updatedInventory.getQty());
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
    }
}
