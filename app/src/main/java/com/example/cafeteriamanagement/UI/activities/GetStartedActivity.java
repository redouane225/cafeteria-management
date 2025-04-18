package com.example.cafeteriamanagement.UI.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cafeteriamanagement.R;

import android.content.Intent;

import androidx.core.view.WindowInsetsControllerCompat;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowInsetsControllerCompat insetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        insetsController.hide(WindowInsetsCompat.Type.systemBars()); // Hides status & navigation bars

        setContentView(R.layout.getstarted_activity);

        // Handle system bar insets dynamically



        findViewById(R.id.btnGetStarted).setOnClickListener(v -> {
            Intent intent = new Intent(GetStartedActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
