package com.example.laptopmart.laptop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.databinding.FragmentListLaptopBinding;

public class ListLaptopFragment extends Fragment {

    private FragmentListLaptopBinding binding;
    private ListLaptopViewModel viewModel;
    private LaptopAdapter laptopAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListLaptopBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ListLaptopViewModel.class);

        setupRecyclerView();
        observeModel();
        initButton();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        laptopAdapter = new LaptopAdapter(laptop -> {
            Intent intent = new Intent(requireContext(), AddEditLaptopActivity.class);

            intent.putExtra("IS_EDIT_MODE", true);
            intent.putExtra("LAPTOP_ID", laptop.getId());
            intent.putExtra("LAPTOP_NAME", laptop.getName());
            intent.putExtra("LAPTOP_BRAND", laptop.getBrand());
            intent.putExtra("LAPTOP_DESCRIPTION", laptop.getDescription());
            intent.putExtra("LAPTOP_PRICE", String.valueOf((long) laptop.getPrice()));
            intent.putExtra("LAPTOP_STOCK", String.valueOf(laptop.getStock()));
            intent.putExtra("LAPTOP_IMAGE", laptop.getImageUrl());

            startActivity(intent);
        });
        binding.rvLaptops.setAdapter(laptopAdapter);
    }

    private void observeModel() {
        viewModel.getLaptopsLiveData().observe(getViewLifecycleOwner(), laptops -> {
            if (laptops != null) {
                laptopAdapter.submitList(laptops);
            }
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                showToast(errorMsg);
            }
        });
    }

    private void initButton() {
        binding.btnAdd.setOnClickListener(v -> navigateToAddEditLaptop());
    }

    private void navigateToAddEditLaptop() {
        Intent intent = new Intent(requireContext(), AddEditLaptopActivity.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}