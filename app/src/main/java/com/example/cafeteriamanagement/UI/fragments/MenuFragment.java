package com.example.cafeteriamanagement.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.Adapter.MenuAdapter;
import com.example.cafeteriamanagement.model.Menu_item;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment implements MenuAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<Menu_item> menuItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize menu item list
        menuItemList = new ArrayList<>();
        // Add some sample data (in a real scenario, fetch from database)
        menuItemList.add(new Menu_item(1,"Pizza", 10.99, "Available"));
        menuItemList.add(new Menu_item(2,"Burger", 5.99, "Available"));

        // Initialize adapter
        menuAdapter = new MenuAdapter(menuItemList, this);
        recyclerView.setAdapter(menuAdapter);

        return view;
    }

    @Override
    public void onItemClick(Menu_item menuItem) {
        openMenuDetailsFragment(menuItem);
    }

    private void openMenuDetailsFragment(@Nullable Menu_item menuItem) {
        MenuDetailsFragment menuDetailsFragment = MenuDetailsFragment.newInstance(menuItem);
        menuDetailsFragment.setTargetFragment(this, 0);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, menuDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    public void updateMenuItemList(Menu_item menuItem) {
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