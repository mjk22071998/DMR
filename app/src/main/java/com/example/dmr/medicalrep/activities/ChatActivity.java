package com.example.dmr.medicalrep.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.adapters.ChatAdapter;
import com.example.dmr.medicalrep.model.Chat;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    ChatAdapter adapter;
    List<Chat> chats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

    }

    void init(){
        firestore=FirebaseFirestore.getInstance();
    }
}