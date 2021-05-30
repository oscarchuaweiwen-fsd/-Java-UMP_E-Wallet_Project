package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminPay extends AppCompatActivity {
    private EditText uid,paymentdetails,amountgven;
    private Button addpayment,signout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pay);

        uid = findViewById(R.id.et_adminuid);
        paymentdetails = findViewById(R.id.et_paymentname);
        amountgven = findViewById(R.id.et_amountadding);
        addpayment = findViewById(R.id.btn_addpayment);
        signout = findViewById(R.id.btn_adminsignout);

        addpayment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Map<String,Object> data= new HashMap<String,Object>();
                data.put("paymentdetail",paymentdetails.getText().toString());
                data.put("amount",amountgven.getText().toString());

                Map<String,Object> data2= new HashMap<String,Object>();
                data2.put("payment", FieldValue.arrayUnion(data));

                String uiduser = uid.getText().toString();
                DocumentReference paymentdetail = db.collection("user").document(uiduser);

                paymentdetail.update(data2);

                paymentdetail.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    private static final String TAG = "AdminPay";

                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            String card =  document.getData().get("payment").toString();
                            Log.d(TAG, card );
                        }
                    }
                });

                Toast.makeText(AdminPay.this,"Payment Detail Added Successfully",Toast.LENGTH_SHORT).show();

            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(AdminPay.this, "Sign out Successfully!", Toast.LENGTH_SHORT).show();
                Intent gotomainpage = new Intent(AdminPay.this,MainActivity.class);
                startActivity(gotomainpage);
            }
        });
    }
}