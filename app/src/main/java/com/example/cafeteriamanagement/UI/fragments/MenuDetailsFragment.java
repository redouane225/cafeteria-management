package com.example.cafeteriamanagement.UI.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cafeteriamanagement.Network.ApiCallback;
import com.example.cafeteriamanagement.Network.ApiService;
import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.databinding.FragmentMenuDetailsBinding;
import com.example.cafeteriamanagement.model.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MenuDetailsFragment extends Fragment {

    private static final String ARG_MENU_ITEM = "menu_item";
    private static final String ARG_CATEGORY = "category";
    private static final String[] CATEGORIES = {"Beverages", "Bakery", "Special"};

    private FragmentMenuDetailsBinding binding;
    private MenuItem menuItem;

    public static MenuDetailsFragment newInstance(@Nullable MenuItem menuItem, @Nullable String category) {
        MenuDetailsFragment fragment = new MenuDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MENU_ITEM, menuItem);
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Hide BottomNavigationView
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.admin_btmnavbar);
        bottomNavigationView.setVisibility(View.GONE);

        // Set up Availability Spinner
        ArrayList<String> availabilityList = new ArrayList<>();
        availabilityList.add("Available");
        availabilityList.add("Unavailable");

        ArrayAdapter<String> availabilityAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                availabilityList
        );
        availabilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.menuAvailability.setAdapter(availabilityAdapter);

        // Set up Category Spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                CATEGORIES
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.menuCategory.setAdapter(categoryAdapter);

        // Load data if editing
        if (getArguments() != null) {
            menuItem = (MenuItem) getArguments().getSerializable(ARG_MENU_ITEM);

            if (menuItem != null) {
                binding.etitemname.setText(menuItem.getName());
                binding.editemprice.setText(String.valueOf(menuItem.getPrice()));
                binding.menuAvailability.setSelection(availabilityList.indexOf(menuItem.getIsAvailable()));
                binding.menuCategory.setSelection(categoryAdapter.getPosition(menuItem.getCategory()));
            } else {
                // Adding new item
                String passedCategory = getArguments().getString(ARG_CATEGORY, "Beverages"); // Default category
                binding.menuCategory.setSelection(categoryAdapter.getPosition(passedCategory));
                binding.menuAvailability.setSelection(0); // Default to "Available"
            }
        }

        // Save menu item details button
        binding.btnSave.setOnClickListener(v -> saveMenuItem());

        // Return back button
        binding.btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack(); // Go back to the previous fragment
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Restore BottomNavigationView visibility
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.admin_btmnavbar);
        bottomNavigationView.setVisibility(View.VISIBLE);

        binding = null; // Prevent memory leaks
    }

    private void saveMenuItem() {
        String itemName = binding.etitemname.getText().toString().trim();
        String priceText = binding.editemprice.getText().toString().trim();
        String availability = binding.menuAvailability.getSelectedItem().toString();
        String category = binding.menuCategory.getSelectedItem().toString();

        if (itemName.isEmpty() || priceText.isEmpty() || category.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double itemPrice;
        try {
            itemPrice = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }



        // Determine whether to create or update
        if (menuItem.getName() == null || menuItem.getName().isEmpty()) {
            // Create new menu item
            menuItem = new MenuItem(0, itemName, itemPrice, availability, category);
            ApiService.addMenuItem(menuItem, new ApiCallback<String>() {
                @Override
                public void onSuccess(String responseMessage) {
                    Toast.makeText(requireContext(), "Menu item created successfully: " + responseMessage, Toast.LENGTH_SHORT).show();
                    navigateBack();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(requireContext(), "Failed to create menu item: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Update existing menu item
            menuItem.setName(itemName);
            menuItem.setPrice(itemPrice);
            menuItem.setIsAvailable(availability);
            menuItem.setCategory(category);

            ApiService.updateMenuItem(menuItem, new ApiCallback<String>() { // Assuming updateMenuItem still expects ApiCallback<MenuItem>
                @Override
                public void onSuccess(String responseMessage) {
                    Toast.makeText(requireContext(), "Menu item updated successfully", Toast.LENGTH_SHORT).show();
                    navigateBack();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(requireContext(), "Failed to update menu item: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateBack() {
        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            getParentFragmentManager().popBackStack(); // Go back to the previous fragment
        }
    }


}