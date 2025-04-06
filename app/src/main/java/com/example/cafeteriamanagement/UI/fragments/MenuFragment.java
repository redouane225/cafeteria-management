package com.example.cafeteriamanagement.UI.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.Adapter.MenuAdapter;
import com.example.cafeteriamanagement.Adapter.MenuPagerAdapter;
import com.example.cafeteriamanagement.model.MenuItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment implements MenuAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItemList;
    private EditText searchBar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize search bar
        searchBar = view.findViewById(R.id.etSearch);

        // Initialize TabLayout and ViewPager2
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.tabViewPager);

        // Initialize menu item list
        menuItemList = new ArrayList<>();
        // Add some sample data (in a real scenario, fetch from database)
        menuItemList.add(new MenuItem(1, "Pizza", 10.99, "Available", "Bakery"));
        menuItemList.add(new MenuItem(2, "coke", 5.99, "Available", "baverages"));

        // Initialize adapter
        menuAdapter = new MenuAdapter(menuItemList, this);
        recyclerView.setAdapter(menuAdapter);

        // Add TextWatcher to search bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                menuAdapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed here
            }
        });

        // Set up TabLayout and ViewPager2 with MenuPageAdapter
        MenuPagerAdapter menuPageAdapter = new MenuPagerAdapter(requireActivity());
        viewPager.setAdapter(menuPageAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Beverages");
                            break;
                        case 1:
                            tab.setText("Bakery");
                            break;
                        case 2:
                            tab.setText("Special");
                            break;
                    }
                }).attach();

        return view;
    }

    @Override
    public void onItemClick(MenuItem menuItem) {
        openMenuDetailsFragment(menuItem);
    }

    private void openMenuDetailsFragment(@Nullable MenuItem menuItem) {
        MenuDetailsFragment menuDetailsFragment = MenuDetailsFragment.newInstance(menuItem);
        menuDetailsFragment.setTargetFragment(this, 0);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, menuDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    public void updateMenuItemList(MenuItem menuItem) {
        if (menuItem != null) {
            // Update existing item or add new item
            int index = menuItemList.indexOf(menuItem);
            if (index != -1) {
                menuItemList.set(index, menuItem);
            } else {
                menuItemList.add(menuItem);
            }
            menuAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "Error updating menu item", Toast.LENGTH_SHORT).show();
        }
    }
}