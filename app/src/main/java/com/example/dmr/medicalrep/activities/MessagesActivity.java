package com.example.dmr.medicalrep.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.adapters.MessageAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MessagesActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    boolean rep;
    String recCNIC, recName,recToken;
    RecyclerView messageView;
    MessageAdapter adapter;
    TextInputEditText messageText;
    FloatingActionButton send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
    }
    void init(){
        firestore=FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("File", MODE_PRIVATE);
        rep=sharedPreferences.getBoolean("rep",false);
        recCNIC=getIntent().getStringExtra("CNIC");
        recToken=getIntent().getStringExtra("Token");
    }
}