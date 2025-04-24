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

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.Adapter.MenuPagerAdapter;
import com.example.cafeteriamanagement.databinding.FragmentMenuBinding;
import com.example.cafeteriamanagement.model.MenuItem;
import com.google.android.material.tabs.TabLayoutMediator;

public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;
    private MenuPagerAdapter menuPageAdapter;
    private static final String[] CATEGORIES = {"Beverages", "Bakery", "Special"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Set up ViewPager2 with Adapter
        menuPageAdapter = new MenuPagerAdapter(this);
        binding.tabViewPager.setAdapter(menuPageAdapter);

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

            newItem.setCategorie(category);
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

        // FragmentResultListener for menu item updates
        getParentFragmentManager().setFragmentResultListener("menu_item_request", this, (key, bundle) -> {
            MenuItem updatedItem = (MenuItem) bundle.getSerializable("menu_item");

            if (updatedItem != null) {
                String category = updatedItem.getCategorie();
                Log.d("MenuFlow", "MenuFragment received result for category: " + category);

                int position = -1;
                for (int i = 0; i < CATEGORIES.length; i++) {
                    if (CATEGORIES[i].equals(category)) {
                        position = i;
                        break;
                    }
                }

                if (position != -1) {
                    String tag = "f" + position;
                    Fragment targetFragment = getChildFragmentManager().findFragmentByTag(tag);

                    if (targetFragment instanceof MenuCategoryInterface) {
                        ((MenuCategoryInterface) targetFragment).updateCategoryMenuItem(updatedItem);
                    } else {
                        Log.e("MenuFlow", "Target fragment not found or doesn't implement handler. Tag: " + tag);
                        Toast.makeText(getContext(), "Failed to update category. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        getParentFragmentManager().clearFragmentResultListener("menu_item_request");
    }
}