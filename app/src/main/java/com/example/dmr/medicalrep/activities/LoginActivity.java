package com.example.dmr.medicalrep.activities;

import static com.example.dmr.medicalrep.utils.SessionManager.PHONE_NUMBER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.dmr.medicalrep.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText emailEt,passwordEt;
    String email,password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    MaterialButton signIn;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    boolean rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=emailEt.getText().toString();
                password=passwordEt.getText().toString();
                if (validate()){
                    auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(LoginActivity.this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            firestore.collection("User").document(authResult.getUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                                            .setPhoneNumber("+92" + documentSnapshot.getData().get(PHONE_NUMBER).toString())
                                            .setTimeout(60L, TimeUnit.SECONDS)
                                            .setActivity(LoginActivity.this)
                                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                @Override
                                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                }
                                                @Override
                                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                    super.onCodeSent(s, forceResendingToken);
                                                    AlertDialog dialog;
                                                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(LoginActivity.this);
                                                    View view = getLayoutInflater().inflate(R.layout.item_otp, null, false);
                                                    builder.setView(view);
                                                    MaterialButton submitOTP = view.findViewById(R.id.verifyOTP);
                                                    TextInputEditText editText = view.findViewById(R.id.otp);
                                                    dialog = builder.create();
                                                    dialog.show();
                                                    submitOTP.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(s, editText.getText().toString());
                                                            auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                                @Override
                                                                public void onSuccess(@NonNull AuthResult authResult) {
                                                                    editor = sharedPreferences.edit();
                                                                    editor.putBoolean("auth", true);
                                                                    editor.apply();
                                                                    rep=sharedPreferences.getBoolean("rep",false);
                                                                    if (rep){
                                                                        startActivity(new Intent(LoginActivity.this, DashboardRepActivity.class));
                                                                    } else {
                                                                        startActivity(new Intent(LoginActivity.this, DashboardDoctorActivity.class));
                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getApplicationContext(), "Phone Number Not verified", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                                @Override
                                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(LoginActivity.this, "Phone number verification failed", Toast.LENGTH_SHORT).show();
                                                    authResult.getUser().delete();
                                                }
                                            })
                                            .build();
                                    PhoneAuthProvider.verifyPhoneNumber(options);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "User not found as patient", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    void init(){
        emailEt=findViewById(R.id.email);
        passwordEt=findViewById(R.id.password);
        signIn=findViewById(R.id.sign_in);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        sharedPreferences=getSharedPreferences("File",MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    boolean validate(){
        if (password.length()<8){
            passwordEt.setError("Password too short");
            return false;
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("Enter correct email address");
            return false;
        } else {
            return true;
        }
    }
}