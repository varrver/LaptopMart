package com.example.laptopmart.model;

public class CartItem {
    private String id;
    private String laptopId;
    private String imageUrl;
    private double price;
    private int quantity;

    public CartItem(String id, String laptopId, String imageUrl, double price, int quantity) {
        this.id = id;
        this.laptopId = laptopId;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    public CartItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLaptopId() {
        return laptopId;
    }

    public void setLaptopId(String laptopId) {
        this.laptopId = laptopId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
