package com.example.dmr.medicalrep.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.adapters.MedicineAdapter;
import com.example.dmr.medicalrep.model.Medicine;
import com.example.dmr.medicalrep.utils.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendRequestActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    TextInputEditText name, potency, description;
    Button sendRequest,addMed;
    List<Medicine> medicines;
    RecyclerView meds;
    String docName,docCNIC,docToken;
    Map<String, Object> data;
    MedicineAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);
        init();
        addMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    Medicine medicine = new Medicine();
                    medicine.setDescription(description.getText().toString());
                    medicine.setName(name.getText().toString());
                    medicine.setPotency(potency.getText().toString());
                    medicines.add(medicine);
                    adapter.notifyItemInserted(medicines.size()-1);
                }
            }
        });
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                data.put("to",docCNIC);
                data.put("from", SessionManager.getUser(getApplicationContext()).get(SessionManager.CNIC).toString());
                data.put("timestamp", Timestamp.now());
                data.put("status","Sent");
                data.put("docName",docName);
                data.put("repName",SessionManager.getUser(getApplicationContext()).get(SessionManager.FULL_NAME).toString());
                data.put("medicines",medicines);
                firestore.collection("Requests").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        Map<String,String> map=new HashMap<>();
                        map.put("title","Request from " + SessionManager.getUser(getApplicationContext()).get(SessionManager.FULL_NAME).toString());
                        map.put("body","You have a new Request");
                        RemoteMessage remoteMessage=new RemoteMessage.Builder(docToken).setData(map).build();
                        FirebaseMessaging.getInstance().send(remoteMessage);
                        startActivity(new Intent(SendRequestActivity.this,RequestsRepActivity.class));
                        finish();
                    }
                });
            }
        });
    }

    boolean validate(){
        if (name.getText().toString().isEmpty()){
            name.requestFocus();
            name.setError("Please enter medicine name");
            return false;
        } else if (potency.getText().toString().isEmpty()){
            potency.setError("Please enter medicine potency");
            potency.requestFocus();
            return false;
        } else if (description.getText().toString().isEmpty()){
            description.setError("Please enter medicine description");
            description.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    void init(){
        firestore=FirebaseFirestore.getInstance();
        name=findViewById(R.id.medName);
        potency=findViewById(R.id.medPotency);
        description=findViewById(R.id.medDesc);
        docName=getIntent().getStringExtra("docName");
        docCNIC=getIntent().getStringExtra("docCNIC");
        docToken=getIntent().getStringExtra("docToken");
        data=new HashMap<>();
        meds=findViewById(R.id.meds);
        sendRequest=findViewById(R.id.sendRequest);
        addMed=findViewById(R.id.addMed);
        medicines =new ArrayList<>();
        adapter=new MedicineAdapter(medicines);
        meds.setLayoutManager(new LinearLayoutManager(this));
        meds.setAdapter(adapter);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Sending Request");
    }
}