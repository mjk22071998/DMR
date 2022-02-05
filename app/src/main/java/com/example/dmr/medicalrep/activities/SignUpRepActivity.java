package com.example.dmr.medicalrep.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignUpRepActivity extends AppCompatActivity {

    TextInputEditText fullNameEt,phoneNumberEt,addressEt,cnicEt,emailEt,passwordEt,cPasswordEt;
    String fullName,phoneNumber,address,cnic,password,cPassword,email;
    Button signUp;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    CollectionReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_rep);
        init();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName=fullNameEt.getText().toString();
                phoneNumber=phoneNumberEt.getText().toString();
                address=addressEt.getText().toString();
                cnic=cnicEt.getText().toString();
                password=passwordEt.getText().toString();
                cPassword=cPasswordEt.getText().toString();
                if (validate()) {
                    progressDialog.setMessage("Signing Up");
                    progressDialog.show();
                    Map<String, Object> data = new HashMap<>();
                    data.put("Full Name", fullName);
                    data.put("Email", email);
                    data.put("CNIC", cnic);
                    data.put("Role","Rep");
                    data.put("phoneNumber", phoneNumber);
                    data.put("Address", address);
                    auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            PhoneAuthOptions options =
                                    PhoneAuthOptions.newBuilder(auth)
                                            .setPhoneNumber("+92"+phoneNumber)       // Phone number to verify
                                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                            .setActivity(SignUpRepActivity.this)                 // Activity (for callback binding)
                                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                @Override
                                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                    auth.signOut();
                                                    authResult.getUser().sendEmailVerification();
                                                    startActivity(new Intent(SignUpRepActivity.this,LoginActivity.class));
                                                    progressDialog.dismiss();
                                                }

                                                @Override
                                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                    super.onCodeSent(s, forceResendingToken);
                                                    progressDialog.dismiss();
                                                    AlertDialog dialog;
                                                    MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(SignUpRepActivity.this);
                                                    View view=getLayoutInflater().inflate(R.layout.item_otp,null,false);
                                                    builder.setView(view);
                                                    MaterialButton submitOTP=view.findViewById(R.id.verifyOTP);
                                                    TextInputEditText editText=view.findViewById(R.id.otp);
                                                    dialog=builder.create();
                                                    dialog.show();
                                                    submitOTP.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(s, editText.getText().toString());
                                                            auth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                                @Override
                                                                public void onSuccess(@NonNull AuthResult authResult) {
                                                                    auth.signOut();
                                                                    reference.document(email).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            auth.signOut();
                                                                            progressDialog.dismiss();
                                                                            startActivity(new Intent(SignUpRepActivity.this,LoginActivity.class));
                                                                        }
                                                                    });
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    e.printStackTrace();
                                                                    Toast.makeText(SignUpRepActivity.this, "Phone number verification failed", Toast.LENGTH_SHORT).show();
                                                                    authResult.getUser().delete();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .build();
                            PhoneAuthProvider.verifyPhoneNumber(options);
                        }
                    });
                }
            }
        });
    }
    void init(){
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        reference = firestore.collection("Users");
        fullNameEt =findViewById(R.id.full_name);
        cnicEt =findViewById(R.id.cnic);
        addressEt =findViewById(R.id.address);
        phoneNumberEt =findViewById(R.id.phone_number);
        emailEt =findViewById(R.id.email);
        passwordEt =findViewById(R.id.password);
        cPasswordEt =findViewById(R.id.confirm_password);
        signUp=findViewById(R.id.sign_up);
        progressDialog=new ProgressDialog(this);
    }

    boolean validate() {
        if (fullName.isEmpty()) {
            fullNameEt.setError("Please enter your Full Name");
            fullNameEt.requestFocus();
            return false;
        } else if (cnic.isEmpty()||cnic.length()<13){
            cnicEt.setError("Please enter your CNIC");
            cnicEt.requestFocus();
            return false;
        } else if (address.isEmpty()){
            addressEt.setError("Please enter your address");
            return false;
        } else if (phoneNumber.isEmpty()||phoneNumber.length()<11){
            phoneNumberEt.setError("Please enter your phone Number");
            phoneNumberEt.requestFocus();
            return false;
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.setError("Enter correct email address");
            emailEt.requestFocus();
            return false;
        } else if (password.isEmpty()||password.length()<8){
            passwordEt.setError("The Password is too short");
            passwordEt.requestFocus();
            return false;
        } else if (cPassword.isEmpty()||!cPassword.equals(password)){
            cPasswordEt.setError("Password did not match");
            cPasswordEt.requestFocus();
            return false;
        }
        return true;
    }
}