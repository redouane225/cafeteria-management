package com.example.cafeteriamanagement.UI.fragmentsStaff;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cafeteriamanagement.UI.activities.LoginActivity;
import com.example.cafeteriamanagement.databinding.FragmentStaffProfileBinding ;
import org.json.JSONException;
import org.json.JSONObject;

public class staffProfile extends Fragment {

    private FragmentStaffProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize binding
        binding = FragmentStaffProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayUserName();

        binding.staffLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    private void displayUserName() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String userJsonString = sharedPreferences.getString("user", null);

        if (userJsonString != null) {
            try {
                JSONObject userJson = new JSONObject(userJsonString);
                String username = userJson.getString("username");

                // Set the username in the TextView
                binding.staffName.setText(username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showLogoutConfirmationDialog() {
        // Show a confirmation dialog before logging out
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performLogout() {
        // Clear SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();


        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}