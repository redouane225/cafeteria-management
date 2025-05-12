package com.example.cafeteriamanagement.UI.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cafeteriamanagement.Adapter.MenuPagerAdapter;
import com.example.cafeteriamanagement.Network.ApiCallback;
import com.example.cafeteriamanagement.Network.ApiService;
import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.databinding.FragmentMenuBinding;
import com.example.cafeteriamanagement.model.MenuItem;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;
    private MenuPagerAdapter menuPagerAdapter;
    private static final String[] CATEGORIES = {"Beverages", "Bakery", "Special"};
    private final Map<String, List<MenuItem>> categorizedItems = new HashMap<>(); // Store data for lazy fragments

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize categorizedItems with empty lists
        for (String category : CATEGORIES) {
            categorizedItems.put(category, new ArrayList<>());
        }

        // Set up ViewPager2 with Adapter
        menuPagerAdapter = new MenuPagerAdapter(this, categorizedItems);
        binding.tabViewPager.setAdapter(menuPagerAdapter);
        binding.tabViewPager.setOffscreenPageLimit(3);



        // Connect TabLayout and ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.tabViewPager, (tab, position) -> {
            tab.setText(CATEGORIES[position]);
        }).attach();

        // Action for Search
        binding.etSearch.setOnClickListener(v ->
                Toast.makeText(getContext(), "Search feature coming soon", Toast.LENGTH_SHORT).show()
        );

        // Button click to add new item
        binding.btnAddMenuItem.setOnClickListener(v -> {
            MenuItem newItem = new MenuItem(); // Empty object
            int currentTab = binding.tabLayout.getSelectedTabPosition();
            String category = CATEGORIES[currentTab];

            Log.d("MenuFlow", "Add button clicked. Preparing new MenuItem for category: " + category);

            newItem.setCategory(category);
            if (getActivity() != null && getActivity().findViewById(R.id.fragment_container) != null) {
                MenuDetailsFragment menuDetailsFragment = MenuDetailsFragment.newInstance(newItem, category);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, menuDetailsFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Log.e("MenuFlow", "Fragment container not found for MenuDetailsFragment.");
                Toast.makeText(getContext(), "Cannot navigate to details. Container not found.", Toast.LENGTH_SHORT).show();
            }
        });

        // Get menu items from the backend
        fetchMenuItems();

        // FragmentResultListener for menu item updates
        getParentFragmentManager().setFragmentResultListener("menu_item_request", this, (key, bundle) -> {
            fetchMenuItems(); // Re-fetch the menu items after add/edit
        });

        return view;
    }

    private void fetchMenuItems() {
        ApiService.getMenuItems(new ApiCallback<List<MenuItem>>() {
            @Override
            public void onSuccess(List<MenuItem> menuItems) {
                distributeMenuItemsToCategories(menuItems); // Distribute items to categories
                updateFragments(); // Update all fragments with the latest data
                Log.d("MenuFlow", "Menu items fetched: " + menuItems.size());
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), "Failed to fetch menu items: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void distributeMenuItemsToCategories(List<MenuItem> menuItems) {
        // Clear existing data
        for (String category : CATEGORIES) {
            categorizedItems.get(category).clear();
        }

        // Group menu items by category
        for (MenuItem item : menuItems) {
            List<MenuItem> categoryList = categorizedItems.get(item.getCategory());
            if (categoryList != null) {
                categoryList.add(item);
                Log.d("MenuFlow", "Item \"" + item.getName() + "\" added to category: " + item.getCategory());
            } else {
                Log.w("MenuFlow", "Item \"" + item.getName() + "\" has unrecognized category: " + item.getCategory());
            }
        }

        // Log category counts
        for (String category : CATEGORIES) {
            Log.d("MenuFlow", category + " category item count: " + categorizedItems.get(category).size());
        }
    }

    private void updateFragments() {
        for (int i = 0; i < CATEGORIES.length; i++) {
            String category = CATEGORIES[i];
            Fragment fragment = getChildFragmentManager().findFragmentByTag("f" + i); // Ensure correct tag

            if (fragment instanceof MenuCategoryInterface) {
                ((MenuCategoryInterface) fragment).setMenuItems(categorizedItems.get(category));
                Log.d("MenuFlow", "Items passed to " + category + " fragment.");
            } else if (fragment == null) {
                Log.w("MenuFlow", "No fragment found for tag: f" + i + " (category: " + category + ")");
            } else {
                Log.e("MenuFlow", "Fragment for " + category + " does not implement MenuCategoryInterface");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        getParentFragmentManager().clearFragmentResultListener("menu_item_request");
    }
}