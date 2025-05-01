package com.example.cafeteriamanagement.UI.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.UI.fragments.InventoryFragment;
import com.example.cafeteriamanagement.UI.fragments.MenuFragment;
import com.example.cafeteriamanagement.UI.fragments.ProfileFragment;
import com.example.cafeteriamanagement.UI.fragments.staffFragment;
import com.example.cafeteriamanagement.databinding.ActivityDashboardBinding;
import com.example.cafeteriamanagement.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private FragmentManager fragmentManager;
    private Fragment activeFragment;
    private final MenuFragment menuFragment = new MenuFragment();
    private final staffFragment staffFragment = new staffFragment();
    private final InventoryFragment inventoryFragment = new InventoryFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = (User) getIntent().getSerializableExtra("user");

        if (user == null) {
            Toast.makeText(this, "User data not found!", Toast.LENGTH_SHORT).show();
            finish(); // Prevent crash by closing the activity
            return;
        }

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, profileFragment, "Profile").hide(profileFragment);
            transaction.add(R.id.fragment_container, inventoryFragment, "Inventory").hide(inventoryFragment);
            transaction.add(R.id.fragment_container, staffFragment, "Staff").hide(staffFragment);
            transaction.add(R.id.fragment_container, menuFragment, "Menu");
            transaction.commit();
            activeFragment = menuFragment;
        }

        binding.adminBtmnavbar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.dashboard) {
                switchFragment(menuFragment);
                return true;
            } else if (itemId == R.id.staff) {
                switchFragment(staffFragment);
                return true;
            } else if (itemId == R.id.inventory) {
                switchFragment(inventoryFragment);
                return true;
            } else if (itemId == R.id.profile) {
                switchFragment(profileFragment);
                return true;
            } else {
                return false;
            }
        });

    }

    private void switchFragment(Fragment targetFragment) {
        if (targetFragment != activeFragment) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(activeFragment).show(targetFragment).commit();
            activeFragment = targetFragment;
        }
    }
}
