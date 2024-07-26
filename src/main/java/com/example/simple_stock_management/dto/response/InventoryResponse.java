package com.example.simple_stock_management.dto.response;

import com.example.simple_stock_management.model.Inventory;

public class InventoryResponse {
    private Integer itemId;
    private String type;
    private Integer quantity;
    private Integer remainingStock;

    public InventoryResponse() {
    }

    public InventoryResponse(Integer itemId, String type, Integer quantity, Integer remainingStock) {
        this.itemId = itemId;
        this.type = type;
        this.quantity = quantity;
        this.remainingStock = remainingStock;
    }

    public InventoryResponse(Inventory inventory, Integer remainingStock) {
        itemId = inventory.getId().getItemId();
        type = inventory.getId().getType();
        quantity = inventory.getQty();
        this.remainingStock = remainingStock;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }

    @Override
    public String toString() {
        return "InventoryResponse{" +
                "itemId=" + itemId +
                ", type='" + type + '\'' +
                ", qty=" + quantity +
                ", remainingStock=" + remainingStock +
                '}';
    }
}
