package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePage extends AppCompatActivity {

    private TextView username,balance;
    private Button topup,signout,transfer,mycard,payment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        username = findViewById(R.id.tv_username);
        balance = findViewById(R.id.tv_balance);
        topup = findViewById(R.id.btn_topup);
        signout = findViewById(R.id.btn_signout);
        transfer = findViewById(R.id.btn_transfer);
        mycard = findViewById(R.id.btn_mycard);
        payment = findViewById(R.id.btn_pay);

        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        DocumentReference userinfo = db.collection("user").document(
                uid);

        userinfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();


                username.setText(document.getData().get("name").toString());
                balance.setText("RM " + document.getData().get("balance").toString());
            }
        });

        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topuppage = new Intent(HomePage.this,TopUp.class);
                startActivity(topuppage);
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transferpage = new Intent(HomePage.this,Transfer.class);
                startActivity(transferpage);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mycardpage = new Intent(HomePage.this,MyCard.class);
                startActivity(mycardpage);
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentpage = new Intent(HomePage.this,Pay.class);
                startActivity(paymentpage);
            }
        });
    }
}