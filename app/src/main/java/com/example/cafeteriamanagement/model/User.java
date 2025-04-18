package com.example.cafeteriamanagement.model;

import java.io.Serializable;

public class User implements Serializable {
    private int userId;
    private String username;
    private String role;
    private String password ;

    // Constructor
    public User(int userId, String username, String role, String password) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.password = password;
    }
    public User() {
        // Default constructor
    }


    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }

    // Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setRole(String role) { this.role = role; }

}
