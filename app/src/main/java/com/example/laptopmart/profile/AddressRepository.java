package com.example.laptopmart.profile;

import com.example.laptopmart.model.UserAddress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class AddressRepository {
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public AddressRepository() {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public interface AddressCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    public interface AddressListCallback {
        void onDataChange(List<UserAddress> addresses);
        void onError(String errorMessage);
    }

    public void addAddress(UserAddress address, AddressCallback callback) {
        if (auth.getCurrentUser() == null) return;
        String userId = auth.getCurrentUser().getUid();

        DocumentReference addressRef = firestore.collection("users")
                .document(userId)
                .collection("addresses")
                .document();

        address.setId(addressRef.getId());

        if (address.isDefault()) {
            setAllOtherAddressesNotDefault(userId, addressRef.getId(), () -> {
                addressRef.set(address)
                        .addOnSuccessListener(aVoid -> callback.onSuccess("Alamat berhasil ditambahkan!"))
                        .addOnFailureListener(e -> callback.onError("Gagal menambahkan alamat!"));
            });
        } else {
            addressRef.set(address)
                    .addOnSuccessListener(aVoid -> callback.onSuccess("Alamat berhasil ditambahkan!"))
                    .addOnFailureListener(e -> callback.onError("Gagal menambahkan alamat!"));
        }
    }

    public void updateAddress(UserAddress address, AddressCallback callback) {
        if (auth.getCurrentUser() == null) return;
        String userId = auth.getCurrentUser().getUid();

        DocumentReference addressRef = firestore.collection("users")
                .document(userId)
                .collection("addresses")
                .document(address.getId());

        if (address.isDefault()) {
            setAllOtherAddressesNotDefault(userId, address.getId(), () -> {
                addressRef.set(address)
                        .addOnSuccessListener(aVoid -> callback.onSuccess("Alamat berhasil diperbarui!"))
                        .addOnFailureListener(e -> callback.onError("Gagal memperbarui alamat!"));
            });
        } else {
            addressRef.set(address)
                    .addOnSuccessListener(aVoid -> callback.onSuccess("Alamat berhasil diperbarui!"))
                    .addOnFailureListener(e -> callback.onError("Gagal memperbarui alamat!"));
        }
    }

    public void deleteAddress(String addressId, AddressCallback callback) {
        if (auth.getCurrentUser() == null) return;
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users")
                .document(userId)
                .collection("addresses")
                .document(addressId)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess("Alamat berhasil dihapus!"))
                .addOnFailureListener(e -> callback.onError("Gagal menghapus alamat!"));
    }

    public void listenForAddresses(AddressListCallback callback) {
        if (auth.getCurrentUser() == null) return;
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users")
                .document(userId)
                .collection("addresses")
                .orderBy("isDefault", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        callback.onError(error.getMessage());
                        return;
                    }
                    if (value != null) {
                        List<UserAddress> addresses = new ArrayList<>();
                        for (var doc : value) {
                            addresses.add(doc.toObject(UserAddress.class));
                        }
                        callback.onDataChange(addresses);
                    }
                });
    }

    private void setAllOtherAddressesNotDefault(String userId, String currentAddressId, Runnable onComplete) {
        firestore.collection("users")
                .document(userId)
                .collection("addresses")
                .whereEqualTo("isDefault", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    WriteBatch batch = firestore.batch();
                    for (var doc : queryDocumentSnapshots) {
                        if (!doc.getId().equals(currentAddressId)) {
                            batch.update(doc.getReference(), "isDefault", false);
                        }
                    }
                    batch.commit().addOnCompleteListener(task -> onComplete.run());
                })
                .addOnFailureListener(e -> onComplete.run());
    }
}
