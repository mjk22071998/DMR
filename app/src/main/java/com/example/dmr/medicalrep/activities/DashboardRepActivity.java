package com.example.dmr.medicalrep.activities;

import static com.example.dmr.medicalrep.utils.SessionManager.CITY;
import static com.example.dmr.medicalrep.utils.SessionManager.FULL_NAME;
import static com.example.dmr.medicalrep.utils.SessionManager.PHONE_NUMBER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardRepActivity extends AppCompatActivity{

    CardView request,sendRequest,messages;
    TextView name,phoneNumber,city;
    Button logout;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_rep);
        init();
        name.setText(SessionManager.getUser(getApplicationContext()).get(FULL_NAME).toString());
        phoneNumber.setText(SessionManager.getUser(getApplicationContext()).get(PHONE_NUMBER).toString());
        city.setText(SessionManager.getUser(getApplicationContext()).get(CITY).toString());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(DashboardRepActivity.this,MainActivity.class));
                finishAffinity();
            }
        });
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardRepActivity.this, ChatActivity.class));
            }
        });
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardRepActivity.this, SearchDoctorsActivity.class));
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardRepActivity.this, RequestsRepActivity.class));
            }
        });
    }

    void init(){
        request=findViewById(R.id.request);
        sendRequest=findViewById(R.id.sendRequest);
        messages=findViewById(R.id.message);
        name=findViewById(R.id.name);
        phoneNumber=findViewById(R.id.phone_number);
        city=findViewById(R.id.city);
        logout=findViewById(R.id.logout);
        auth=FirebaseAuth.getInstance();
    }
}