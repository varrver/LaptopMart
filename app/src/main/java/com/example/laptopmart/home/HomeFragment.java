package com.example.laptopmart.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.laptopmart.databinding.FragmentHomeBinding;
import com.example.laptopmart.laptop.DetailLaptopActivity;
import com.example.laptopmart.laptop.LaptopAdapter;
import com.example.laptopmart.laptop.ListLaptopViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListLaptopViewModel viewModel;
    private LaptopAdapter adapter;
    private final Handler sliderHandler = new Handler(Looper.getMainLooper());
    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (binding != null && binding.vpBanner != null && binding.vpBanner.getAdapter() != null) {
                int currentItem = binding.vpBanner.getCurrentItem();
                int totalItems = binding.vpBanner.getAdapter().getItemCount();

                // If we reach the end, go back to 0. Otherwise, go to the next one!
                int nextItem = (currentItem + 1) % totalItems;
                binding.vpBanner.setCurrentItem(nextItem, true);

                // Tell the handler to run this exact code again in 3 seconds!
                sliderHandler.postDelayed(this, 3000);
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ListLaptopViewModel.class);

        setupRecyclerView();
        observeModel();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new LaptopAdapter(laptop -> {
            Intent intent = new Intent(requireContext(), DetailLaptopActivity.class);
            intent.putExtra("LAPTOP_ID", laptop.getId());
            startActivity(intent);
        });
        binding.rvUserLaptops.setAdapter(adapter);
        binding.rvUserLaptops.setNestedScrollingEnabled(false);
    }

    private void observeModel() {
        viewModel.getLaptopsLiveData().observe(getViewLifecycleOwner(), laptops -> {
            if (laptops != null) {
                adapter.submitList(laptops);
            }
        });
        viewModel.getBannersLiveData().observe(getViewLifecycleOwner(), bannerUrls -> {
            if (bannerUrls != null && !bannerUrls.isEmpty()) {
                setupBanner(bannerUrls); // Pass the Firebase URLs to the setup method!
            }
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                showToast(errorMsg);
            }
        });
    }

    private void setupBanner(List<String> bannerImages) {
        binding.pbBanner.setVisibility(View.GONE);

        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        binding.vpBanner.setAdapter(bannerAdapter);

        binding.vpBanner.setClipToPadding(false);
        binding.vpBanner.setClipChildren(false);
        binding.vpBanner.setOffscreenPageLimit(3);

        // CLEAR any existing timers before starting a new one (prevents crazy fast scrolling bug)
        sliderHandler.removeCallbacks(sliderRunnable);
        sliderHandler.postDelayed(sliderRunnable, 3000);

        binding.vpBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    // 3. Very Important: Stop the timer when the screen is closed to prevent Memory Leaks!
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacks(sliderRunnable);
        binding = null;
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}