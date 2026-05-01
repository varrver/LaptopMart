package com.example.laptopmart.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthRepository {

    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public AuthRepository() {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public interface AuthCallBack {
        void onSuccess(String role);

        void onError(String errorMessage);
    }

    public void loginPhone(String phone, String password, AuthCallBack callBack) {
        firestore.collection("users")
                .whereEqualTo("phone", phone)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String registeredEmail = document.getString("email");
                        if (registeredEmail != null) {
                            loginEmail(registeredEmail, password, callBack);
                        } else {
                            callBack.onError("Terdapat kesalahan data pengguna!");
                        }
                    } else {
                        callBack.onError("Nomor telepon tidak ditemukan!");
                    }
                });
    }

    public void loginEmail(String email, String password, AuthCallBack callBack) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = auth.getCurrentUser();
                        if (currentUser != null) {
                            checkAdmin(currentUser.getUid(), callBack);
                        }
                    } else {
                        String realError = task.getException() != null ?
                                task.getException().getMessage() : "Login Gagal!";
                        callBack.onError(realError);
                    }
                });
    }

    public void checkAdmin(String userId, AuthCallBack callBack) {
        firestore.collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                        String role = task.getResult().getString("role");
                        callBack.onSuccess(role);
                    } else {
                        auth.signOut();
                        callBack.onError("Gagal mengambil data profil");
                    }
                });
    }

    public void checkSession(AuthCallBack callBack) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            checkAdmin(currentUser.getUid(), callBack);
        } else {
            callBack.onError("Tidak ada sesi yang ditemukan");
        }
    }

    public void checkIdentifier(String name, String phone, String email, String password, AuthCallBack callBack) {
        firestore.collection("users")
                .whereEqualTo("phone", phone)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (!task.getResult().isEmpty()) {
                            callBack.onError("Nomor telepon sudah terdaftar");
                        } else {
                            createAccount(name, phone, email, password, callBack);
                        }
                    } else {
                        callBack.onError("Gagal memeriksa database. Periksa koneksi anda.");
                    }
                });
    }

    public void createAccount(String name, String phone, String email, String password, AuthCallBack callBack) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && auth.getCurrentUser() != null) {
                        String userId = auth.getCurrentUser().getUid();
                        saveUserDataToFirestore(userId, name, phone, email, password, callBack);
                    } else {
                        String realError = task.getException() != null ?
                                task.getException().getMessage() : "Register gagal!";
                        callBack.onError(realError);
                    }
                });
    }

    public void saveUserDataToFirestore(String userId, String name, String phone, String email, String password, AuthCallBack callBack) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", userId);
        userMap.put("name", name);
        userMap.put("phone", phone);
        userMap.put("email", email);
        userMap.put("role", "user");

        firestore.collection("users")
                .document(userId)
                .set(userMap)
                .addOnSuccessListener(documentReference -> loginEmail(email, password, callBack))
                .addOnFailureListener(e -> callBack.onError("Gagal menympan ke database!"));
    }

}
