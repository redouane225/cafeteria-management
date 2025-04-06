package com.example.cafeteriamanagement.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cafeteriamanagement.UI.fragments.BakeryFragment;
import com.example.cafeteriamanagement.UI.fragments.BaveragesFragment;
import com.example.cafeteriamanagement.UI.fragments.SpecialFragment;

public class MenuPagerAdapter extends FragmentStateAdapter {

    public MenuPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BaveragesFragment();
            case 1:
                return new BakeryFragment();
            case 2:
                return new SpecialFragment();
            default:
                return new BaveragesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}