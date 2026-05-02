package com.example.laptopmart.model;

public class Laptop {

    private String id;
    private String name;
    private String brand;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;

    public Laptop(String id, String name, String brand, String description, double price, int stock, String imageUrl) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public Laptop() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
