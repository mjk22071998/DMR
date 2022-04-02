package com.example.dmr.medicalrep.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.dmr.medicalrep.R;
import com.google.android.material.button.MaterialButton;

public class SelectUserActivity extends AppCompatActivity {

    MaterialButton rep,doc;
    private SharedPreferences.Editor editor;
    boolean reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        init();
        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("rep",true);
                editor.commit();
                if (reg){
                    startActivity(new Intent(SelectUserActivity.this, SignUpRepActivity.class));
                } else{
                    startActivity(new Intent(SelectUserActivity.this,LoginActivity.class));
                }
            }
        });
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("rep",false);
                editor.commit();
                if (reg){
                    startActivity(new Intent(SelectUserActivity.this, SignUpDocActivity.class));
                } else{
                    startActivity(new Intent(SelectUserActivity.this,LoginActivity.class));
                }

            }
        });
    }

    void init(){
        rep=findViewById(R.id.med_rep);
        doc=findViewById(R.id.doc);
        SharedPreferences sharedPreferences = getSharedPreferences("File", MODE_PRIVATE);
        editor= sharedPreferences.edit();
        reg= sharedPreferences.getBoolean("register",false);
    }
}