package com.example.laptopmart.order;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.databinding.FragmentAdminOrderBinding;
import com.example.laptopmart.model.Order;

public class AdminOrderFragment extends Fragment {

    private FragmentAdminOrderBinding binding;
    private AdminOrderViewModel viewModel;
    private AdminOrderAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminOrderBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AdminOrderViewModel.class);

        setupRecyclerView();
        observeViewModel();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        // Show the Pop-up Dialog!
        adapter = new AdminOrderAdapter(true, this::showStatusDialog);
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

    private void showStatusDialog(Order order) {
        // The list of statuses the Admin can choose from
        String[] statuses = {"Menunggu", "Dikemas", "Dikirim", "Selesai"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Update Status Pesanan")
                .setItems(statuses, (dialog, which) -> {
                    // 'which' is the index of the clicked item (0, 1, 2, or 3)
                    String selectedStatus = statuses[which];

                    // Tell the ViewModel to update it!
                    viewModel.changeStatus(order.getOrderId(), selectedStatus);
                })
                .show();
    }
}