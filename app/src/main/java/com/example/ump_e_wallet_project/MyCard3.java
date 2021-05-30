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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyCard3 extends AppCompatActivity {

    private static final String TAG = "MyCard3";
    private TextView carddetail1,cvv1,exp1;
    private Button delete;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mauth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card3);
        String carddetail = getIntent().getStringExtra("carddetail");
        String cvv = getIntent().getStringExtra("cvv");
        String exp = getIntent().getStringExtra("exp");

        Log.d(TAG,carddetail + " "+ cvv + " " + exp);

        carddetail1 = findViewById(R.id.tv_card);
        cvv1 = findViewById(R.id.tv_cvv);
        exp1 = findViewById(R.id.tv_exp);
        delete = findViewById(R.id.btn_delete);
        FirebaseUser user = mauth.getCurrentUser();
        String uid = user.getUid();


        carddetail1.setText(carddetail);
        cvv1.setText(cvv);;
        exp1.setText(exp);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> data= new HashMap<String,Object>();
                data.put("carddetail",carddetail);
                data.put("exp",exp);
                data.put("cvv",cvv);

                Map<String,Object> data2= new HashMap<String,Object>();
                data2.put("card", FieldValue.arrayRemove(data));

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

                Toast.makeText(MyCard3.this,"Card Deleted Successfully",Toast.LENGTH_SHORT).show();


                Intent gotohomepage = new Intent(MyCard3.this,HomePage.class);
                startActivity(gotohomepage);
            }
        });
    }
}