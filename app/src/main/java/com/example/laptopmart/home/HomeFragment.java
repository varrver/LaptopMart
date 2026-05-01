package com.example.laptopmart.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.laptopmart.adapter.BannerAdapter;
import com.example.laptopmart.databinding.FragmentHomeBinding;
import com.example.laptopmart.viewmodel.MainViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MainViewModel viewModel;

    // Timer variables for Auto-Slide
    private final Handler slideHandler = new Handler(Looper.getMainLooper());
    private Runnable slideRunnable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initBanner();
        // initCategory();  (keep your other methods)
        // initPopular();   (keep your other methods)
    }

    private void initBanner() {
        binding.pbBanner.setVisibility(View.VISIBLE);

        viewModel.loadBanner().observe(getViewLifecycleOwner(), banners -> {
            if (banners != null && !banners.isEmpty()) {

                // 1. Set up the Adapter
                BannerAdapter bannerAdapter = new BannerAdapter(banners);
                binding.vpBanner.setAdapter(bannerAdapter);

                // 2. Setup Auto-Slide Timer Logic
                setupAutoSlide(bannerAdapter.getItemCount());
            }
            binding.pbBanner.setVisibility(View.GONE);
        });
    }

    private void setupAutoSlide(int totalBanners) {
        // If there is only 1 banner, no need to slide
        if (totalBanners <= 1) return;

        slideRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = binding.vpBanner.getCurrentItem();

                // If it's at the last banner, go back to 0. Otherwise, go to the next one.
                if (currentItem < totalBanners - 1) {
                    binding.vpBanner.setCurrentItem(currentItem + 1, true); // true = smooth scroll
                } else {
                    binding.vpBanner.setCurrentItem(0, true);
                }

                // Repeat this every 3000 milliseconds (3 seconds)
                slideHandler.postDelayed(this, 3000);
            }
        };

        // Start the timer
        slideHandler.postDelayed(slideRunnable, 3000);

        // Optional: Pause auto-slide when the user is manually dragging the banners
        binding.vpBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    slideHandler.removeCallbacks(slideRunnable); // Pause
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    slideHandler.postDelayed(slideRunnable, 3000); // Resume
                }
            }
        });
    }

    // CRITICAL: Stop the timer when the user leaves the fragment to prevent memory leaks!
    @Override
    public void onPause() {
        super.onPause();
        if (slideRunnable != null) {
            slideHandler.removeCallbacks(slideRunnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (slideRunnable != null) {
            slideHandler.postDelayed(slideRunnable, 3000);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up the timer when the view is destroyed
        if (slideRunnable != null) {
            slideHandler.removeCallbacks(slideRunnable);
        }
        binding = null;
    }
}