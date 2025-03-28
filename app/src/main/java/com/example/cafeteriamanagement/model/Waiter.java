package com.example.cafeteriamanagement.model;

    public class Waiter extends User {
        private boolean isActive;

        // Constructor
        public Waiter(int userId, String username, String password) {
            super(userId, username, password, "Waiter");  // Le rôle est automatiquement "Waiter"
        }

        // Method to add an order
        public void addOrder(int orderId) {
            System.out.println("Order " + orderId + " has been added by " + getUsername());
        }

        // Method to update an order
        public void updateOrder(int orderId) {
            System.out.println("Order " + orderId + " has been updated by " + getUsername());
        }

        // Method to cancel an order
        public void cancelOrder(int orderId) {
            System.out.println("Order " + orderId + " has been canceled by " + getUsername());
        }
}
