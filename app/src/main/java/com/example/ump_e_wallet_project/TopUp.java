package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TopUp extends AppCompatActivity {


    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button button;
    private EditText eta;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);


        radioGroup = findViewById(R.id.rg_amount);
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton =findViewById(radioId);
        button = findViewById(R.id.btn_topup);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = radioButton.getText().toString();
                int amount = Integer.parseInt(str.replaceAll("[\\D]" , "" ));

                topUp(amount);

            }
        });
    }

    public void topUp(int amount){
        Map<String, Object> map = new HashMap<>();

        map.put("amount",amount);

        db.collection("TopUpAmount").document("hello").set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(TopUp.this,"Top up Successfully",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TopUp.this,"Top up Failed",Toast.LENGTH_SHORT).show();
                Log.d(TAG,e.toString());
            }
        });

    }

    public void showAmount(View v){

        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton =findViewById(radioId);
        eta = findViewById(R.id.et_amount);

        Toast.makeText(TopUp.this, radioButton.getText(),Toast.LENGTH_SHORT).show();


        String str = radioButton.getText().toString();
        int amount = Integer.parseInt(str.replaceAll("[\\D]" , "" ));
        String a =  String.valueOf(amount);

        eta.setText("RM " + a);
    }
}

