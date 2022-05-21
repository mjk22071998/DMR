package com.example.dmr.medicalrep.activities;

import static com.example.dmr.medicalrep.utils.SessionManager.CNIC;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.adapters.MessageAdapter;
import com.example.dmr.medicalrep.model.Chat;
import com.example.dmr.medicalrep.model.Message;
import com.example.dmr.medicalrep.utils.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    boolean rep;
    String recCNIC, recName,recToken;
    RecyclerView messageView;
    MessageAdapter adapter;
    TextInputEditText messageText;
    String id;
    FloatingActionButton send;
    List<Message> messages;
    ProgressDialog progressDialog;
    boolean newChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        init();
        getSupportActionBar().setTitle(recName);
        if (rep)
            id=recCNIC+SessionManager.getUser(getApplicationContext()).get(CNIC).toString();
        else
            id=SessionManager.getUser(getApplicationContext()).get(CNIC).toString()+recCNIC;
        getData();
        newChat=true;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!messageText.getText().toString().trim().isEmpty()){
                    if(!newChat){
                        sendMessage();
                    } else {
                        Chat chat=new Chat();
                        if (rep){
                            chat.setRepCNIC(SessionManager.getUser(getApplicationContext()).get(CNIC).toString());
                            chat.setRepToken(SessionManager.getToken(getApplicationContext()));
                            chat.setRepName(SessionManager.getUser(getApplicationContext()).get(SessionManager.FULL_NAME).toString());
                            chat.setDocCNIC(recCNIC);
                            chat.setDocToken(recToken);
                            chat.setDocName(recName);
                        } else{
                            chat.setDocCNIC(SessionManager.getUser(getApplicationContext()).get(CNIC).toString());
                            chat.setDocToken(SessionManager.getToken(getApplicationContext()));
                            chat.setDocName(SessionManager.getUser(getApplicationContext()).get(SessionManager.FULL_NAME).toString());
                            chat.setRepCNIC(recCNIC);
                            chat.setRepToken(recToken);
                            chat.setRepName(recName);
                        }
                        firestore.collection("chats").document(id).set(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                sendMessage();
                            }
                        });
                    }
                } else {
                    Toast.makeText(MessagesActivity.this, "Please enter the message", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    void sendMessage(){
        Message message=new Message();
        message.setMessage(messageText.getText().toString());
        message.setTimestamp(Timestamp.now());
        message.setSender(SessionManager.getUser(getApplicationContext()).get(CNIC).toString());
        firestore.collection("chats").document(id).collection("messages").add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MessagesActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                Map<String,String> map=new HashMap<>();
                map.put("title","Message from " + SessionManager.getUser(getApplicationContext()).get(SessionManager.FULL_NAME).toString());
                map.put("body",messageText.getText().toString());
                RemoteMessage remoteMessage=new RemoteMessage.Builder(recToken).setData(map).build();
                FirebaseMessaging.getInstance().send(remoteMessage);
            }
        });
    }
    void getData(){
        firestore.collection("chats").document(id).collection("messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value!=null){
                    if (value.isEmpty()||value.size()==0){
                        for (QueryDocumentSnapshot queryDocumentSnapshot:value){
                            messages.add(queryDocumentSnapshot.toObject(Message.class));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MessagesActivity.this, "No messages In this chat", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MessagesActivity.this, "No Message in this chat", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void init(){
        firestore=FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("File", MODE_PRIVATE);
        rep=sharedPreferences.getBoolean("rep",false);
        recCNIC=getIntent().getStringExtra("CNIC");
        recToken=getIntent().getStringExtra("token");
        recName=getIntent().getStringExtra("name");
        messages=new ArrayList<>();
        messageView=findViewById(R.id.messages);
        adapter=new MessageAdapter(this,messages);
        id=getIntent().getStringExtra("id");
        messageText=findViewById(R.id.messageText);
        send=findViewById(R.id.send);
        progressDialog=new ProgressDialog(this);
    }
}