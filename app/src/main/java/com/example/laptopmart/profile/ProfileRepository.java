package com.example.laptopmart.profile;

import com.example.laptopmart.model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileRepository {

    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public ProfileRepository() {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public interface ProfileCallback {
        void onSuccess(UserProfile userProfile);

        void onError(String errorMessage);
    }

    public void getUserProfile(ProfileCallback callback) {
        if (auth.getCurrentUser() == null) {
            callback.onError("User belum login");
            return;
        }

        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Magically convert the Firestore document into our UserProfile object!
                        UserProfile profile = documentSnapshot.toObject(UserProfile.class);
                        callback.onSuccess(profile);
                    } else {
                        callback.onError("Data profil tidak ditemukan!");
                    }
                })
                .addOnFailureListener(e -> callback.onError("Gagal mengambil profil: " + e.getMessage()));
    }

    public void logout() {
        auth.signOut(); // This tells Firebase to clear the current session!
    }
}