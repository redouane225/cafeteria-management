package com.example.cafeteriamanagement.UI.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.UI.fragmentsStaff.newOrder;
import com.example.cafeteriamanagement.UI.fragmentsStaff.ordersList;
import com.example.cafeteriamanagement.UI.fragmentsStaff.staffMenu;
import com.example.cafeteriamanagement.UI.fragmentsStaff.staffProfile;
import com.example.cafeteriamanagement.databinding.ActivityStaffDashboardBinding;
import com.example.cafeteriamanagement.model.User;

public class StaffDashboardActivity extends AppCompatActivity {


    private ActivityStaffDashboardBinding binding;
    private FragmentManager fragmentManager;
    private Fragment activeFragment;
    private final staffMenu staffMenu = new staffMenu();
    private final ordersList ordersList = new ordersList();
    private final newOrder newOrder=new newOrder();
    private final staffProfile staffProfile = new staffProfile();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("user");

        if (user == null) {
            Toast.makeText(this, "User data not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding = ActivityStaffDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, staffProfile, "Profile").hide(staffProfile);
            transaction.add(R.id.fragment_container, newOrder, "Inventory").hide(newOrder);
            transaction.add(R.id.fragment_container, ordersList, "Staff").hide(ordersList);
            transaction.add(R.id.fragment_container, staffMenu, "Menu");
            transaction.commit();
            activeFragment = staffMenu;
        }

        binding.staffBtmnavbar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu) {
                switchFragment(staffMenu);
                return true;
            } else if (itemId == R.id.orders_list) {
                switchFragment(ordersList);
                return true;
            } else if (itemId == R.id.new_order) {
                switchFragment(newOrder);
                return true;
            } else if (itemId == R.id.staff_profile) {
                switchFragment(staffProfile);
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
