package com.example.simple_stock_management.dto.response;

public class InventoryTypeResponse {
    private String type;
    private Integer quantity;

    public InventoryTypeResponse() {
    }

    public InventoryTypeResponse(String type, Integer quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "InventoryTypeResponse{" +
                "type='" + type + '\'' +
                ", qty=" + quantity +
                '}';
    }
}
