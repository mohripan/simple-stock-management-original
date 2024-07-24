package com.example.simple_stock_management.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class InventoryKey implements Serializable {
    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "type")
    private String type;

    public InventoryKey() {
    }

    public InventoryKey(Integer itemId, String type) {
        this.itemId = itemId;
        this.type = type;
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

    @Override
    public String toString() {
        return "InventoryKey{" +
                "itemId=" + itemId +
                ", type='" + type + '\'' +
                '}';
    }
}
