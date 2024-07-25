package com.example.simple_stock_management.dto.response;

import com.example.simple_stock_management.model.Item;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ItemResponse {
    private Integer itemId;
    private String name;
    private Double price;
    private Integer remainingStock;
    private List<CustomerOrderResponse> orderHistory;

    public ItemResponse() {
    }

    public ItemResponse(Integer itemId, String name, Double price) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
    }

    public ItemResponse(Integer itemId, String name, Double price, Integer remainingStock) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.remainingStock = remainingStock;
    }

    public ItemResponse(Integer itemId, String name, Double price, Integer remainingStock, List<CustomerOrderResponse> orderHistory) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.remainingStock = remainingStock;
        this.orderHistory = orderHistory;
    }

    public ItemResponse(Item item) {
        this.itemId = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<CustomerOrderResponse> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<CustomerOrderResponse> orderHistory) {
        this.orderHistory = orderHistory;
    }

    @Override
    public String toString() {
        return "ItemResponse{" +
                "itemId=" + itemId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", remainingStock=" + remainingStock +
                ", orderHistory=" + orderHistory +
                '}';
    }
}
