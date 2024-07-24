package com.example.simple_stock_management.dto;

public class OrderResponse {
    private String orderNo;
    private Integer quantity;

    public OrderResponse() {
    }

    public OrderResponse(String orderNo, Integer quantity) {
        this.orderNo = orderNo;
        this.quantity = quantity;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "orderNo='" + orderNo + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
