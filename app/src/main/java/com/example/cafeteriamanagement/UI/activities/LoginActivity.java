package com.example.cafeteriamanagement.UI.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cafeteriamanagement.Network.ApiService;
import com.example.cafeteriamanagement.Network.ApiCallback;
import com.example.cafeteriamanagement.databinding.LogInActivityBinding;
import com.example.cafeteriamanagement.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private LogInActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LogInActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check if user is already logged in
        if (isUserLoggedIn()) {
            navigateToDashboard(); // Automatically navigate to the appropriate dashboard
        }

        binding.buttonLogin.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        // Get username and password from input fields
        String username = binding.editTextUsername.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        // Validate input fields
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call the login API
        ApiService.login(username, password, new ApiCallback<String>() {
            @Override
            public void onSuccess(String role) {
                // Process success on the UI thread
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    // Create a minimal User object
                    User user = new User();
                    user.setUsername(username); // Set username
                    user.setRole(role);         // Set role returned by the backend

                    // Save user data and login state in SharedPreferences
                    saveUserSession(user);

                    // Navigate to appropriate dashboard
                    if (user.getRole().equals("admin")) {
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    } else if (user.getRole().equals("staff")) {
                        Intent intent = new Intent(LoginActivity.this, StaffDashboardActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Unknown role, cannot navigate", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error on the UI thread
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void saveUserSession(User user) {
        // Save login state and user data in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert User object to JSONObject
        try {
            JSONObject userJson = new JSONObject();
            userJson.put("username", user.getUsername());
            userJson.put("role", user.getRole());

            editor.putString("user", userJson.toString()); // Save user data as a JSON string
            editor.putBoolean("isLoggedIn", true); // Save login state
            editor.apply(); // Commit changes
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isUserLoggedIn() {
        // Check login state from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false); // Default is false
    }

    private void navigateToDashboard() {
        // Retrieve user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String userJsonString = sharedPreferences.getString("user", null);

        if (userJsonString != null) {
            try {
                JSONObject userJson = new JSONObject(userJsonString);
                String username = userJson.getString("username");
                String role = userJson.getString("role");

                // Create a User object from JSON data
                User user = new User();
                user.setUsername(username);
                user.setRole(role);

                // Navigate to appropriate dashboard
                if (user.getRole().equals("admin")) {
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                } else if (user.getRole().equals("staff")) {
                    Intent intent = new Intent(LoginActivity.this, StaffDashboardActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}