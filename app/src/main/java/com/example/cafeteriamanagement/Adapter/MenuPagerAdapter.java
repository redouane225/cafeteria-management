package com.example.cafeteriamanagement.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cafeteriamanagement.UI.fragments.BakeryFragment;
import com.example.cafeteriamanagement.UI.fragments.BeveragesFragment;
import com.example.cafeteriamanagement.UI.fragments.SpecialFragment;

import java.util.HashMap;
import java.util.Map;

public class MenuPagerAdapter extends FragmentStateAdapter {

    private final Map<String, Fragment> fragmentMap = new HashMap<>();

    public MenuPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new BeveragesFragment();
                fragmentMap.put("Beverages", fragment);
                return fragment;
            case 1:
                fragment = new BakeryFragment();
                fragmentMap.put("Bakery", fragment);
                return fragment;
            case 2:
                fragment = new SpecialFragment();
                fragmentMap.put("Special", fragment);
                return fragment;
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Beverages, Bakery, Special
    }

    public Fragment getFragmentByCategory(String category) {
        return fragmentMap.get(category);
    }
}
