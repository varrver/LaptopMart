package com.example.laptopmart.auth;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> navigateTo = new MutableLiveData<>();

    private final AuthRepository repository;

    public LoginViewModel() {
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

    public void login(String identifier, String password) {
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

        if (Patterns.PHONE.matcher(identifier).matches()) {
            repository.loginPhone(identifier, password, callBack);
        } else if (Patterns.EMAIL_ADDRESS.matcher(identifier).matches()) {
            repository.loginEmail(identifier, password, callBack);
        } else {
            errorMessage.setValue("Masukkan email atau nomor telepon!");
            isLoading.setValue(false);
        }
    }

    public void checkSession() {
        isLoading.setValue(true);
        repository.checkSession(new AuthRepository.AuthCallBack() {
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
            public void onError(String errorMessage) {
                isLoading.setValue(false);
            }
        });
    }
}
