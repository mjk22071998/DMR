package com.example.dmr.medicalrep.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.model.Request;
import com.example.dmr.medicalrep.utils.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class SentFragment extends Fragment {

    FirebaseFirestore firestore;
    RecyclerView sent;
    SharedPreferences sharedPreferences;
    List<Request> requests;

    public SentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sent, container, false);
        sent=view.findViewById(R.id.sent);
        firestore.collection("Request").whereEqualTo("from", SessionManager.getUser(getContext()).get(SessionManager.CNIC).toString()).whereEqualTo("status","Sent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                requests=queryDocumentSnapshots.toObjects(Request.class);
                updateUI();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        return view;
    }

    void updateUI(){

    }
}