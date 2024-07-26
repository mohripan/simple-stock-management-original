package com.example.simple_stock_management;

import com.example.simple_stock_management.model.CustomerOrder;
import com.example.simple_stock_management.model.Item;
import com.example.simple_stock_management.repository.ItemRepository;
import com.example.simple_stock_management.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class OrderControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ItemRepository itemRepository;

    private Item item;
    private CustomerOrder order;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        item = new Item();
        item.setId(1);
        item.setName("Test Item");
        item.setPrice(10.0);

        order = new CustomerOrder();
        order.setOrderNo("O1");
        order.setItem(item);
        order.setQty(5);
    }

    @Test
    public void testSaveOrder() throws Exception {
        when(orderService.saveOrder(any(CustomerOrder.class))).thenReturn(order);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\": 1, \"qty\": 5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNo").value("O1"))
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.itemName").value("Test Item"))
                .andExpect(jsonPath("$.qty").value(5));
    }

    @Test
    public void testUpdateOrder() throws Exception {
        CustomerOrder updatedOrder = new CustomerOrder();
        updatedOrder.setOrderNo("O1");
        updatedOrder.setItem(item);
        updatedOrder.setQty(10);

        when(orderService.updateOrder(eq("O1"), anyInt())).thenReturn(updatedOrder);

        mockMvc.perform(put("/orders/O1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"qty\": 10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNo").value("O1"))
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.itemName").value("Test Item"))
                .andExpect(jsonPath("$.qty").value(10));
    }

    @Test
    public void testListOrders() throws Exception {
        Page<CustomerOrder> page = new PageImpl<>(Arrays.asList(order), PageRequest.of(0, 10), 1);
        when(orderService.listOrders(any(Pageable.class), any())).thenReturn(page);

        mockMvc.perform(get("/orders")
                        .param("page", "1")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.values").isArray())
                .andExpect(jsonPath("$.values[0].orderNo").value("O1"))
                .andExpect(jsonPath("$.values[0].itemId").value(1))
                .andExpect(jsonPath("$.values[0].itemName").value("Test Item"))
                .andExpect(jsonPath("$.values[0].qty").value(5))
                .andExpect(jsonPath("$.search.pageAt").value(1));
    }

    @Test
    public void testGetOrderDetails() throws Exception {
        when(orderService.getOrderDetails("O1")).thenReturn(order);

        mockMvc.perform(get("/orders/O1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNo").value("O1"))
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.itemName").value("Test Item"))
                .andExpect(jsonPath("$.qty").value(5));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder("O1");

        mockMvc.perform(delete("/orders/O1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}

