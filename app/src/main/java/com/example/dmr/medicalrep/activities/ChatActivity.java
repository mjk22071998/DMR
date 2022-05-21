package com.example.dmr.medicalrep.activities;

import static com.example.dmr.medicalrep.utils.SessionManager.CNIC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.adapters.ChatAdapter;
import com.example.dmr.medicalrep.model.Chat;
import com.example.dmr.medicalrep.utils.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    ChatAdapter adapter;
    List<Chat> chats;
    boolean rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        Task<QuerySnapshot> task=
                rep? firestore.collection("chats")
                        .whereEqualTo("repCNIC",SessionManager.getUser(getApplicationContext())
                                .get(CNIC).toString()).get()
                        :firestore.collection("chats")
                        .whereEqualTo("docCNIC",SessionManager.getUser(this).get(CNIC).toString()).get();
        task.addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots){
                    Chat chat=queryDocumentSnapshot.toObject(Chat.class);
                    chat.setId(queryDocumentSnapshot.getId());
                    chats.add(chat);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Failed to fetch chats",Toast.LENGTH_SHORT).show();
            }
        });
    }

    void init(){
        firestore=FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.chats);
        chats=new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("File", MODE_PRIVATE);
        rep=sharedPreferences.getBoolean("rep",false);
        adapter=new ChatAdapter(this,chats,rep);
        adapter.setOnChatClickListener(new ChatAdapter.OnChatClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(getApplicationContext(),MessagesActivity.class);
                if (rep){
                    intent.putExtra("CNIC",chats.get(position).getDocCNIC());
                    intent.putExtra("token",chats.get(position).getDocToken());
                    intent.putExtra("name",chats.get(position).getDocName());
                } else {
                    intent.putExtra("CNIC",chats.get(position).getRepCNIC());
                    intent.putExtra("token",chats.get(position).getRepToken());
                    intent.putExtra("name",chats.get(position).getRepName());
                }
                startActivity(intent);
            }
        });
    }
}