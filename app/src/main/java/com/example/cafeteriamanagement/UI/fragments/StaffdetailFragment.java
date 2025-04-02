package com.example.cafeteriamanagement.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cafeteriamanagement.R;

import java.util.ArrayList;

public class StaffdetailFragment extends Fragment {

    private EditText staffNameEditText;
    private EditText staffRoleEditText;
    private Spinner availabilitySpinner;
    private Button saveButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staffdetail, container, false);

        // Initialize views
        staffNameEditText = view.findViewById(R.id.stfName);
        staffRoleEditText = view.findViewById(R.id.stfRole);
        availabilitySpinner = view.findViewById(R.id.stf_status);
        saveButton = view.findViewById(R.id.btnSave);

        // Create and set up the spinner adapter
        ArrayList<String> availability = new ArrayList<>();
        availability.add("Full-time");
        availability.add("Part-time");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, availability);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availabilitySpinner.setAdapter(adapter);

        // Set click listener for save button
        saveButton.setOnClickListener(v -> saveStaffDetails());

        return view;
    }

    private void saveStaffDetails() {
        String staffName = staffNameEditText.getText().toString().trim();
        String staffRole = staffRoleEditText.getText().toString().trim();
        String availability = availabilitySpinner.getSelectedItem().toString();

        if (staffName.isEmpty() || staffRole.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save staff details (Implement your database logic here)
        Toast.makeText(requireContext(), "Staff details saved successfully", Toast.LENGTH_SHORT).show();

        // Optionally, clear the form
        staffNameEditText.setText("");
        staffRoleEditText.setText("");
        availabilitySpinner.setSelection(0);
    }
}