package com.example.laptopmart.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private String userId;
    private String customerName;
    private String address;
    private double totalPrice;
    private String status;
    private long orderDate;
    private List<CartItem> items;
    private String shippingMethod;
    private String paymentMethod;
    private String bankAccount;
    private String notes;

    // 2. Update your constructor to include them!
    public Order(String orderId, String userId, String customerName, String address, String shippingMethod, String paymentMethod, String bankAccount, String notes, double totalPrice, String status, long orderDate, List<CartItem> items) {
        this.orderId = orderId;
        this.userId = userId;
        this.customerName = customerName;
        this.address = address;
        this.shippingMethod = shippingMethod; // NEW
        this.paymentMethod = paymentMethod;   // NEW
        this.bankAccount = bankAccount;
        this.notes = notes;
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

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
