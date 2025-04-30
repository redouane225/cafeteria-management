package com.example.cafeteriamanagement.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cafeteriamanagement.Network.ApiService;
import com.example.cafeteriamanagement.Network.ApiCallback;
import com.example.cafeteriamanagement.databinding.LogInActivityBinding;
import com.example.cafeteriamanagement.model.User;

public class LoginActivity extends AppCompatActivity {

    private LogInActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LogInActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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


                    if(user.getRole().equals("admin")){
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    }
                     else if (user.getRole().equals("staff")) {
                        Intent intent = new Intent(LoginActivity.this, StaffDashboardActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Unknown role, cannot navigate", Toast.LENGTH_SHORT).show();                    }


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
}