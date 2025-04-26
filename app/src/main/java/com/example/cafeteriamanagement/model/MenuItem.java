package com.example.cafeteriamanagement.model;

import java.io.Serializable;

public class MenuItem implements Serializable {



    private int id;
    private String name;
    private double price;
    private String isAvailable;
    private String category;

    // Constructor
    public MenuItem(int id, String name, double price, String isAvailable, String categorie) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
        this.category = categorie;
    }
    public MenuItem(){}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", isAvailable='" + isAvailable + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

}
