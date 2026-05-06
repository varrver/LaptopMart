package com.example.laptopmart.laptop;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.laptopmart.cart.CartRepository;
import com.example.laptopmart.model.CartItem;
import com.example.laptopmart.model.Laptop;

public class DetailLaptopViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final MutableLiveData<Laptop> laptopLiveData = new MutableLiveData<>();
    private final CartRepository cartRepository;
    private final LaptopRepository laptopRepository;

    public DetailLaptopViewModel() {
        this.cartRepository = new CartRepository();
        this.laptopRepository = new LaptopRepository();
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Laptop> getLaptopLiveData() {
        return laptopLiveData;
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

        cartRepository.addToCart(newItem, callback);
    }

    public void loadLaptopDetails(String laptopId) {
        isLoading.setValue(true);
        laptopRepository.getLaptopById(laptopId, new LaptopRepository.SingleLaptopCallback() {
            @Override
            public void onSuccess(Laptop laptop) {
                isLoading.setValue(false);
                laptopLiveData.setValue(laptop); // Send the laptop to the Activity!
            }

            @Override
            public void onError(String errorMessage) {
                isLoading.setValue(false);
                toastMessage.setValue(errorMessage);
            }
        });
    }
}
