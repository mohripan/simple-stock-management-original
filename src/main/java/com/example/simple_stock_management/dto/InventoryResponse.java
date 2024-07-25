package com.example.simple_stock_management.dto;

import com.example.simple_stock_management.model.Inventory;

public class InventoryResponse {
    private Integer itemId;
    private String type;
    private Integer qty;

    public InventoryResponse() {
    }

    public InventoryResponse(Inventory inventory) {
        this.itemId = inventory.getId().getItemId();
        this.type = inventory.getId().getType();
        this.qty = inventory.getQty();
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
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
        return "InventoryResponse{" +
                "itemId=" + itemId +
                ", type='" + type + '\'' +
                ", qty=" + qty +
                '}';
    }
}
