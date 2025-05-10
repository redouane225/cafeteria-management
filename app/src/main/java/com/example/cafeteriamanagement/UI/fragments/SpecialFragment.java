package com.example.cafeteriamanagement.UI.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriamanagement.Adapter.MenuAdapter;
import com.example.cafeteriamanagement.Adapter.staffMenuAdapter;
import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.databinding.FragmentSpecialBinding;
import com.example.cafeteriamanagement.model.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SpecialFragment extends Fragment implements MenuCategoryInterface {

    private FragmentSpecialBinding binding;
    private RecyclerView.Adapter<?> menuAdapter;
    private final List<MenuItem> menuItemList = new ArrayList<>();
    private static final String CATEGORY = "Special";
    private static final String ARG_MENU_ITEMS = "menu_items";

    private String userRole;

    public static SpecialFragment newInstance(List<MenuItem> menuItems) {
        SpecialFragment fragment = new SpecialFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MENU_ITEMS, (ArrayList<MenuItem>) menuItems);
        fragment.setArguments(args);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSpecialBinding.inflate(inflater, container, false);

        //  Retrieve the user's role
        fetchUserRole();

        // Set up RecyclerView with GridLayoutManager
        binding.recyclerSpecial.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        // Set up the correct adapter based on the user's role
        if ("admin".equalsIgnoreCase(userRole)) {
            menuAdapter = new MenuAdapter(menuItemList, this::openMenuDetailsFragment); // Admin adapter with full functionality
        } else {
            menuAdapter = new staffMenuAdapter(menuItemList); // Staff adapter (view-only)
        }
        binding.recyclerSpecial.setAdapter(menuAdapter);


        return binding.getRoot();
    }

    @Override
    public void setMenuItems(List<MenuItem> menuItems) {
        // Update the menu items list and refresh the adapter
        menuItemList.clear();
        if (menuItems != null && !menuItems.isEmpty()) {
            menuItemList.addAll(menuItems);
            Log.d("MenuFlow", "SpecialFragment received " + menuItems.size() + " items.");
        } else {
            Log.d("MenuFlow", "SpecialFragment received an empty list of items.");
        }
        refreshData();

    }

    @Override
    public void updateCategoryMenuItem(MenuItem updatedItem) {
        if (!"admin".equalsIgnoreCase(userRole)) {
            Log.w("MenuFlow", "updateCategoryMenuItem called, but user role is not admin.");
            return; // Prevent staff from updating items
        }

        if (menuAdapter instanceof MenuAdapter) {
            ((MenuAdapter) menuAdapter).updateMenuItem(updatedItem);
            Log.d("MenuFlow", "SpecialFragment updated with: " + updatedItem.getName());
        } else {
            Log.e("MenuFlow", "MenuAdapter is not initialized for admin in SpecialFragment.");
        }
    }

    private void openMenuDetailsFragment(MenuItem menuItem) {

        // Prevent staff from opening details
        if (!"admin".equalsIgnoreCase(userRole)) {
            Log.w("MenuFlow", "openMenuDetailsFragment called, but user role is not admin.");
            return;
        }
        // open menu details when role is admin
        MenuDetailsFragment menuDetailsFragment = MenuDetailsFragment.newInstance(menuItem, CATEGORY);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, menuDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    public void refreshData() {
        if (menuAdapter != null) {
            // Notify RecyclerView adapter to refresh the UI
            menuAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }

    private void fetchUserRole() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String userJsonString = sharedPreferences.getString("user", null);

        if (userJsonString != null) {
            try {
                JSONObject userJson = new JSONObject(userJsonString);
                userRole = userJson.getString("role"); // Assuming the role is stored as 'role' in the JSON

                Log.d("MenuFlow", "User role fetched: " + userRole);
            } catch (JSONException e) {
                e.printStackTrace();
                userRole = "staff"; // Default to staff in case of an error
                Log.e("MenuFlow", "Error parsing user role. Defaulting to 'staff'.");
            }
        } else {
            userRole = "staff"; // Default role if no user data is found
            Log.e("MenuFlow", "No user data found. Defaulting to 'staff'.");
        }
    }
}