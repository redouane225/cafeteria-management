package com.example.cafeteriamanagement.UI.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.cafeteriamanagement.Adapter.MenuAdapter;
import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.databinding.FragmentBakeryBinding;
import com.example.cafeteriamanagement.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class BakeryFragment extends Fragment implements MenuCategoryInterface {

    private FragmentBakeryBinding binding; // View binding
    private MenuAdapter menuAdapter;
    private final List<MenuItem> menuItemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentBakeryBinding.inflate(inflater, container, false);
        View view = binding.getRoot(); // Get root view

        binding.recyclerBakery.setLayoutManager(new GridLayoutManager(getContext(), 2)); // Using binding to access recyclerView

        populateDummyData();

        menuAdapter = new MenuAdapter(menuItemList, this::openMenuDetailsFragment);
        binding.recyclerBakery.setAdapter(menuAdapter); // Set adapter

        // Fragment ResultListener using Serializable


        return view;
    }
    public void  updateCategoryMenuItem(MenuItem updatedItem) {
        if (menuAdapter != null) {
            menuAdapter.updateMenuItem(updatedItem);
            Log.d("MenuFlow", "BakeryFragment updated with: " + updatedItem.getName());
        } else {
            Log.d("MenuFlow", "menuAdapter is null in BakeryFragment.");
        }
    }

    private void populateDummyData() {
        menuItemList.clear();
        menuItemList.add(new MenuItem(1, "Croissant", 2.99, "Available", "Bakery"));
        menuItemList.add(new MenuItem(2, "Donut", 1.49, "Available", "Bakery"));
        menuItemList.add(new MenuItem(3, "Muffin", 2.79, "Unavailable", "Bakery"));
        menuItemList.add(new MenuItem(4, "Harcha", 2.00, "Unavailable", "Bakery"));
        menuItemList.add(new MenuItem(5, "Kaek", 3.00, "Available", "Bakery"));
        menuItemList.add(new MenuItem(6, "Moula", 9.99, "Available", "Bakery"));
    }

    // Update this method to pass both the menuItem and the category
    private void openMenuDetailsFragment(MenuItem menuItem) {
        MenuDetailsFragment menuDetailsFragment = MenuDetailsFragment.newInstance(menuItem, "Bakery"); // Pass category
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, menuDetailsFragment)
                .addToBackStack(null)
                .commit();
    }


    public void refreshData() {
        menuAdapter.notifyDataSetChanged();
    }



}
