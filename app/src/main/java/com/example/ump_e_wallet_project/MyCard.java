package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    ArrayList <String>listviewlist = new ArrayList<>();

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
                                arrayList.add(language.getCvv());
                                arrayList.add(language.getExp());
                            }


                            for(int x = 0; x<arrayList.size(); x = x + 3){
                                listviewlist.add(arrayList.get(x));
                            }

                            Log.d(TAG, "size : " + String.valueOf(cards.size()));
                            ArrayAdapter arrayAdapter = new ArrayAdapter(MyCard.this, android.R.layout.simple_list_item_1,listviewlist);
                            lv.setAdapter(arrayAdapter);


                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Log.d(TAG, "checking" + arrayList.get(position).toString());
                                    Toast.makeText(MyCard.this,"position" + position + " " + arrayList.get(position).toString(),Toast.LENGTH_SHORT).show();
                                    Intent gotomycard = new Intent(MyCard.this,MyCard3.class);

                                    for(int x=0;x<3;x++){
                                        int arrayposition = position * 3;

                                        if(x==0){
                                            gotomycard.putExtra("carddetail",arrayList.get(arrayposition));
                                        }else if(x==1){
                                            gotomycard.putExtra("cvv",arrayList.get(arrayposition+1));
                                        }else if(x==2){
                                            gotomycard.putExtra("exp",arrayList.get(arrayposition+2));
                                        }
                                    }


                                    startActivity(gotomycard);
                                }
                            });
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