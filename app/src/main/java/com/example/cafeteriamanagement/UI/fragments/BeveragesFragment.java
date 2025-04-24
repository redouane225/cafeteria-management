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
import com.example.cafeteriamanagement.databinding.FragmentBaveragesBinding;
import com.example.cafeteriamanagement.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class BeveragesFragment extends Fragment implements MenuCategoryInterface {

    private FragmentBaveragesBinding binding;
    private MenuAdapter menuAdapter;
    private final List<MenuItem> menuItemList = new ArrayList<>();
    private static final String CATEGORY = "Beverages";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentBaveragesBinding.inflate(inflater, container, false);

        // Set up RecyclerView with GridLayoutManager
        binding.recyclerBaverages.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Populate dummy data for testing
        if (menuItemList.isEmpty()) {
            populateDummyData();
        }


        // Set up MenuAdapter
        menuAdapter = new MenuAdapter(menuItemList, this::openMenuDetailsFragment);
        binding.recyclerBaverages.setAdapter(menuAdapter);

        return binding.getRoot();
    }

    @Override
    public void updateCategoryMenuItem(MenuItem updatedItem) {
        if (menuAdapter == null) {
            Log.e("MenuFlow", "MenuAdapter is not initialized yet in BeveragesFragment.");
            return;
        }
        menuAdapter.updateMenuItem(updatedItem);
        Log.d("MenuFlow", "BeveragesFragment updated with: " + updatedItem.getName());
    }

    private void populateDummyData() {
        menuItemList.clear();
        menuItemList.add(new MenuItem(10, "Cappuccino", 3.49, "Available", CATEGORY));
        menuItemList.add(new MenuItem(11, "Latte", 3.99, "Available", CATEGORY));
        menuItemList.add(new MenuItem(12, "Iced Tea", 2.49, "Unavailable", CATEGORY));
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