package com.example.laptopmart.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.laptopmart.R;
import com.example.laptopmart.databinding.ActivityDashboardBinding;
import com.example.laptopmart.laptop.ListLaptopFragment;
import com.example.laptopmart.order.AdminOrderFragment;
import com.example.laptopmart.profile.ProfileFragment;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBottomNavigationBar();

        if (savedInstanceState == null) {
            loadFragment(new ListLaptopFragment());
            binding.bottomNavigationAdmin.setSelectedItemId(R.id.item_list);
        }
    }

    private void initBottomNavigationBar() {
        binding.bottomNavigationAdmin.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.item_list) {
                loadFragment(new ListLaptopFragment());
                return true;
            } else if (itemId == R.id.item_orders) {
                loadFragment(new AdminOrderFragment());
                return true;
            } else if (itemId == R.id.item_profile) {
                loadFragment(new ProfileFragment());
                return true;
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_admin, fragment)
                    .commit();
    }
}