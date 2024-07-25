package com.example.simple_stock_management.model;

import jakarta.persistence.*;

@Entity
public class Inventory {
    @EmbeddedId
    private InventoryKey id;
    private Integer qty;

    public Inventory() {
    }

    public Inventory(InventoryKey id, Integer qty) {
        this.id = id;
        this.qty = qty;
    }

    public InventoryKey getId() {
        return id;
    }

    public void setId(InventoryKey id) {
        this.id = id;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", qty=" + qty +
                '}';
    }
}
