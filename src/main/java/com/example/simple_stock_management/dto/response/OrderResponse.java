package com.example.simple_stock_management.dto.response;

import com.example.simple_stock_management.model.CustomerOrder;

public class OrderResponse {
    private String orderNo;
    private Integer itemId;
    private String itemName;
    private Integer qty;

    public OrderResponse() {
    }

    public OrderResponse(CustomerOrder order) {
        this.orderNo = order.getOrderNo();
        this.itemId = order.getItem().getId();
        this.itemName = order.getItem().getName();
        this.qty = order.getQty();
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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "orderNo='" + orderNo + '\'' +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", qty=" + qty +
                '}';
    }
}
