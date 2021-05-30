package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Paint;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private TextView intentSignup,intentForgotpassword;
    private EditText email,password;
    private Button login;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        intentSignup = findViewById(R.id.tv_intentsignup);
        intentForgotpassword = findViewById(R.id.tv_intentforgotpassword);
        login = findViewById(R.id.btn_login);
        email = findViewById(R.id.et_emailaddress);
        password = findViewById(R.id.et_password);

        intentSignup.setPaintFlags(intentSignup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        intentForgotpassword.setPaintFlags(intentForgotpassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        final BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()){

            case BiometricManager.BIOMETRIC_SUCCESS:

                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this,"No fingerprint sensor",Toast.LENGTH_LONG).show();
                login.setVisibility(View.INVISIBLE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this,"Biometric sensor is not available",Toast.LENGTH_LONG).show();
                login.setVisibility(View.INVISIBLE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this,"Your device don't have any fingerprint, check your security setting",Toast.LENGTH_LONG).show();
                login.setVisibility(View.INVISIBLE);
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        final BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this,executor,new BiometricPrompt.AuthenticationCallback(){

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        final BiometricPrompt.PromptInfo  promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("User fingerprint to login")
                .setNegativeButtonText("cancel")
                .build();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String emailinput = email.getText().toString();
                String passwordinput = password.getText().toString();
                biometricPrompt.authenticate(promptInfo);
                mAuth.signInWithEmailAndPassword(emailinput, passwordinput)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if(user.isEmailVerified()){
                                        Toast.makeText(MainActivity.this, "Sign In Successful!", Toast.LENGTH_SHORT).show();

                                        Intent homepage = new Intent(MainActivity.this,HomePage.class);
                                        startActivity(homepage);
                                    }else{
                                        Toast.makeText(MainActivity.this, "Please verify your email!", Toast.LENGTH_SHORT).show();

                                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(MainActivity.this, "Verification Email Has Been Sent!" ,Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("TAG", "Verification Email Sent Error" + e.getMessage());
                                            }
                                        });
                                    }


                                } else {
                                    // If sign in fails, display a message to the user.


                                    Toast.makeText(MainActivity.this, "Sign In Unsuccessful \nEmail Address or Password not Matched!", Toast.LENGTH_SHORT).show();


                                }
                            }
                        });

    }



});

}
    public void click(View v){
        Intent intent;

        switch (v.getId()){
            case R.id.tv_intentsignup: intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
                break;

            case R.id.tv_intentforgotpassword: intent = new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(intent);
                break;
        }


    };
}