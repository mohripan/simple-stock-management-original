package com.example.simple_stock_management;

import com.example.simple_stock_management.dto.response.ItemResponse;
import com.example.simple_stock_management.model.Item;
import com.example.simple_stock_management.services.ItemService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ItemControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private Item item;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
    }

    @Test
    public void testGetItems() throws Exception {
        Page<Item> items = new PageImpl<>(Arrays.asList(item), PageRequest.of(0, 10), 1);
        when(itemService.getItems(any(Pageable.class), eq(null), eq(null), eq(null))).thenReturn(items);

        mockMvc.perform(get("/items")
                        .param("page", "1")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.values").isArray())
                .andExpect(jsonPath("$.search.pageAt").value(1));
    }

    @Test
    public void testGetItemsWithFilter() throws Exception {
        Item filteredItem = new Item();
        filteredItem.setName("Pen");
        filteredItem.setPrice(1.5);
        Page<Item> items = new PageImpl<>(Arrays.asList(filteredItem), PageRequest.of(0, 10), 1);
        when(itemService.getItems(any(Pageable.class), eq("Pen"), eq(1.0), eq(2.0))).thenReturn(items);

        mockMvc.perform(get("/items")
                        .param("page", "1")
                        .param("size", "10")
                        .param("name", "Pen")
                        .param("minPrice", "1")
                        .param("maxPrice", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.values[0].name").value("Pen"))
                .andExpect(jsonPath("$.values[0].price").value(1.5))
                .andExpect(jsonPath("$.search.pageAt").value(1));
    }

    @Test
    public void testGetItemById() throws Exception {
        ItemResponse itemResponse = new ItemResponse(item);
        when(itemService.getItemResponseById(1, null, null)).thenReturn(itemResponse);

        mockMvc.perform(get("/items/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Item"));
    }

    @Test
    public void testCreateItem() throws Exception {
        when(itemService.saveItem(any(Item.class))).thenReturn(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Item\", \"price\": 10.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Item"));
    }

    @Test
    public void testUpdateItem() throws Exception {
        Item updatedItem = new Item();
        updatedItem.setId(1);
        updatedItem.setName("Updated Item");
        updatedItem.setPrice(15.0);

        when(itemService.updateItem(eq(1), any(Item.class))).thenReturn(updatedItem);

        mockMvc.perform(put("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Item\", \"price\": 15.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Item"));
    }

    @Test
    public void testDeleteItem() throws Exception {
        doNothing().when(itemService).deleteItem(1);

        mockMvc.perform(delete("/items/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
