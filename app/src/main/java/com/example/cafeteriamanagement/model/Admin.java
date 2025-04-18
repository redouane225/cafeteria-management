package com.example.cafeteriamanagement.model;

import java.io.Serializable;

public class Admin extends User implements Serializable {


    // Constructor
    public Admin(int userId, String username, String role ,String password) {
        super(userId, username, "Admin",password);
    }


}