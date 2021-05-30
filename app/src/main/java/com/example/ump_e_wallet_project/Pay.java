package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Pay extends AppCompatActivity {
    private ListView listview;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mauth = FirebaseAuth.getInstance();
    ArrayList<String>arrayList = new ArrayList<>();
    ArrayList<String>paymentlistview = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        listview = findViewById(R.id.lv_paylist);

        FirebaseUser user = mauth.getCurrentUser();
        String uid = user.getUid();

        DocumentReference docRef = db.collection("user").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "Pay";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserResponse userResponse = document.toObject(UserResponse.class);

                        if(userResponse.getPayment() == null){
                            Log.d(TAG,"Document :" + userResponse.getCard());
                        }else{

                            ArrayList<Payment> payment = userResponse.getPayment();
                            for (Payment payment1: payment){
                                arrayList.add(payment1.getPaymentdetail());
                                arrayList.add(payment1.getAmount());

                            }


                            for(int x = 0; x<arrayList.size(); x = x + 2){
                                paymentlistview.add(arrayList.get(x));
                            }


                            ArrayAdapter arrayAdapter = new ArrayAdapter(Pay.this, android.R.layout.simple_list_item_1,paymentlistview);
                            listview.setAdapter(arrayAdapter);

                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent gotomycard = new Intent(Pay.this,Pay2.class);

                                    for(int x=0;x<2;x++){
                                        int arrayposition = position * 2;

                                        if(x==0){
                                            gotomycard.putExtra("paymentdetail",arrayList.get(arrayposition));
                                        }else if(x==1){
                                            gotomycard.putExtra("amount",arrayList.get(arrayposition+1));
                                        }
                                    }


                                    startActivity(gotomycard);
                                }
                            });

                            Log.d(TAG, "DocumentSnapshot data: "+ userResponse.getPayment());
                    }
            }
        };

        }


    });
}
}