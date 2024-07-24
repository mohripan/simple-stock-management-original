package com.example.simple_stock_management.model;

import jakarta.persistence.*;

@Entity
public class Inventory {
    @EmbeddedId
    private InventoryKey id;
    private Integer qyt;

    public Inventory() {
    }

    public Inventory(InventoryKey id, Integer qyt) {
        this.id = id;
        this.qyt = qyt;
    }

    public InventoryKey getId() {
        return id;
    }

    public void setId(InventoryKey id) {
        this.id = id;
    }

    public Integer getQyt() {
        return qyt;
    }

    public void setQyt(Integer qyt) {
        this.qyt = qyt;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", qyt=" + qyt +
                '}';
    }
}
