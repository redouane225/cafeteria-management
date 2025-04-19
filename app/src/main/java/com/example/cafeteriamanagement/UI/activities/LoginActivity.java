package com.example.cafeteriamanagement.UI.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.model.Admin;
import com.example.cafeteriamanagement.model.User;
import com.example.cafeteriamanagement.model.Waiter;

import java.io.Serializable;
import java.util.List;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;

    // Dummy users list
    private List<User> dummyUsers = Arrays.asList(
            new Admin(2, "Admin", "Admin", "admin123"),
            new Waiter(1, "Staff", "Waiter", "waiter321", "Active")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate username and password
        User loggedInUser = null;

        for (User user : dummyUsers) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user;
                break;
            }
        }

        if (loggedInUser != null) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            Intent intent;
            if (loggedInUser.getRole().equals("Admin")) {
                intent = new Intent(LoginActivity.this , DashboardActivity.class);
            } else if (loggedInUser.getRole().equals("Waiter")) {
                intent = new Intent(this, StaffDashboardActivity.class);
            } else {
                Toast.makeText(this, "Unknown role, cannot navigate", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pass the user object
            intent.putExtra("user", (Serializable) loggedInUser);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}