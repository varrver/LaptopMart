package com.example.laptopmart.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.laptopmart.model.CartItem;

import java.util.List;

public class CheckoutViewModel extends ViewModel {
    private final OrderRepository repository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public CheckoutViewModel() {
        repository = new OrderRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public void createOrder(String address, String shippingMethod, String paymentMethod, String bankAccount, String notes, double totalPrice, List<CartItem> cartItems) {
        isLoading.setValue(true);
        repository.placeOrder(address, shippingMethod, paymentMethod, bankAccount, notes, totalPrice, cartItems, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(String message) {
                isLoading.setValue(false);
                toastMessage.setValue(message);
            }

            @Override
            public void onError(String errorMessage) {
                isLoading.setValue(false);
                toastMessage.setValue(errorMessage);
            }
        });
    }
}
