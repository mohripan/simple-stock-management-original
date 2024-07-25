package com.example.simple_stock_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_number_counter")
public class OrderNumberCounter {
    @Id
    private String id = "ORDER";

    private int lastOrderNumber;

    public OrderNumberCounter() {
    }

    public OrderNumberCounter(int lastOrderNumber) {
        this.lastOrderNumber = lastOrderNumber;
    }

    public String getId() {
        return id;
    }

    public int getLastOrderNumber() {
        return lastOrderNumber;
    }

    public void setLastOrderNumber(int lastOrderNumber) {
        this.lastOrderNumber = lastOrderNumber;
    }
}
