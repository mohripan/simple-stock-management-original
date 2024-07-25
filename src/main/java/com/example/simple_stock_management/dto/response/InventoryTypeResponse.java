package com.example.simple_stock_management.dto.response;

public class InventoryTypeResponse {
    private String type;
    private Integer qty;

    public InventoryTypeResponse() {
    }

    public InventoryTypeResponse(String type, Integer qty) {
        this.type = type;
        this.qty = qty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "InventoryTypeResponse{" +
                "type='" + type + '\'' +
                ", qty=" + qty +
                '}';
    }
}
