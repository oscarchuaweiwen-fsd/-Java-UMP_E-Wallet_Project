package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Withdraw extends AppCompatActivity {
    private EditText amountwithdraw;
    private ListView listview;
    private TextView balancetv;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mauth = FirebaseAuth.getInstance();
    ArrayList<String> arrayList1 = new ArrayList<>();
    ArrayList<String> listviewlist1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        FirebaseUser user = mauth.getCurrentUser();
        String uid = user.getUid();
        balancetv = findViewById(R.id.tv_balancewallet);
        amountwithdraw = findViewById(R.id.et_withdrawamount);
        listview = findViewById(R.id.lv_card);

        DocumentReference docRef = db.collection("user").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "Withdraw";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String balance = document.getData().get("balance").toString();
                        balancetv.setText("RM " + balance);

                        UserResponse userResponse = document.toObject(UserResponse.class);
                        if (userResponse.getCard() == null) {
                            Log.d(TAG, "Document :" + userResponse.getCard());
                        } else {
                            ArrayList<Card> cards = userResponse.getCard();

                            for (Card language : cards) {
                                arrayList1.add(language.getCarddetail());
                                arrayList1.add(language.getCvv());
                                arrayList1.add(language.getExp());
                            }


                            for (int x = 0; x < arrayList1.size(); x = x + 3) {
                                listviewlist1.add(arrayList1.get(x));
                            }

                            Log.d(TAG, "size : " + String.valueOf(cards.size()));
                            ArrayAdapter arrayAdapter = new ArrayAdapter(Withdraw.this, android.R.layout.simple_list_item_1, listviewlist1);
                            listview.setAdapter(arrayAdapter);


                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Log.d(TAG, "checking" + arrayList1.get(position).toString());

                                    Intent gotomycard = new Intent(Withdraw.this, Withdraw2.class);

                                    for (int x = 0; x < 3; x++) {
                                        int arrayposition = position * 3;

                                        if (x == 0) {
                                            gotomycard.putExtra("carddetail", arrayList1.get(arrayposition));
                                        } else if (x == 1) {
                                            gotomycard.putExtra("cvv", arrayList1.get(arrayposition + 1));
                                        } else if (x == 2) {
                                            gotomycard.putExtra("exp", arrayList1.get(arrayposition + 2));
                                        }
                                    }

                                    gotomycard.putExtra("amountwithdraw",amountwithdraw.getText().toString());
                                    gotomycard.putExtra("balance",balance);
                                    startActivity(gotomycard);
                                }
                            });

                        }
                    }
                }

            }
        });
    }
}