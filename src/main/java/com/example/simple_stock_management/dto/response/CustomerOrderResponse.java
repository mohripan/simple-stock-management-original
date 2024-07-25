package com.example.simple_stock_management.dto.response;

import com.example.simple_stock_management.model.CustomerOrder;

public class CustomerOrderResponse {
    private String orderNo;
    private Integer quantity;

    public CustomerOrderResponse() {
    }

    public CustomerOrderResponse(CustomerOrder order) {
        this.orderNo = order.getOrderNo().toString();
        this.quantity = order.getQyt();
    }

    public CustomerOrderResponse(String orderNo, Integer quantity) {
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
