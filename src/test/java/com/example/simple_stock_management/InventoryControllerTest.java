package com.example.simple_stock_management;

import com.example.simple_stock_management.dto.response.InventoryGroupedResponse;
import com.example.simple_stock_management.dto.response.InventoryResponse;
import com.example.simple_stock_management.dto.response.InventoryTypeResponse;
import com.example.simple_stock_management.model.Inventory;
import com.example.simple_stock_management.model.InventoryKey;
import com.example.simple_stock_management.model.Item;
import com.example.simple_stock_management.repository.ItemRepository;
import com.example.simple_stock_management.services.InventoryService;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class InventoryControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @MockBean
    private ItemRepository itemRepository;

    private Inventory inventory;
    private InventoryKey inventoryKey;
    private Item item;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        item = new Item();
        item.setId(1);  // Ensure the item ID is set
        item.setName("Test Item");
        item.setPrice(10.0);

        inventoryKey = new InventoryKey(item.getId(), "T");
        inventory = new Inventory(inventoryKey, item, 10);
    }

    @Test
    public void testGetInventory() throws Exception {
        InventoryGroupedResponse response = new InventoryGroupedResponse(inventoryKey.getItemId(), 10, Arrays.asList(new InventoryTypeResponse("T", 10)));
        when(inventoryService.getInventoryByItemId(1)).thenReturn(response);

        mockMvc.perform(get("/inventories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.remainingStock").value(10))
                .andExpect(jsonPath("$.types[0].type").value("T"))
                .andExpect(jsonPath("$.types[0].qty").value(10));
    }

    @Test
    public void testListInventories() throws Exception {
        Inventory inventoryT = new Inventory(new InventoryKey(1, "T"), item, 10);
        Inventory inventoryW = new Inventory(new InventoryKey(1, "W"), item, 0);
        Page<Inventory> page = new PageImpl<>(Arrays.asList(inventoryT, inventoryW), PageRequest.of(0, 10), 2);

        when(inventoryService.listInventories(any(Pageable.class))).thenReturn(page);
        when(inventoryService.calculateRemainingStock(1)).thenReturn(10);

        mockMvc.perform(get("/inventories")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.values").isArray())
                .andExpect(jsonPath("$.values[0].itemId").value(1))
                .andExpect(jsonPath("$.values[0].remainingStock").value(10))
                .andExpect(jsonPath("$.values[0].types[0].type").value("T"))
                .andExpect(jsonPath("$.values[0].types[0].qty").value(10));
    }

    @Test
    public void testSaveInventory() throws Exception {
        InventoryResponse response = new InventoryResponse(inventory, 10);
        when(inventoryService.saveInventory(any(Inventory.class))).thenReturn(inventory);
        when(inventoryService.calculateRemainingStock(anyInt())).thenReturn(10);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));  // Mock the item repository

        mockMvc.perform(post("/inventories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\": 1, \"qty\": 10, \"type\": \"T\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.type").value("T"))
                .andExpect(jsonPath("$.qty").value(10))
                .andExpect(jsonPath("$.remainingStock").value(10));
    }

    @Test
    public void testUpdateInventory() throws Exception {
        InventoryKey key = new InventoryKey(1, "T");
        Inventory updatedInventory = new Inventory(key, item, 20);

        when(inventoryService.updateInventory(any(Inventory.class))).thenReturn(updatedInventory);
        when(inventoryService.calculateRemainingStock(anyInt())).thenReturn(10);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));  // Mock the item repository

        mockMvc.perform(put("/inventories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\": 1, \"qty\": 20, \"type\": \"T\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.type").value("T"))
                .andExpect(jsonPath("$.qty").value(20))
                .andExpect(jsonPath("$.remainingStock").value(10));
    }

    @Test
    public void testDeleteInventory() throws Exception {
        doNothing().when(inventoryService).deleteInventoryByItemId(1);

        mockMvc.perform(delete("/inventories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}