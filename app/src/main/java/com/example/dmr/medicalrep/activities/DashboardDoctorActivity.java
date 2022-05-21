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

public class DashboardDoctorActivity extends AppCompatActivity {

    CardView request,messages;
    TextView name,phoneNumber,city;
    Button logout;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_doctor);
        init();
        name.setText(SessionManager.getUser(getApplicationContext()).get(FULL_NAME).toString());
        phoneNumber.setText(SessionManager.getUser(getApplicationContext()).get(PHONE_NUMBER).toString());
        city.setText(SessionManager.getUser(getApplicationContext()).get(CITY).toString());
        logout.setOnClickListener(view -> {
            auth.signOut();
            startActivity(new Intent(DashboardDoctorActivity.this,MainActivity.class));
            finishAffinity();
        });
        request.setOnClickListener(view -> startActivity(new Intent(DashboardDoctorActivity.this, RequestDocActivity.class)));
        messages.setOnClickListener(view -> startActivity(new Intent(DashboardDoctorActivity.this, ChatActivity.class)));
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    void init(){
        request=findViewById(R.id.request);
        messages=findViewById(R.id.message);
        name=findViewById(R.id.name);
        phoneNumber=findViewById(R.id.phone_number);
        city=findViewById(R.id.city);
        logout=findViewById(R.id.logout);
        auth=FirebaseAuth.getInstance();
    }
}