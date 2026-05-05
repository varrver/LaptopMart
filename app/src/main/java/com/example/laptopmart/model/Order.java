package com.example.laptopmart.model;

import java.util.List;

public class Order {
    private String orderId;
    private String userId;
    private String customerName;
    private String address;
    private double totalPrice;
    private String status;
    private long orderDate;
    private List<CartItem> items;

    public Order(String orderId, String userId, String customerName, String address, double totalPrice, String status, long orderDate, List<CartItem> items) {
        this.orderId = orderId;
        this.userId = userId;
        this.customerName = customerName;
        this.address = address;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
        this.items = items;
    }

    public Order() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(long orderDate) {
        this.orderDate = orderDate;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
