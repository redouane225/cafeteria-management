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
import com.example.cafeteriamanagement.databinding.FragmentSpecialBinding;
import com.example.cafeteriamanagement.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class SpecialFragment extends Fragment {

    private FragmentSpecialBinding binding;
    private MenuAdapter menuAdapter;
    private final List<MenuItem> menuItemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSpecialBinding.inflate(inflater, container, false);

        binding.recyclerSpecial.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        populateDummyData();

        menuAdapter = new MenuAdapter(menuItemList, this::openMenuDetailsFragment);
        binding.recyclerSpecial.setAdapter(menuAdapter);

        getParentFragmentManager().setFragmentResultListener("menu_item_updated", this, (key, bundle) -> {
            MenuItem updatedItem = (MenuItem) bundle.getSerializable("updated_menu_item");
            if (updatedItem != null && "Special".equals(updatedItem.getCategorie())) {
                updateItemInList(updatedItem);
            }
        });

        return binding.getRoot();
    }

    private void populateDummyData() {
        menuItemList.clear();
        menuItemList.add(new MenuItem(20, "Seasonal Cake", 5.49, "Available", "Special"));
        menuItemList.add(new MenuItem(21, "Holiday Pie", 4.99, "Available", "Special"));
        menuItemList.add(new MenuItem(22, "Chef’s Surprise", 6.99, "Unavailable", "Special"));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
