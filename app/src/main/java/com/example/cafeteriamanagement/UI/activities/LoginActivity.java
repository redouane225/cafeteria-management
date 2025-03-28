package com.example.cafeteriamanagement.UI.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.UI.activities.StaffDashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        executorService = Executors.newSingleThreadExecutor();

        buttonLogin.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidUsername(username) || !isValidPassword(password)) {
            Toast.makeText(this, "Invalid username or password format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform login in a background thread
        executorService.execute(() -> simulateLogin(username, password));
    }

    private boolean isValidUsername(String username) {
        // Add your username validation logic here
        return username.length() >= 3;
    }

    private boolean isValidPassword(String password) {
        // Add your password validation logic here
        return password.length() >= 6;
    }

    private void simulateLogin(String username, String password) {
        // Simulating a login response from the server
        String response = "{ \"status\": \"success\", \"role\": \"admin\" }"; // Example response

        runOnUiThread(() -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                String role = jsonResponse.getString("role");

                if (status.equals("success")) {
                    if (role.equals("admin")) {
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    } else if (role.equals("staff")) {
                        startActivity(new Intent(LoginActivity.this, StaffDashboardActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Login failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}