package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyCard2 extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mauth = FirebaseAuth.getInstance();
    Button addthiscard;
    private EditText cardnumber,month,cvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card2);

        FirebaseUser user = mauth.getCurrentUser();
        String uid = user.getUid();
        addthiscard = findViewById(R.id.btn_addthiscard);
        cardnumber = findViewById(R.id.et_cardnumber);
        month = findViewById(R.id.et_expireddate);
        cvv = findViewById(R.id.et_cvv);

        addthiscard.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "MyCard2";

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {


                Map<String,Object> data= new HashMap<String,Object>();
                data.put("carddetail",cardnumber.getText().toString());
                data.put("exp",month.getText().toString());
                data.put("cvv",cvv.getText().toString());

                Map<String,Object> data2= new HashMap<String,Object>();
                data2.put("card",FieldValue.arrayUnion(data));

                DocumentReference carddetail = db.collection("user").document(uid);

                carddetail.update(data2);

                carddetail.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                           String card =  document.getData().get("card").toString();
                            Log.d(TAG, card );
                        }
                    }
                });

                Toast.makeText(MyCard2.this,"Card Added Successfully",Toast.LENGTH_SHORT).show();


                Intent gotohomepage = new Intent(MyCard2.this,HomePage.class);
                startActivity(gotohomepage);

            }
        });

    }
}