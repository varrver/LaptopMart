package com.example.laptopmart.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.laptopmart.model.UserAddress;

import java.util.List;

public class AddressViewModel extends ViewModel {
    private final AddressRepository repository;
    private final MutableLiveData<List<UserAddress>> addresses = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public AddressViewModel() {
        repository = new AddressRepository();
        listenForAddresses();
    }

    public LiveData<List<UserAddress>> getAddresses() {
        return addresses;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    private void listenForAddresses() {
        isLoading.setValue(true);
        repository.listenForAddresses(new AddressRepository.AddressListCallback() {
            @Override
            public void onDataChange(List<UserAddress> addressList) {
                isLoading.setValue(false);
                addresses.setValue(addressList);
            }

            @Override
            public void onError(String errorMessage) {
                isLoading.setValue(false);
                toastMessage.setValue(errorMessage);
            }
        });
    }

    public void addAddress(UserAddress address) {
        isLoading.setValue(true);
        repository.addAddress(address, new AddressRepository.AddressCallback() {
            @Override
            public void onSuccess(String message) {
                isLoading.setValue(false);
                toastMessage.setValue(message);
            }

            @Override
            public void onError(String message) {
                isLoading.setValue(false);
                toastMessage.setValue(message);
            }
        });
    }

    public void updateAddress(UserAddress address) {
        isLoading.setValue(true);
        repository.updateAddress(address, new AddressRepository.AddressCallback() {
            @Override
            public void onSuccess(String message) {
                isLoading.setValue(false);
                toastMessage.setValue(message);
            }

            @Override
            public void onError(String message) {
                isLoading.setValue(false);
                toastMessage.setValue(message);
            }
        });
    }

    public void deleteAddress(String addressId) {
        isLoading.setValue(true);
        repository.deleteAddress(addressId, new AddressRepository.AddressCallback() {
            @Override
            public void onSuccess(String message) {
                isLoading.setValue(false);
                toastMessage.setValue(message);
            }

            @Override
            public void onError(String message) {
                isLoading.setValue(false);
                toastMessage.setValue(message);
            }
        });
    }
}
