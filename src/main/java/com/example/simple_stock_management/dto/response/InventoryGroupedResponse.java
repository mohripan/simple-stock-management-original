package com.example.simple_stock_management.dto.response;

import java.util.List;

public class InventoryGroupedResponse {
    private Integer itemId;
    private Integer remainingStock;
    private List<InventoryTypeResponse> types;

    public InventoryGroupedResponse() {
    }

    public InventoryGroupedResponse(Integer itemId, Integer remainingStock, List<InventoryTypeResponse> types) {
        this.itemId = itemId;
        this.remainingStock = remainingStock;
        this.types = types;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }

    public List<InventoryTypeResponse> getTypes() {
        return types;
    }

    public void setTypes(List<InventoryTypeResponse> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "InventoryGroupedResponse{" +
                "itemId=" + itemId +
                ", remainingStock=" + remainingStock +
                ", types=" + types +
                '}';
    }
}
