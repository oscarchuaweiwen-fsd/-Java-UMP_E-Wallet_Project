package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transfer extends AppCompatActivity {
    private TextView transfer;
    private EditText searchPerson;
    private Button next;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "Transfer";
    boolean validationphonenumber=true;
    HashMap<String,String> phonenumber = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        searchPerson = findViewById(R.id.et_searchperson);
        transfer = findViewById(R.id.tv_transfer);
        next = findViewById(R.id.btn_submit);

        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                phonenumber.put(document.getData().get("phonenumber").toString(),document.getId().toString());
                                Log.d(TAG, String.valueOf(phonenumber));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String varx = searchPerson.getText().toString();

                    for(String i:phonenumber.keySet()){
                        if(i.equals(varx)){
                            Intent go2transfer2 = new Intent(Transfer.this, Transfer2.class);
                            go2transfer2.putExtra("uid",phonenumber.get(i));
                            startActivity(go2transfer2);
                        }
                    }

                if(validationphonenumber==false){
                    Toast.makeText(Transfer.this,"Register not found!",Toast.LENGTH_SHORT);
                }

            }
        });

    }
}