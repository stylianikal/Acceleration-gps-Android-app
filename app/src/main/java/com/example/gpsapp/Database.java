package com.example.gpsapp;


import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.gpsapp.User.Authentication;
import com.example.gpsapp.User.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Database {
    private final CollectionReference user_cre = FirebaseFirestore.getInstance().collection("user_creds"),
            user_data = FirebaseFirestore.getInstance().collection("user_data");

    public Database() {

    }

    public void saveData(User us) {
        user_data.document(String.valueOf(us.getUsername())).set(us)
                .addOnSuccessListener(unused -> Log.d(TAG, "Users information saved"))
                .addOnFailureListener(e -> Log.d(TAG, e.getMessage()));

    }

    public void user_login(String username, LoginActivity loginActivity) {
        user_cre.document(String.valueOf(username))
                .get().addOnSuccessListener(documentSnapshot-> {
            if (documentSnapshot.exists()) {
                Authentication authentication = documentSnapshot.toObject(Authentication.class);
                loginActivity.user_authentication(username, authentication);
            }
        })
                .addOnFailureListener(e -> Log.d(TAG, e.getMessage()));
    }

    public void load_user_data(String username, LoginActivity loginActivity) {
        user_data.document(String.valueOf(username))
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        assert user != null;
                        user.setUsername(username);
                        loginActivity.next(user);
                    }

                }).addOnFailureListener(e -> Log.d(TAG, e.getMessage()));
    }

    public CollectionReference getUser_data() {
        return user_data;
    }
}

