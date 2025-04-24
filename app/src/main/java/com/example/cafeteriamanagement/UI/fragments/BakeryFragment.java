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

    private FragmentBakeryBinding binding;
    private MenuAdapter menuAdapter;
    private final List<MenuItem> menuItemList = new ArrayList<>();
    private static final String CATEGORY = "Bakery";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentBakeryBinding.inflate(inflater, container, false);

        // Set up RecyclerView with GridLayoutManager
        binding.recyclerBakery.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Populate dummy data for testing
        if (menuItemList.isEmpty()) {
            populateDummyData();
        }


        // Set up MenuAdapter
        menuAdapter = new MenuAdapter(menuItemList, this::openMenuDetailsFragment);
        binding.recyclerBakery.setAdapter(menuAdapter);

        return binding.getRoot();
    }

    @Override
    public void updateCategoryMenuItem(MenuItem updatedItem) {
        if (menuAdapter == null) {
            Log.e("MenuFlow", "MenuAdapter is not initialized yet in BakeryFragment.");
            return;
        }
        menuAdapter.updateMenuItem(updatedItem);
        Log.d("MenuFlow", "BakeryFragment updated with: " + updatedItem.getName());
    }

    private void populateDummyData() {
        menuItemList.clear();
        menuItemList.add(new MenuItem(1, "Croissant", 2.99, "Available", CATEGORY));
        menuItemList.add(new MenuItem(2, "Donut", 1.49, "Available", CATEGORY));
        menuItemList.add(new MenuItem(3, "Muffin", 2.79, "Unavailable", CATEGORY));
        menuItemList.add(new MenuItem(4, "Harcha", 2.00, "Unavailable", CATEGORY));
        menuItemList.add(new MenuItem(5, "Kaek", 3.00, "Available", CATEGORY));
        menuItemList.add(new MenuItem(6, "Moula", 9.99, "Available", CATEGORY));
    }

    private void openMenuDetailsFragment(MenuItem menuItem) {
        MenuDetailsFragment menuDetailsFragment = MenuDetailsFragment.newInstance(menuItem, CATEGORY);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, menuDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    public void refreshData() {
        if (menuAdapter != null) {
            menuAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}