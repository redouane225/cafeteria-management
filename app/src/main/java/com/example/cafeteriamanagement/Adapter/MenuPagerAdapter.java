package com.example.cafeteriamanagement.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cafeteriamanagement.UI.fragments.BakeryFragment;
import com.example.cafeteriamanagement.UI.fragments.BeveragesFragment;
import com.example.cafeteriamanagement.UI.fragments.SpecialFragment;
import com.example.cafeteriamanagement.model.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MenuPagerAdapter extends FragmentStateAdapter {

    // Store all fragments in a list
    private final List<Fragment> fragmentList = new ArrayList<>();

    public MenuPagerAdapter(@NonNull Fragment fragment, Map<String, List<MenuItem>> categorizedItems) {
        super(fragment);

        // Initialize all fragments with their respective data
        fragmentList.add(BeveragesFragment.newInstance(categorizedItems.get("Beverages"))); // Beverages Fragment
        fragmentList.add(BakeryFragment.newInstance(categorizedItems.get("Bakery")));       // Bakery Fragment
        fragmentList.add(SpecialFragment.newInstance(categorizedItems.get("Special")));     // Special Fragment
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the fragment from the pre-created list
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        // The total number of fragments
        return fragmentList.size();
    }

    @Override
    public long getItemId(int position) {
        // Use position as the unique ID for each fragment
        return position;
    }

    @Override
    public boolean containsItem(long itemId) {
        // Check if the fragment exists in the list
        return itemId >= 0 && itemId < fragmentList.size();
    }
}