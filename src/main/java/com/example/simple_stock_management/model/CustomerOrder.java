package com.example.simple_stock_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_order")
public class CustomerOrder {
    @Id
    private String orderNo;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private Integer qty;

    public CustomerOrder() {
    }

    public CustomerOrder(String orderNo, Item item, Integer qty) {
        this.orderNo = orderNo;
        this.item = item;
        this.qty = qty;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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
        return "CustomerOrder{" +
                "orderNo='" + orderNo + '\'' +
                ", item=" + item +
                ", qty=" + qty +
                '}';
    }
}
