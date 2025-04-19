package com.example.cafeteriamanagement.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.Adapter.MenuPagerAdapter;
import com.example.cafeteriamanagement.databinding.FragmentMenuBinding; //  Import View Binding
import com.example.cafeteriamanagement.model.MenuItem;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.viewpager2.widget.ViewPager2;

public class MenuFragment extends Fragment {

    // Use binding instead of individual view variables
    private FragmentMenuBinding binding; //  View Binding variable
    private MenuPagerAdapter menuPageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //  Inflate layout using View Binding
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //  Set up ViewPager and Adapter
        menuPageAdapter = new MenuPagerAdapter(requireActivity());
        binding.tabViewPager.setAdapter(menuPageAdapter);

        //  Attach ViewPager to TabLayout
        new TabLayoutMediator(binding.tabLayout, binding.tabViewPager, (tab, position) -> {
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

        //  Show message when search bar is clicked
        binding.etSearch.setOnClickListener(v ->
                Toast.makeText(getContext(), "Search feature coming soon", Toast.LENGTH_SHORT).show()
        );
        binding.btnAddMenuItem.setOnClickListener(v -> {
            MenuItem newItem = new MenuItem(); // empty object for add mode
            MenuDetailsFragment menuDetailsFragment = MenuDetailsFragment.newInstance(newItem);
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, menuDetailsFragment)
                    .addToBackStack(null)
                    .commit();
        });


        //  Handle result from MenuDetailsFragment
        getParentFragmentManager().setFragmentResultListener("menu_item_request", this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        MenuItem updatedItem = (MenuItem) result.getSerializable("menu_item");
                        if (updatedItem != null) {
                            updateMenuItemList(updatedItem);
                        }
                    }
                });

        return view;
    }

    //  Best practice: clear binding reference
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; //  Prevent memory leaks
    }

    // Add or update MenuItem in the appropriate category tab
    public void updateMenuItemList(MenuItem updatedItem) {
        String category = updatedItem.getCategorie();
        int tabIndex;

        switch (category) {
            case "Beverages":
                tabIndex = 0;
                break;
            case "Bakery":
                tabIndex = 1;
                break;
            case "Special":
                tabIndex = 2;
                break;
            default:
                Toast.makeText(getContext(), "Unknown category", Toast.LENGTH_SHORT).show();
                return;
        }

        Fragment categoryFragment = getChildFragmentManager().findFragmentByTag("f" + tabIndex);
        if (categoryFragment instanceof MenuCategoryInterface) {
            ((MenuCategoryInterface) categoryFragment).updateCategoryMenuItem(updatedItem);
        } else {
            Toast.makeText(getContext(), "Could not find category fragment", Toast.LENGTH_SHORT).show();
        }
    }
}
