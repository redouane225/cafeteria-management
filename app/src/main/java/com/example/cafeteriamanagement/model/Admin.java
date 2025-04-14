package com.example.cafeteriamanagement.model;

public class Admin extends User {


    // Constructor
    public Admin(int userId, String username, String role ,String password) {
        super(userId, username, "Admin",password);
    }


}