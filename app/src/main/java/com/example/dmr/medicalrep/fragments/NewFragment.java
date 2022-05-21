package com.example.dmr.medicalrep.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.activities.MessagesActivity;
import com.example.dmr.medicalrep.adapters.NewRequestAdapter;
import com.example.dmr.medicalrep.adapters.RequestAdapter;
import com.example.dmr.medicalrep.model.Request;
import com.example.dmr.medicalrep.utils.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NewFragment extends Fragment {

    FirebaseFirestore firestore;
    RecyclerView newRequests;
    List<Request> requests;
    ProgressDialog progressDialog;
    NewRequestAdapter adapter;

    public NewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_new, container, false);
        newRequests=view.findViewById(R.id.newRequests);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        requests=new ArrayList<>();
        firestore=FirebaseFirestore.getInstance();
        getData();
        return view;
    }

    void getData(){
        firestore.collection("Request").whereEqualTo("to", SessionManager.getUser(getContext()).get(SessionManager.CNIC).toString()).whereEqualTo("status","Sent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    Request request=queryDocumentSnapshot.toObject(Request.class);
                    request.setId(queryDocumentSnapshot.getId());
                    requests.add(request);
                }
                Collections.sort(requests, new Comparator<Request>() {
                    @Override
                    public int compare(Request request1, Request request2) {
                        return request1.getTimestamp().compareTo(request2.getTimestamp());
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
    }

    private void updateUI() {
        adapter=new NewRequestAdapter(requests);
        newRequests.setAdapter(adapter);
        adapter.setOnAcceptClickListener(new NewRequestAdapter.OnAcceptClickListener() {
            @Override
            public void onClick(final int position) {
                progressDialog.show();
                firestore.collection("Request").document(requests.get(position).getId()).update("status","Accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        requests.clear();
                        getData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        adapter.setOnMessageClickListener(new NewRequestAdapter.OnMessageClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(getContext(), MessagesActivity.class);
                intent.putExtra("CNIC",requests.get(position).getFrom());
                intent.putExtra("token",requests.get(position).getRepToken());
                intent.putExtra("name",requests.get(position).getRepName());
                startActivity(intent);
            }
        });
        adapter.setOnRejectClickListener(new NewRequestAdapter.OnRejectClickListener() {
            @Override
            public void onClick(int position) {
                progressDialog.show();
                firestore.collection("Request").document(requests.get(position).getId()).update("status","Rejected").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        requests.clear();
                        getData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        newRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog.dismiss();
    }


}