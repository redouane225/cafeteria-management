package com.example.cafeteriamanagement.model;
import com.example.cafeteriamanagement.model.OrderItem;


import java.util.List;

public class Order {
    private int id;
    private int tableNumber; // Changed from String to int
    private String specialRequest;
    private List<OrderItem> items;

    public Order(int id, int tableNumber, String specialRequest, List<OrderItem> items) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.specialRequest = specialRequest;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public int getTableNumber() { // Updated from String to int
        return tableNumber;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}