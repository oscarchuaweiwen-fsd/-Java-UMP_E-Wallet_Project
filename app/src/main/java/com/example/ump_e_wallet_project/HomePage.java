package com.example.ump_e_wallet_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class HomePage extends AppCompatActivity {
    private ImageView myImage;
    private TextView username,balance;
    private Button topup,signout,transfer,mycard,payment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference mStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mStorage = FirebaseStorage.getInstance().getReference();
        username = findViewById(R.id.tv_username);
        balance = findViewById(R.id.tv_balance);
        topup = findViewById(R.id.btn_topup);
        signout = findViewById(R.id.btn_signout);
        transfer = findViewById(R.id.btn_transfer);
        mycard = findViewById(R.id.btn_mycard);
        payment = findViewById(R.id.btn_pay);
        myImage = findViewById(R.id.iv_imageview);
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        DocumentReference userinfo = db.collection("user").document(
                uid);

        userinfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();


                username.setText(document.getData().get("name").toString());
                balance.setText("RM " + document.getData().get("balance").toString());
            }
        });

        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topuppage = new Intent(HomePage.this,TopUp.class);
                startActivity(topuppage);
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transferpage = new Intent(HomePage.this,Transfer.class);
                startActivity(transferpage);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(HomePage.this, "Sign out Successfully!", Toast.LENGTH_SHORT).show();
                Intent gotomainpage = new Intent(HomePage.this,MainActivity.class);
                startActivity(gotomainpage);


            }
        });

        mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mycardpage = new Intent(HomePage.this,MyCard.class);
                startActivity(mycardpage);
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentpage = new Intent(HomePage.this,Pay.class);
                startActivity(paymentpage);
            }
        });

        StorageReference downloadProfile = mStorage.child("JPEG_" + uid + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        downloadProfile.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            private static final String TAG = "HomePage";

            @Override
            public void onSuccess(byte[] bytes) {
                Log.d(TAG,"Helloooo:"  + String.valueOf(bytes));
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                myImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    public void uploadImage(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        String imageFileName = "JPEG_" + uid + ".jpg";
        byte bb[] = bytes.toByteArray();


        uploadToFirebase(bb,imageFileName);
    }

    private void uploadToFirebase(byte[] bb,String name) {
        StorageReference sr = mStorage.child(name);

        sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(HomePage.this, "Successfully Upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomePage.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
            }
        });




    }
}