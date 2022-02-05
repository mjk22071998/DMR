package com.example.dmr.medicalrep.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dmr.medicalrep.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

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