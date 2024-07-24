package com.example.simple_stock_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_order")
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderNo;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private Integer qyt;

    public CustomerOrder() {
    }

    public CustomerOrder(Integer orderNo, Item item, Integer qyt) {
        this.orderNo = orderNo;
        this.item = item;
        this.qyt = qyt;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQyt() {
        return qyt;
    }

    public void setQyt(Integer qyt) {
        this.qyt = qyt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNo=" + orderNo +
                ", item=" + item +
                ", qyt=" + qyt +
                '}';
    }
}
