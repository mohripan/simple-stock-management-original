package com.example.simple_stock_management.dto.request;

public class InventoryRequest {
    private Integer itemId;
    private Integer qty;
    private String type;

    public InventoryRequest() {
    }

    public InventoryRequest(Integer itemId, Integer qty, String type) {
        this.itemId = itemId;
        this.qty = qty;
        this.type = type;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "InventoryRequest{" +
                "itemId=" + itemId +
                ", qty=" + qty +
                ", type='" + type + '\'' +
                '}';
    }
}
