package com.example.cafeteriamanagement.UI.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.UI.fragments.InventoryDetails;
import com.example.cafeteriamanagement.UI.fragments.InventoryDetails;
import com.example.cafeteriamanagement.UI.fragments.InventoryFragment;
import com.example.cafeteriamanagement.UI.fragments.MenuDetailsFragment;
import com.example.cafeteriamanagement.UI.fragments.MenuFragment;
import com.example.cafeteriamanagement.UI.fragments.ProfileFragment;
import com.example.cafeteriamanagement.UI.fragments.StaffdetailFragment;
import com.example.cafeteriamanagement.UI.fragments.StaffdetailFragment;
import com.example.cafeteriamanagement.UI.fragments.StafflistFragment;
import com.example.cafeteriamanagement.databinding.ActivityDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private FragmentManager fragmentManager;
    private Fragment activeFragment;
    private final MenuFragment menuFragment = new MenuFragment();
    private final StafflistFragment staffFragment = new StafflistFragment();
    private final InventoryFragment inventoryFragment = new InventoryFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the binding
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        // Initialize Fragments (Only add once)
        if (savedInstanceState == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, profileFragment, "Profile").hide(profileFragment);
            transaction.add(R.id.fragment_container, inventoryFragment, "Inventory").hide(inventoryFragment);
            transaction.add(R.id.fragment_container, staffFragment, "Staff").hide(staffFragment);
            transaction.add(R.id.fragment_container, menuFragment, "Menu");
            transaction.commit();
            activeFragment = menuFragment;
        }

        binding.bottomnavigationview.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_dashboard) {
                switchFragment(menuFragment);
                return true;
            } else if ( itemId == R.id.menu_staff) {
                switchFragment(staffFragment);
                return true;
            } else if (itemId == R.id.menu_inventory) {
                switchFragment(inventoryFragment);
                return true;
            } else if (itemId == R.id.menu_profile) {
                switchFragment(profileFragment);
                return true;
            } else {
                return false;
            }
        });

        // Set up Floating Action Button (FAB) to add new items
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> showAddItemFragment());
    }

    private void switchFragment(Fragment targetFragment) {
        if (targetFragment != activeFragment) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(activeFragment).show(targetFragment).commit();
            activeFragment = targetFragment;
        }
    }

    private void showAddItemFragment() {
        // Navigate to the appropriate "Add New Item" fragment based on the current fragment
        Fragment addItemFragment = null;
        String tag = null;

        if (activeFragment instanceof MenuFragment) {
            addItemFragment = new MenuDetailsFragment();
            tag = "MenuDetails";
        } else if (activeFragment instanceof StafflistFragment) {
            addItemFragment = new StaffdetailFragment();
            tag = "StaffDetails";
        } else if (activeFragment instanceof InventoryFragment) {
            addItemFragment = new InventoryDetails();
            tag = "InventoryDetails";
        } else {
            Toast.makeText(this, "Invalid Action", Toast.LENGTH_SHORT).show();
            return;
        }

        if (addItemFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, addItemFragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}