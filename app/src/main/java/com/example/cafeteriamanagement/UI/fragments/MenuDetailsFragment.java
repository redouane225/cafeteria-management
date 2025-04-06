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
import com.example.cafeteriamanagement.model.MenuItem;

import java.util.ArrayList;

public class MenuDetailsFragment extends Fragment {

    private static final String ARG_MENU_ITEM = "menu_item";

    private EditText itemNameEditText;
    private EditText priceEditText;
    private Spinner availabilitySpinner;
    private Spinner categorySpinner;  // New category spinner
    private Button saveButton;
    private MenuItem menuItem;

    public static MenuDetailsFragment newInstance(@Nullable MenuItem menuItem) {
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
        categorySpinner = view.findViewById(R.id.menu_category);  // Initialize category spinner
        saveButton = view.findViewById(R.id.btnSave);

        // Create and set up the availability spinner adapter
        ArrayList<String> availability = new ArrayList<>();
        availability.add("Available");
        availability.add("Unavailable");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, availability);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availabilitySpinner.setAdapter(adapter);

        // Create and set up the category spinner adapter
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.menu_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Retrieve menu item from arguments
        if (getArguments() != null) {
            menuItem = getArguments().getParcelable(ARG_MENU_ITEM);
            if (menuItem != null) {
                itemNameEditText.setText(menuItem.getName());
                priceEditText.setText(String.valueOf(menuItem.getPrice()));
                availabilitySpinner.setSelection(availability.indexOf(menuItem.getIsAvailable()));
                categorySpinner.setSelection(categoryAdapter.getPosition(menuItem.getCategorie()));  // Set category spinner selection
            }
        }

        // Set click listener for save button
        saveButton.setOnClickListener(v -> saveMenuItem());

        return view;
    }

    private void saveMenuItem() {
        String itemName = itemNameEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        double itemPrice;

        try {
            itemPrice = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }
        String availability = availabilitySpinner.getSelectedItem().toString();
        String category = categorySpinner.getSelectedItem().toString();  // Get selected category

        if (itemName.isEmpty() || price.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (menuItem == null) {
            menuItem = new MenuItem(0, itemName, itemPrice, availability, category);
        } else {
            menuItem.setName(itemName);
            menuItem.setPrice(itemPrice);
            menuItem.setIsAvailable(availability);
            menuItem.setCategorie(category);  // Set category
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