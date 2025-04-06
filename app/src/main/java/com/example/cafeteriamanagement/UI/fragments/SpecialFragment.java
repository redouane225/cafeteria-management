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

public class SpecialFragment extends Fragment {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special, container, false);

        recyclerView = view.findViewById(R.id.recycler_special);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        menuItemList = new ArrayList<>();
        menuItemList.add(new MenuItem(5, "Special Burger", 5.99, "Available","Special"));
        menuItemList.add(new MenuItem(6, "Special Pizza", 7.99, "Available","Special"));

        menuAdapter = new MenuAdapter(menuItemList, menuItem -> {
            // Handle item click
        });
        recyclerView.setAdapter(menuAdapter);

        return view;
    }
}