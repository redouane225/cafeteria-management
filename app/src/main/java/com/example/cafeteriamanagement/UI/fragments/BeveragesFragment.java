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

    private static final String ARG_MENU_ITEMS = "menu_items";

    public static BeveragesFragment newInstance(List<MenuItem> menuItems) {
        BeveragesFragment fragment = new BeveragesFragment();
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

        binding = FragmentBaveragesBinding.inflate(inflater, container, false);



        // Set up RecyclerView with GridLayoutManager
        binding.recyclerBaverages.setLayoutManager(new GridLayoutManager(getContext(), 2));
        // Set up MenuAdapter
        menuAdapter = new MenuAdapter(menuItemList, this::openMenuDetailsFragment);
        binding.recyclerBaverages.setAdapter(menuAdapter);


        return binding.getRoot();
    }

    @Override
    public void setMenuItems(List<MenuItem> menuItems) {
        // Update the menu items list and refresh the adapter
        menuItemList.clear();
        if (menuItems != null && !menuItems.isEmpty()) {
            menuItemList.addAll(menuItems);
            Log.d("MenuFlow", "BeveragesFragment received " + menuItems.size() + " items.");
        } else {
            Log.d("MenuFlow", "BeveragesFragment received an empty list of items.");
        }
        refreshData();

    }

    @Override
    public void updateCategoryMenuItem(MenuItem updatedItem) {
        if (menuAdapter == null) {
            Log.e("MenuFlow", "MenuAdapter is not initialized yet in BeveragesFragment.");
            return;
        }

        // Update a specific menu item if it exists
        menuAdapter.updateMenuItem(updatedItem);
        Log.d("MenuFlow", "BeveragesFragment updated with: " + updatedItem.getName());
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