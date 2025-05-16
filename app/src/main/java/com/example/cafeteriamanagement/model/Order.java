package com.example.cafeteriamanagement.model;
import com.example.cafeteriamanagement.model.OrderItem;


import java.util.List;

public class Order {
    private int id;
    private int tableNumber; // Changed from String to int
    private String specialRequest;
    private String status;
    private List<OrderItem> items;

    public Order(int id, int tableNumber, String specialRequest, List<OrderItem> items,String status) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.specialRequest = specialRequest;
        this.items = items;
        this.status=status;
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
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void toggleStatus() {
        if (status.equals("in progress")) {
            status = "completed";
        } else {
            status = "in progress";
        }
    }
}