package com.example.dmr.medicalrep.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.adapters.RequestAdapter;
import com.example.dmr.medicalrep.model.Request;
import com.example.dmr.medicalrep.utils.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AcceptedFragment extends Fragment {

    RecyclerView accepted;
    FirebaseFirestore firestore;
    SharedPreferences sharedPreferences;
    Task<QuerySnapshot> task;
    List<Request> requests;
    ProgressDialog progressDialog;

    public AcceptedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_accepted, container, false);
        accepted=view.findViewById(R.id.accept);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        sharedPreferences=getActivity().getSharedPreferences("File",MODE_PRIVATE);
        if (sharedPreferences.getBoolean("rep",false)) {
            task = firestore.collection("Request").whereEqualTo("from", SessionManager.getUser(getContext()).get(SessionManager.CNIC).toString()).whereEqualTo("status","Accepted").get();
        } else {
            task = firestore.collection("Request").whereEqualTo("to", SessionManager.getUser(getContext()).get(SessionManager.CNIC).toString()).whereEqualTo("status","Accepted").get();
        }
        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                requests=queryDocumentSnapshots.toObjects(Request.class);
                Collections.sort(requests, new Comparator<Request>() {
                    @Override
                    public int compare(Request request, Request t1) {
                        return request.getTimestamp().compareTo(t1.getTimestamp());
                    }
                });
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
        accepted.setAdapter(new RequestAdapter(requests));
        accepted.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog.dismiss();
    }
}