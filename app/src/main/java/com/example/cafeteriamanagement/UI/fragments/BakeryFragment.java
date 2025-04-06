package com.example.cafeteriamanagement.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.Adapter.MenuAdapter;
import com.example.cafeteriamanagement.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class BakeryFragment extends Fragment {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bakery, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_bakery);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize menu item list
        menuItemList = new ArrayList<>();
        // Add some sample data (in a real scenario, fetch from database)
        menuItemList.add(new MenuItem(1, "Croissant", 2.99, "Available", "Bakery"));

        // Initialize adapter
        menuAdapter = new MenuAdapter(menuItemList, menuItem -> {
            // Handle item click
            openMenuDetailsFragment(menuItem);
        });
        recyclerView.setAdapter(menuAdapter);

        return view;
    }

    private void openMenuDetailsFragment(@Nullable MenuItem menuItem) {
        MenuDetailsFragment menuDetailsFragment = MenuDetailsFragment.newInstance(menuItem);
        menuDetailsFragment.setTargetFragment(this, 0);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, menuDetailsFragment)
                .addToBackStack(null)
                .commit();
    }
}