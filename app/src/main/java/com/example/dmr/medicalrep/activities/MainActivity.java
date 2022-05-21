package com.example.dmr.medicalrep.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.dmr.medicalrep.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton login,signup;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    boolean rep;

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences=getSharedPreferences("File",MODE_PRIVATE);
        if (sharedPreferences.getBoolean("auth",false)){
            rep=sharedPreferences.getBoolean("rep",false);
            if (rep){
                startActivity(new Intent(MainActivity.this, DashboardRepActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, DashboardDoctorActivity.class));
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        login.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,SelectUserActivity.class));
            editor.putBoolean("register",false);
            editor.commit();
        });
        signup.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,SelectUserActivity.class));
            editor.putBoolean("register",true);
            editor.commit();
        });
    }

    void init(){
        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        sharedPreferences=getSharedPreferences("File",MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
}