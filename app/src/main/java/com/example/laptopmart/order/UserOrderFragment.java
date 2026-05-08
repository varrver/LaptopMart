package com.example.laptopmart.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.R;
import com.example.laptopmart.databinding.FragmentOrderBinding;

public class UserOrderFragment extends Fragment {

    private FragmentOrderBinding binding;
    private UserOrderViewModel viewModel;
    private AdminOrderAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(UserOrderViewModel.class);

        binding.tvHeader.setText(R.string.order_history);

        setupRecyclerView();
        observeViewModel();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new AdminOrderAdapter(false, null);
        binding.rvAdminOrders.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getOrdersLiveData().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) {
                adapter.submitList(orders);
            }
        });

        viewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}