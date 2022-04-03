package com.example.dmr.medicalrep.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.adapters.DoctorAdapter;
import com.example.dmr.medicalrep.model.Doctors;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchDoctorsActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    RecyclerView docsView;
    DoctorAdapter adapter;
    ArrayList<Doctors> doctors;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctors);
        init();
        progressDialog.show();
        firestore.collection("Users").whereEqualTo("Role","Doc").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    Doctors doc=queryDocumentSnapshot.toObject(Doctors.class);
                    doctors.add(doc);
                }
                adapter=new DoctorAdapter(doctors);
                docsView.setAdapter(adapter);
                progressDialog.dismiss();
                adapter.setOnDoctorClickListener(new DoctorAdapter.OnDoctorClickListener() {
                    @Override
                    public void onClick(int position) {
                        //TODO: SOON
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(SearchDoctorsActivity.this, "Failed to access doctors", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void init(){
        firestore=FirebaseFirestore.getInstance();
        docsView=findViewById(R.id.docsView);
        docsView.setLayoutManager(new LinearLayoutManager(this));
        doctors=new ArrayList<>();
        progressDialog=new ProgressDialog(this);
    }
}