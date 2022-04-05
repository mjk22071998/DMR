package com.example.dmr.medicalrep.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.model.Request;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class RejectedFragment extends Fragment {

    FirebaseFirestore firestore;
    RecyclerView rejected;
    SharedPreferences sharedPreferences;
    Task<QuerySnapshot> task;
    List<Request> requests;

    public RejectedFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_rejected, container, false);
        rejected=view.findViewById(R.id.reject);
        return view;
    }
}