package com.example.cafeteriamanagement.model;

import java.io.Serializable;

public class Waiter extends User implements Serializable {
    private String status;

    public Waiter(int userId, String username, String role,String password ,  String status) {
        super(userId, username, "Waiter",password);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return "Active".equalsIgnoreCase(status);
    }

    // All the following methods must be inside the class
    public void addOrder(int orderId) {
        System.out.println("Order " + orderId + " has been added by " + getUsername());
    }

    public void updateOrder(int orderId) {
        System.out.println("Order " + orderId + " has been updated by " + getUsername());
    }

    public void cancelOrder(int orderId) {
        System.out.println("Order " + orderId + " has been canceled by " + getUsername());
    }
}
