package com.example.laptopmart.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> navigateTo = new MutableLiveData<>();

    private final AuthRepository repository;

    public RegisterViewModel() {
        repository = new AuthRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getNavigateTo() {
        return navigateTo;
    }

    public void register(String name, String phone, String email, String password) {
        isLoading.setValue(true);
        AuthRepository.AuthCallBack callBack = new AuthRepository.AuthCallBack() {
            @Override
            public void onSuccess(String role) {
                isLoading.setValue(false);
                if ("admin".equals(role)) {
                    navigateTo.setValue("ADMIN");
                } else {
                    navigateTo.setValue("MAIN");
                }
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                errorMessage.setValue(error);
            }
        };
        repository.checkIdentifier(name, phone, email, password, callBack);
    }
}
