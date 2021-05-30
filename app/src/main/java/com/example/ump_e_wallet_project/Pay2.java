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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Pay2 extends AppCompatActivity {
    private static final String TAG = "Pay2";
    private TextView paymentdetails, amountpayment;
    private Button btnpayment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mauth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay2);

        String paymentdetail = getIntent().getStringExtra("paymentdetail");
        String amount = getIntent().getStringExtra("amount");


        Log.d(TAG,paymentdetail + " "+ amount);

        paymentdetails = findViewById(R.id.tv_paymentdetails);
        amountpayment = findViewById(R.id.tv_amountpayment);
        btnpayment = findViewById(R.id.btn_paypayment);

        paymentdetails.setText(paymentdetail);
        amountpayment.setText("RM " + amount);
        FirebaseUser user = mauth.getCurrentUser();
        String uid = user.getUid();

        btnpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> data= new HashMap<String,Object>();
                data.put("paymentdetail",paymentdetail);
                data.put("amount",amount);


                Map<String,Object> data2= new HashMap<String,Object>();
                data2.put("payment", FieldValue.arrayRemove(data));

                DocumentReference paymentdetailss = db.collection("user").document(uid);

                paymentdetailss.update(data2);

                paymentdetailss.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            String payment =  document.getData().get("payment").toString();
                            String balance = document.getData().get("balance").toString();
                            int balance1 = Integer.parseInt(balance);
                            int amount1 = Integer.parseInt(amount);
                            int newBalance = balance1 - amount1;
                            String newBalance1 = String.valueOf(newBalance);


                            DocumentReference washingtonRef = db.collection("user").document(uid);

// Set the "isCapital" field of the city 'DC'
                            washingtonRef
                                        .update("balance", newBalance1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                        }
                    }
                });

                Toast.makeText(Pay2.this,"Payment Paid Successfully",Toast.LENGTH_SHORT).show();


                Intent gotohomepage = new Intent(Pay2.this,HomePage.class);
                startActivity(gotohomepage);
            }
        });



    }
}