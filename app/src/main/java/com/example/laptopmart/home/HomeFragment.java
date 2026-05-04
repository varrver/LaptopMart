package com.example.laptopmart.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.databinding.FragmentHomeBinding;
import com.example.laptopmart.laptop.DetailLaptopActivity;
import com.example.laptopmart.laptop.LaptopAdapter;
import com.example.laptopmart.laptop.ListLaptopViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListLaptopViewModel viewModel;
    private LaptopAdapter adapter;

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
            intent.putExtra("LAPTOP_NAME", laptop.getName());
            intent.putExtra("LAPTOP_BRAND", laptop.getBrand());
            intent.putExtra("LAPTOP_DESCRIPTION", laptop.getDescription());
            intent.putExtra("LAPTOP_PRICE", laptop.getPrice());
            intent.putExtra("LAPTOP_STOCK", laptop.getStock());
            intent.putExtra("LAPTOP_IMAGE", laptop.getImageUrl());

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
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                showToast(errorMsg);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}