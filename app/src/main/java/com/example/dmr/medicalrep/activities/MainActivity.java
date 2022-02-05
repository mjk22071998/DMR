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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SelectUserActivity.class));
                editor.putBoolean("register",false);
                editor.commit();
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SelectUserActivity.class));
                editor.putBoolean("register",true);
                editor.commit();
                finish();
            }
        });
    }

    void init(){
        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        sharedPreferences=getSharedPreferences("File",MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
}