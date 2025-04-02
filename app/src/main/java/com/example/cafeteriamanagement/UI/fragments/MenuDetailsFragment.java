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
import com.example.cafeteriamanagement.model.Menu_item;

import java.util.ArrayList;

public class MenuDetailsFragment extends Fragment {

    private static final String ARG_MENU_ITEM = "menu_item";

    private EditText itemNameEditText;
    private EditText priceEditText;
    private Spinner availabilitySpinner;
    private Button saveButton;
    private Menu_item menuItem;

    public static MenuDetailsFragment newInstance(@Nullable Menu_item menuItem) {
        MenuDetailsFragment fragment = new MenuDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MENU_ITEM, menuItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_details, container, false);

        // Initialize views
        itemNameEditText = view.findViewById(R.id.etitemname);
        priceEditText = view.findViewById(R.id.editemprice);
        availabilitySpinner = view.findViewById(R.id.menu_availability);
        saveButton = view.findViewById(R.id.btnSave);

        // Create and set up the spinner adapter
        ArrayList<String> availability = new ArrayList<>();
        availability.add("Available");
        availability.add("Unavailable");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, availability);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availabilitySpinner.setAdapter(adapter);

        // Retrieve menu item from arguments
        if (getArguments() != null) {
            menuItem = getArguments().getParcelable(ARG_MENU_ITEM);
            if (menuItem != null) {
                itemNameEditText.setText(menuItem.getName());
                priceEditText.setText(menuItem.getPrice());
                availabilitySpinner.setSelection(availability.indexOf(menuItem.getIsAvailable()));
            }
        }

        // Set click listener for save button
        saveButton.setOnClickListener(v -> saveMenuItem());

        return view;
    }

    private void saveMenuItem() {
        String itemName = itemNameEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String availability = availabilitySpinner.getSelectedItem().toString();

        if (itemName.isEmpty() || price.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (menuItem == null) {
            menuItem = new Menu_item(id,itemName, price, availability);
        } else {
            menuItem.setId(id);
            menuItem.setName(itemName);
            menuItem.setPrice(price);
            menuItem.setIsAvailable(availability);
        }

        // Notify MenuFragment to update the menu list
        Fragment targetFragment = getTargetFragment();
        if (targetFragment instanceof MenuFragment) {
            ((MenuFragment) targetFragment).updateMenuItemList(menuItem);
        }

        // Close the fragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}