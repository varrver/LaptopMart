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

    public interface AddressCallback {
        void onSuccess(String message);

        void onError(String errorMessage);
    }

    public interface UpdateProfileCallback {
        void onSuccess(String message);

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

    public void updateAddress(String newAddress, AddressCallback callback) {
        if (auth.getCurrentUser() == null) return;
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users").document(userId)
                .update("address", newAddress)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Alamat berhasil diperbarui!"))
                .addOnFailureListener(e -> callback.onError("Gagal memperbarui alamat!"));
    }

    public void updateFullProfile(String newName, String newPhone, String newAddress, UpdateProfileCallback callback) {
        if (auth.getCurrentUser() == null) return;
        String userId = auth.getCurrentUser().getUid();

        // Firestore lets us update multiple fields at the same time!
        firestore.collection("users").document(userId)
                .update(
                        "name", newName,
                        "phone", newPhone,
                        "address", newAddress
                )
                .addOnSuccessListener(aVoid -> callback.onSuccess("Profil berhasil diperbarui!"))
                .addOnFailureListener(e -> callback.onError("Gagal memperbarui profil!"));
    }
}