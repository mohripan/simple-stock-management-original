package com.example.simple_stock_management.dto;

import java.util.List;

public class ItemResponse {
    private Long itemId;
    private String name;
    private Double price;
    private Integer remainingStock;
    private List<OrderResponse> orderHistory;

    public ItemResponse() {
    }

    public ItemResponse(Long itemId, String name, Double price, Integer remainingStock, List<OrderResponse> orderHistory) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.remainingStock = remainingStock;
        this.orderHistory = orderHistory;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
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

    public Integer getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }

    public List<OrderResponse> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderResponse> orderHistory) {
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
