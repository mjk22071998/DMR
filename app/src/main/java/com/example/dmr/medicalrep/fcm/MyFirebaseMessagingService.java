package com.example.dmr.medicalrep.fcm;

import static com.example.dmr.medicalrep.utils.SessionManager.EMAIL;

import androidx.annotation.NonNull;

import com.example.dmr.medicalrep.utils.SessionManager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        SessionManager.saveToken(getApplicationContext(),s);
        CollectionReference reference;
        reference = FirebaseFirestore.getInstance().collection("Users");
        reference.document(SessionManager.getUser(getApplicationContext()).get(EMAIL).toString()).update("token",s);
    }
}
