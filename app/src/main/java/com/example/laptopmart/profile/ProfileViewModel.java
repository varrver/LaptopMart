package com.example.laptopmart.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.laptopmart.model.UserProfile;

public class ProfileViewModel extends ViewModel {

    private final ProfileRepository repository;
    private final MutableLiveData<UserProfile> userProfileLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ProfileViewModel() {
        repository = new ProfileRepository();
        fetchProfile(); // Fetch the data as soon as the screen opens!
    }

    public LiveData<UserProfile> getUserProfileLiveData() {
        return userProfileLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private void fetchProfile() {
        repository.getUserProfile(new ProfileRepository.ProfileCallback() {
            @Override
            public void onSuccess(UserProfile userProfile) {
                userProfileLiveData.setValue(userProfile);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    public void logout() {
        repository.logout();
    }
}