package com.example.simple_stock_management.model;

import jakarta.persistence.*;

@Entity
public class Inventory {
    @EmbeddedId
    private InventoryKey id;
    @MapsId("itemId")
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    private Integer qty;

    public Inventory() {
    }

    public Inventory(InventoryKey id, Item item, Integer qty) {
        this.id = id;
        this.item = item;
        this.qty = qty;
    }

    public InventoryKey getId() {
        return id;
    }

    public void setId(InventoryKey id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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
                ", item=" + item +
                ", qty=" + qty +
                '}';
    }
}
