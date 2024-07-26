package com.example.simple_stock_management.dto.response;

import com.example.simple_stock_management.model.CustomerOrder;

public class OrderResponse {
    private String orderNo;
    private Integer itemId;
    private String itemName;
    private Integer quantity;
    private Double totalPrice;

    public OrderResponse() {
    }

    public OrderResponse(CustomerOrder order) {
        this.orderNo = order.getOrderNo();
        this.itemId = order.getItem().getId();
        this.itemName = order.getItem().getName();
        this.quantity = order.getQty();
        this.totalPrice = order.getQty() * order.getItem().getPrice();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "orderNo='" + orderNo + '\'' +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", qty=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
