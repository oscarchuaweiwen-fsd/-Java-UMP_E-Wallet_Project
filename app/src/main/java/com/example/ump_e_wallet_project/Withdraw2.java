package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Withdraw2 extends AppCompatActivity {
    private TextView carddetails,amountwithdraw;
    private Button submit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mauth = FirebaseAuth.getInstance();
    private static final String TAG = "Withdraw2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw2);

        String carddetail = getIntent().getStringExtra("carddetail");
        String cvv = getIntent().getStringExtra("cvv");
        String exp = getIntent().getStringExtra("exp");
        String balance = getIntent().getStringExtra("balance");
        String amountwithdraw2 = getIntent().getStringExtra("amountwithdraw");
        FirebaseUser user = mauth.getCurrentUser();
        String uid = user.getUid();

        carddetails = findViewById(R.id.tv_cardnumberdetailwithdraw);
        amountwithdraw = findViewById(R.id.tv_amountwithdraw2);
        submit = findViewById(R.id.btn_confirmwithdraw);

        carddetails.setText(carddetail);
        amountwithdraw.setText(amountwithdraw2);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference withdraw = db.collection("user").document(uid);

                int amountwithdraw = Integer.parseInt(amountwithdraw2);
                int balanceacc = Integer.parseInt(balance);

              int  newBalanceacc = balanceacc - amountwithdraw;

              String newBaanceacc1 = String.valueOf(newBalanceacc);

                withdraw
                        .update("balance", newBaanceacc1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {


                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                Toast.makeText(Withdraw2.this,"Withdraw Successfully!",Toast.LENGTH_SHORT).show();
                                Intent gotohomepage1 = new Intent(Withdraw2.this,HomePage.class);
                                startActivity(gotohomepage1);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        });

    }
}