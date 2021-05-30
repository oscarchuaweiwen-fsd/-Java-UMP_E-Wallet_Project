package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyCard extends AppCompatActivity {
    private Button addmycard;
    private static final String TAG = "MyCard";
    private ListView lv;
    ArrayList <String>arrayList = new ArrayList<>();
    FirebaseAuth mauth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mauth.getCurrentUser();
        String uid = user.getUid();

        addmycard = findViewById(R.id.btn_addnewcard);
        lv = findViewById(R.id.lv_carddetail);

        DocumentReference docRef = db.collection("user").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserResponse userResponse = document.toObject(UserResponse.class);
                        if(userResponse.getCard() == null){
                            Log.d(TAG,"Document :" + userResponse.getCard());
                        }else{
                            ArrayList<Card> cards = userResponse.getCard();

                            for (Card language: cards){
                                arrayList.add(language.getCarddetail());
                                Log.d(TAG,language.getCarddetail());
                            }

                            ArrayAdapter arrayAdapter = new ArrayAdapter(MyCard.this, android.R.layout.simple_list_item_1,arrayList);
                            lv.setAdapter(arrayAdapter);

                        }




                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



        addmycard.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent mycard2page = new Intent(MyCard.this,MyCard2.class);
                startActivity(mycard2page);

            }
        });
    }
}