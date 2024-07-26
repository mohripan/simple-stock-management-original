package com.example.simple_stock_management.dto.response;

import com.example.simple_stock_management.model.CustomerOrder;

public class CustomerOrderResponse {
    private String orderNo;
    private Integer quantity;
    private Double totalPrice;

    public CustomerOrderResponse() {
    }

    public CustomerOrderResponse(CustomerOrder order) {
        this.orderNo = order.getOrderNo().toString();
        this.quantity = order.getQty();
        this.totalPrice = order.getQty() * order.getItem().getPrice();
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "CustomerOrderResponse{" +
                "orderNo='" + orderNo + '\'' +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
