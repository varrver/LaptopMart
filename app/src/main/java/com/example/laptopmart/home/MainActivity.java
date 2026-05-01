package com.example.laptopmart.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.laptopmart.R;
import com.example.laptopmart.databinding.ActivityMainBinding;
import com.example.laptopmart.profile.ProfileFragment;
import com.example.laptopmart.search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBottomNavigationBar();

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            binding.bottomNavigationBar.setSelectedItemId(R.id.itemHome);
        }
    }

    private void initBottomNavigationBar() {
        binding.bottomNavigationBar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.itemHome) {
                loadFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.itemSearch) {
                loadFragment(new SearchFragment());
                return true;
            } else if (itemId == R.id.itemProfile) {
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
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
    }
}