package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Transfer2 extends AppCompatActivity {
    private static final String TAG = "Transfer";
    private TextView name, userphonenumber,amountupto;
    private EditText amounttransfer;
    private Button confirmtransfer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mauth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer2);

        name = findViewById(R.id.tv_receivername);
        userphonenumber = findViewById(R.id.tv_receiverphonenumber);
        amountupto = findViewById(R.id.tv_amountup);
        confirmtransfer = findViewById(R.id.btn_confirmtransfer);
        amounttransfer = findViewById(R.id.et_amounttransfer);
        FirebaseUser user = mauth.getCurrentUser();
        String uid = user.getUid();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String value = extras.getString("uid"); //receiver id
            DocumentReference docRefr = db.collection("user").document(value); //receiver info
            docRefr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String username = document.getData().get("name").toString();
                            String phonenumber = document.getData().get("phonenumber").toString();
                            String amountup2 = document.getData().get("balance").toString(); // receiver amount

                            name.setText(username);
                            userphonenumber.setText(phonenumber);


                            DocumentReference docRefs = db.collection("user").document(uid); //sender info
                            docRefs.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String amountreceiver = document.getData().get("balance").toString(); //sender amount
                                            amountupto.setText("RM " + amountreceiver);

                                            confirmtransfer.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {


                                                    String getAmount = amounttransfer.getText().toString();
                                                    int enterAmount = Integer.parseInt(getAmount);
                                                    int senderAmount = Integer.parseInt(amountreceiver);
                                                    int receiverAmount = Integer.parseInt(amountup2);

                                            if(enterAmount > senderAmount){
                                                    Toast.makeText(Transfer2.this, "Sorry, your amount entered is exceed from your balance!",Toast.LENGTH_SHORT).show();
                                             }else{
                                                int newsenderAmount = senderAmount - enterAmount;
                                                int newreceiverAmount = receiverAmount + enterAmount;

                                                docRefr.update("balance",String.valueOf(newreceiverAmount));
                                                docRefs.update("balance",String.valueOf(newsenderAmount));

                                                Toast.makeText(Transfer2.this, "Transfer Successfully!", Toast.LENGTH_SHORT).show();
                                                Intent gotohomepage = new Intent(Transfer2.this, HomePage.class);
                                                startActivity(gotohomepage);

                                            }

                                                }
                                            });

                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });


                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            //end
        }

    }
}