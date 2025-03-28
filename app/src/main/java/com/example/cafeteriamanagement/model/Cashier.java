package com.example.cafeteriamanagement.model;

public class Cashier extends User {
    private boolean isActive;

    // Constructor
    public Cashier(int userId, String username, String password, boolean isActive) {
        super(userId, username, password, "Cashier");  // Le rôle est automatiquement "Cashier"
        this.isActive = isActive;
    }

    // Method to process a payment
    public void processPayment(int orderId, double amount) {
        System.out.println("Payment of $" + amount + " processed for Order " + orderId + " by " + getUsername());
    }
}