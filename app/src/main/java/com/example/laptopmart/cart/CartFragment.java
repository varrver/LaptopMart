package com.example.laptopmart.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.databinding.FragmentCartBinding;
import com.example.laptopmart.laptop.DetailLaptopActivity;
import com.example.laptopmart.model.CartItem;
import com.example.laptopmart.order.CheckoutActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private CartViewModel viewModel;
    private CartAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        setupRecyclerView();
        observeModel();
        initButton();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new CartAdapter(new CartAdapter.OnCartClickListener() {
            @Override
            public void onCartClick(CartItem cartItem) {
                Intent intent = new Intent(requireContext(), DetailLaptopActivity.class);
                // ALL WE NEED TO PASS IS THE ID! The Activity will do the rest!
                intent.putExtra("LAPTOP_ID", cartItem.getLaptopId());
                startActivity(intent);
            }

            @Override
            public void onPlusCLick(CartItem cartItem) {
                viewModel.increaseQuantity(cartItem);
            }

            @Override
            public void onMinusClick(CartItem cartItem) {
                viewModel.decreaseQuantity(cartItem);
            }
        });
        binding.rvCart.setAdapter(adapter);
    }

    private void observeModel() {
        viewModel.getCartItemsLiveData().observe(getViewLifecycleOwner(), cartItems -> {
            if (cartItems != null) {
                adapter.submitList(cartItems);
            }
        });
        viewModel.getTotalPriceLiveData().observe(getViewLifecycleOwner(), totalPrice -> {
            if (totalPrice != null) {
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                String formattedPrice = formatRupiah.format(totalPrice);
                binding.tvTotalPrice.setText(formattedPrice);
            }
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                showToast(error);
            }
        });
    }

    private void initButton() {
        binding.btnCheckout.setOnClickListener(v -> {
            List<CartItem> items = viewModel.getCartItemsLiveData().getValue();
            Double totalPrice = viewModel.getTotalPriceLiveData().getValue();
            if (items == null || items.isEmpty()) {
                showToast("Keranjang Anda masih kosong!");
                return;
            }
            Intent intent = new Intent(requireContext(), CheckoutActivity.class);
            intent.putExtra("CART_ITEMS", new java.util.ArrayList<>(items));
            intent.putExtra("TOTAL_PRICE", totalPrice != null ? totalPrice : 0.0);
            startActivity(intent);
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}