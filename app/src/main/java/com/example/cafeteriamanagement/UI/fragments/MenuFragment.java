package com.example.cafeteriamanagement.UI.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.example.cafeteriamanagement.databinding.FragmentMenuBinding;
import com.example.cafeteriamanagement.model.MenuItem;
import com.google.android.material.tabs.TabLayoutMediator;

public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;
    private MenuPagerAdapter menuPageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        // Set up ViewPager2 with Adapter
        menuPageAdapter = new MenuPagerAdapter(this);
        binding.tabViewPager.setAdapter(menuPageAdapter);

        // Connect TabLayout and ViewPager2
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

        //  action for Search
        binding.etSearch.setOnClickListener(v ->
                Toast.makeText(getContext(), "Search feature coming soon", Toast.LENGTH_SHORT).show()
        );

        //  btn click to add new item
        binding.btnAddMenuItem.setOnClickListener(v -> {

            MenuItem newItem = new MenuItem(); // Empty object

            //  Get selected tab index and determine category
            int currentTab = binding.tabLayout.getSelectedTabPosition();
            String category = "";
            switch (currentTab) {
                case 0:
                    category = "Beverages";
                    break;
                case 1:
                    category = "Bakery";
                    break;
                case 2:
                    category = "Special";
                    break;
                default:
                    Toast.makeText(getContext(), "Unknown tab selected", Toast.LENGTH_SHORT).show();
                    return;
            }
            Log.d("MenuFlow", "Add button clicked. Preparing new MenuItem for category: " + category);

            //  Assign category to item and pass to fragment
            newItem.setCategorie(category);
            MenuDetailsFragment menuDetailsFragment = MenuDetailsFragment.newInstance(newItem, category);
            Log.d("MenuFlow", "Navigating to MenuDetailsFragment with item: " + newItem.toString());

            requireActivity()
            .getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, menuDetailsFragment)
            .addToBackStack(null)
            .commit();

        });

        // centralize setFragment result listener
        getParentFragmentManager().setFragmentResultListener("menu_item_request", this, (key, bundle) -> {
            MenuItem updatedItem = (MenuItem) bundle.getSerializable("menu_item");

            if (updatedItem != null) {
                String category = updatedItem.getCategorie();
                Log.d("MenuFlow", "MenuFragment received result for category: " + category);

                int position = -1;
                switch (category) {
                    case "Beverages":
                        position = 0;
                        break;
                    case "Bakery":
                        position = 1;
                        break;
                    case "Special":
                        position = 2;
                        break;
                }

                if (position != -1) {
                    String tag = "f" + position;
                    Fragment targetFragment = getChildFragmentManager().findFragmentByTag("f" + position);

                    if (targetFragment instanceof MenuCategoryInterface) {
                        ((MenuCategoryInterface) targetFragment).updateCategoryMenuItem(updatedItem);
                    } else {
                        Log.d("MenuFlow", "Target fragment not found or doesn't implement handler. Tag: " + tag);
                    }
                }

            }
        });







        //  Receive result from MenuDetailsFragment and update tab
       // getParentFragmentManager().setFragmentResultListener("menu_item_request", this,
                //new FragmentResultListener() {
                   // @Override
                    //public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        //MenuItem updatedItem = (MenuItem) result.getSerializable("menu_item");
                        //if (updatedItem != null) {
                          //  updateMenuItemList(updatedItem);
                           // Log.d("MenuFlow", "MenuFragment received result") ;

                       // }
                    //}

                //});

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //  Handle updating correct category tab after saving or editing
    /*public void updateMenuItemList(MenuItem updatedItem) {
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
        Log.d("MenuFlow", "Calling updateCategoryMenuItem() for tab: " + tabIndex + ", category: " + category);

        Fragment categoryFragment = menuPageAdapter.getFragment(tabIndex);
        if (categoryFragment instanceof MenuCategoryInterface) {
            ((MenuCategoryInterface) categoryFragment).updateCategoryMenuItem(updatedItem);
        } else {
            Toast.makeText(getContext(), "Could not find category fragment", Toast.LENGTH_SHORT).show();
        }*/

}
