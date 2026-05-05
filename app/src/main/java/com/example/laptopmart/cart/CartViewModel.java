package com.example.laptopmart.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.laptopmart.model.CartItem;

import java.util.List;

public class CartViewModel extends ViewModel {
    private final CartRepository repository;
    private final MutableLiveData<List<CartItem>> cartItemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Double> totalPriceLiveData = new MutableLiveData<>(0.0);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public CartViewModel() {
        repository = new CartRepository();
        fetchCart();
    }

    public LiveData<List<CartItem>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }

    public LiveData<Double> getTotalPriceLiveData() {
        return totalPriceLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private void fetchCart() {
        repository.listenForCart(new CartRepository.CartListCallback() {
            @Override
            public void onDataChange(List<CartItem> cartItems) {
                cartItemsLiveData.setValue(cartItems);
                calculateTotal(cartItems);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    private void calculateTotal(List<CartItem> cartItems) {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        totalPriceLiveData.setValue(total);
    }

    public void increaseQuantity(CartItem item) {
        repository.updateQuantity(item.getId(), item.getQuantity() + 1);
    }

    public void decreaseQuantity(CartItem item) {
        repository.updateQuantity(item.getId(), item.getQuantity() - 1);
    }
}
