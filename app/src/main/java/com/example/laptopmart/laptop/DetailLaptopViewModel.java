package com.example.laptopmart.laptop;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.laptopmart.cart.CartRepository;
import com.example.laptopmart.model.CartItem;

public class DetailLaptopViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final CartRepository repository;

    public DetailLaptopViewModel() {
        this.repository = new CartRepository();
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void saveCart(String id, String laptopId, String laptopName, String imageUrl, double price, int quantity) {
        isLoading.setValue(true);
        CartItem newItem = new CartItem(id, laptopId, laptopName, imageUrl, price, quantity);

        CartRepository.CartCallback callback = new CartRepository.CartCallback() {
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
        };

        repository.addToCart(newItem, callback);
    }
}
