package com.example.dmr.medicalrep.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.utils.SessionManager;
import com.github.drjacky.imagepicker.ImagePicker;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignUpDocActivity extends AppCompatActivity {

    TextInputEditText fullNameEt,phoneNumberEt,addressEt,cnicEt,emailEt,passwordEt,cPasswordEt,cityEt;
    String fullName,phoneNumber,address,cnic,password,cPassword,email,city;
    ImageView cert;
    Bitmap bitmap;
    Button selectImage,signUp;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    CollectionReference reference;
    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    if (Build.VERSION.SDK_INT >= 29){
                        try {
                            bitmap= ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(),uri));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else{
                        try {
                            bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    cert.setImageBitmap(bitmap);
                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    ImagePicker.Companion.getError(result.getData());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_doc);
        init();
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=ImagePicker.with(SignUpDocActivity.this)
                        .galleryOnly()
                        .createIntent();
                launcher.launch(intent);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName=fullNameEt.getText().toString();
                phoneNumber=phoneNumberEt.getText().toString();
                address=addressEt.getText().toString();
                cnic=cnicEt.getText().toString();
                city=cityEt.getText().toString();
                email=emailEt.getText().toString();
                password=passwordEt.getText().toString();
                cPassword=cPasswordEt.getText().toString();
                if (validate()){
                    progressDialog.setMessage("Signing Up");
                    progressDialog.show();
                    Map<String,Object> data=new HashMap<>();
                    data.put("fullName",fullName);
                    data.put("Email",email);
                    data.put("City",city);
                    data.put("CNIC",cnic);
                    data.put("Role","Doc");
                    data.put("phoneNumber",phoneNumber);
                    data.put("Address",address);
                    auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(SignUpDocActivity.this, authResult -> {
                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(auth)
                                        .setPhoneNumber("+92"+phoneNumber)       // Phone number to verify
                                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                        .setActivity(SignUpDocActivity.this)                 // Activity (for callback binding)
                                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                            @Override
                                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                authResult.getUser().sendEmailVerification();
                                                startActivity(new Intent(SignUpDocActivity.this,LoginActivity.class));
                                                auth.signOut();
                                                progressDialog.dismiss();
                                            }

                                            @Override
                                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                super.onCodeSent(s, forceResendingToken);
                                                progressDialog.dismiss();
                                                AlertDialog dialog;
                                                MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(SignUpDocActivity.this);
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

                                                                reference.document(email).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        authResult.getUser().sendEmailVerification();
                                                                        auth.signOut();
                                                                        progressDialog.dismiss();
                                                                        SharedPreferences sharedPreferences = getSharedPreferences("File", MODE_PRIVATE);
                                                                        SharedPreferences.Editor editor= sharedPreferences.edit();
                                                                        editor.putBoolean("rep",false);
                                                                        Toast.makeText(SignUpDocActivity.this, "A verification email has been sent", Toast.LENGTH_SHORT).show();
                                                                        editor.apply();
                                                                        startActivity(new Intent(SignUpDocActivity.this,LoginActivity.class));
                                                                    }
                                                                });
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(SignUpDocActivity.this, "Phone number verification failed", Toast.LENGTH_SHORT).show();
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
                        StorageReference reference=storage.getReference().child("images/"+ data.get(SessionManager.CNIC).toString()+".jpg");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] image = baos.toByteArray();
                        UploadTask uploadTask=reference.putBytes(image);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                PhoneAuthProvider.verifyPhoneNumber(options);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }).addOnFailureListener(SignUpDocActivity.this, e -> {
                        e.printStackTrace();
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
        selectImage=findViewById(R.id.select_image);
        phoneNumberEt =findViewById(R.id.phone_number);
        emailEt =findViewById(R.id.email);
        storage=FirebaseStorage.getInstance();
        cityEt=findViewById(R.id.city);
        passwordEt =findViewById(R.id.password);
        cPasswordEt =findViewById(R.id.confirm_password);
        signUp=findViewById(R.id.sign_up);
        cert=findViewById(R.id.certImage);
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
            addressEt.requestFocus();
            return false;
        }else if (city.isEmpty()){
            cityEt.setError("Please enter your address");
            cityEt.requestFocus();
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
        } else if (bitmap==null){
            cert.requestFocus();
            Toast.makeText(getApplicationContext(), "Please select the Image of the certificate", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}