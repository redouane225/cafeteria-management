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

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.databinding.FragmentMenuDetailsBinding;
import com.example.cafeteriamanagement.model.MenuItem;

import java.util.ArrayList;

public class MenuDetailsFragment extends Fragment {

    private static final String ARG_MENU_ITEM = "menu_item";
    private static final String ARG_CATEGORY = "category";

    private FragmentMenuDetailsBinding binding;
    private MenuItem menuItem;

    //  UPDATED: Accept category as parameter
    public static MenuDetailsFragment newInstance(@Nullable MenuItem menuItem, @Nullable String category) {
        MenuDetailsFragment fragment = new MenuDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MENU_ITEM, menuItem);
        args.putString(ARG_CATEGORY, category); //  Correctly store the passed category
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        Log.d("MenuFlow", "Received MenuItem in MenuDetailsFragment: ");

        binding = FragmentMenuDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.menu_categories,
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.menuCategory.setAdapter(categoryAdapter);

        // Load data if editing
        if (getArguments() != null) {
            menuItem = (MenuItem) getArguments().getSerializable(ARG_MENU_ITEM);

            if (menuItem != null) {
                // Editing existing item
                binding.etitemname.setText(menuItem.getName());
                binding.editemprice.setText(String.valueOf(menuItem.getPrice()));
                binding.menuAvailability.setSelection(availabilityList.indexOf(menuItem.getIsAvailable()));
                binding.menuCategory.setSelection(categoryAdapter.getPosition(menuItem.getCategorie()));
            } else {
                // Adding new item: use category passed in arguments
                String passedCategory = getArguments().getString(ARG_CATEGORY);
                if (passedCategory != null) {
                    binding.menuCategory.setSelection(categoryAdapter.getPosition(passedCategory));
                }
            }
        }

        binding.btnSave.setOnClickListener(v -> saveMenuItem());

        return view;
    }

    private void saveMenuItem() {
        String itemName = binding.etitemname.getText().toString().trim();
        String priceText = binding.editemprice.getText().toString().trim();
        String availability = binding.menuAvailability.getSelectedItem().toString();
        String category = binding.menuCategory.getSelectedItem().toString();

        Log.d("MenuFlow", "Save clicked. Returning updated MenuItem: " + menuItem);


        if (itemName.isEmpty() || priceText.isEmpty() || category == null) {
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

        // Create or update the menu item
        if (menuItem == null) {
            int generatedId = MenuItem.getNextId();
            menuItem = new MenuItem(generatedId, itemName, itemPrice, availability, category);
        } else {
            menuItem.setName(itemName);
            menuItem.setPrice(itemPrice);
            menuItem.setIsAvailable(availability);
            menuItem.setCategorie(category);
        }

        // Pass result back to MenuFragment
        Bundle result = new Bundle();
        result.putSerializable("menu_item", menuItem);
        getParentFragmentManager().setFragmentResult("menu_item_request", result);

        Log.d("MenuFlow", "Sending result back using setFragmentResult()" + result);


        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
