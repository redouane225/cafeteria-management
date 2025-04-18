package com.example.cafeteriamanagement.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.cafeteriamanagement.Adapter.MenuAdapter;
import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.databinding.FragmentBakeryBinding;
import com.example.cafeteriamanagement.model.MenuItem;

import java.util.ArrayList;
import java.util.List;



public class BakeryFragment extends Fragment {

    private FragmentBakeryBinding binding; //  View binding
    private MenuAdapter menuAdapter;
    private final List<MenuItem> menuItemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentBakeryBinding.inflate(inflater, container, false);
        View view = binding.getRoot(); //  Get root view

        binding.recyclerBakery.setLayoutManager(new GridLayoutManager(getContext(), 2)); //  Using binding to access recyclerView

        populateDummyData();

        menuAdapter = new MenuAdapter(menuItemList, this::openMenuDetailsFragment);
        binding.recyclerBakery.setAdapter(menuAdapter); //  Set adapter

        //  Fragment ResultListener using Serializable
        getParentFragmentManager().setFragmentResultListener("menu_item_updated", this, (key, bundle) -> {
            MenuItem updatedItem = (MenuItem) bundle.getSerializable("updated_menu_item");
            if (updatedItem != null && "Bakery".equals(updatedItem.getCategorie())) {
                updateItemInList(updatedItem);
            }
        });

        return view;
    }

    private void populateDummyData() {
        menuItemList.clear();
        menuItemList.add(new MenuItem(1, "Croissant", 2.99, "Available", "Bakery"));
        menuItemList.add(new MenuItem(2, "Donut", 1.49, "Available", "Bakery"));
        menuItemList.add(new MenuItem(3, "Muffin", 2.79, "Unavailable", "Bakery"));
    }

    private void openMenuDetailsFragment(MenuItem menuItem) {
        MenuDetailsFragment menuDetailsFragment = MenuDetailsFragment.newInstance(menuItem);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, menuDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void updateItemInList(MenuItem updatedItem) {
        for (int i = 0; i < menuItemList.size(); i++) {
            if (menuItemList.get(i).getId() == updatedItem.getId()) {
                menuItemList.set(i, updatedItem);
                menuAdapter.notifyItemChanged(i);
                return;
            }
        }

        // If it's a new item, add it
        menuItemList.add(updatedItem);
        menuAdapter.notifyItemInserted(menuItemList.size() - 1);
    }

    public void refreshData() {
        menuAdapter.notifyDataSetChanged();
    }

    public void updateCategoryMenuItem(MenuItem updatedItem) {
        for (int i = 0; i < menuItemList.size(); i++) {
            if (menuItemList.get(i).getId() == updatedItem.getId()) {
                menuItemList.set(i, updatedItem);
                menuAdapter.notifyItemChanged(i);
                return;
            }
        }
    }
}
