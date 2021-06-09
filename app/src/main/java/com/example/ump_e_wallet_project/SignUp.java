package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthMeter;


public class SignUp extends AppCompatActivity {
    private EditText name, email, phonenumber, password, passwordc;
    private Button buttonsignup;
    FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorage;
    private ImageView myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        name = findViewById(R.id.et_name);
        email = findViewById(R.id.et_emailaddresssignup);
        phonenumber = findViewById(R.id.et_phonenumber);
        password = findViewById(R.id.et_passwordsignup);
        passwordc = findViewById(R.id.et_passwordsignupc);
        buttonsignup = findViewById(R.id.btn_signup);


        PasswordStrengthMeter meter = findViewById(R.id.passwordInputMeter);
        meter.setEditText(password);

        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid = email.getText().toString();
                String passwordid = password.getText().toString();
                String passwordidc = passwordc.getText().toString();
                if(emailid.isEmpty()){
                    email.setError("Please enter your email address!");
                    email.requestFocus();
                }else if(passwordid.isEmpty()){
                    password.setError("Please enter your password!");
                    password.requestFocus();
                }else if(!(emailid.isEmpty() && passwordid.isEmpty()))
                {
                    mFirebaseAuth.fetchSignInMethodsForEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                    if (isNewUser) {


                                        if (emailid.isEmpty()) {
                                            email.setError("Please enter your email address!");
                                            email.requestFocus();
                                        } else if (passwordid.isEmpty()) {
                                            password.setError("Please enter your password!");
                                            password.requestFocus();
                                        } else if (!(emailid.isEmpty() && passwordid.isEmpty())) {

                                            if (passwordid.length() < 6) {
                                                password.setError("Password is less than 6 character!");
                                                password.requestFocus();
                                            } else {

                                                if(!passwordid.equals(passwordidc)){
                                                    passwordc.setError("Password is not same,please try it again!");
                                                    passwordc.requestFocus();
                                                }else{
                                                    mFirebaseAuth.createUserWithEmailAndPassword(emailid, passwordid).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (!task.isSuccessful()) {
                                                                Toast.makeText(SignUp.this, "Sign Up Unsuccessful!", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(SignUp.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();

                                                                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                                                String id = mFirebaseAuth.getCurrentUser().getUid();
                                                                String name1 = name.getText().toString();
                                                                String phonenumber1 = phonenumber.getText().toString();
                                                                String email1 = email.getText().toString();

                                                                Map<String, Object> userinfo = new HashMap<>();
                                                                userinfo.put("name", name1);
                                                                userinfo.put("phonenumber", phonenumber1);
                                                                userinfo.put("email", email1);
                                                                userinfo.put("balance", "0");

                                                                db.collection("user").document(id).set(userinfo);

                                                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(SignUp.this, "Verification Email Has Been Sent!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.d("TAG", "Verification Email Sent Error" + e.getMessage());
                                                                    }
                                                                });

                                                                Intent back2home = new Intent(SignUp.this, MainActivity.class);
                                                                startActivity(back2home);
                                                            }
                                                        }
                                                    });
                                                }
                                            }

                                        }

                                    } else {
                                        email.setError("Email existed!");
                                        email.requestFocus();
                                    }

                                }
                            });
                }



            }
        });
    }



}