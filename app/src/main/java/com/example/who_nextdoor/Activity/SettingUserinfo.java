package com.example.who_nextdoor.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioButton;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.who_nextdoor.R;
import com.example.who_nextdoor.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Set;

public class SettingUserinfo extends AppCompatActivity {
    private final String Tag = "SettingUserinfo";
    private EditText Alias, number, depart;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Alias = findViewById(R.id.UserAlias);
        number = findViewById(R.id.UserPhonenumber);
        depart = findViewById(R.id.UserDepartment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (user == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
    public void profileUpdate(View v){
        String a = Alias.getText().toString();
        String n = number.getText().toString();
        String d = depart.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        if (documentSnapshot.exists()) {
                            UserInfo meminfo = documentSnapshot.toObject(UserInfo.class);
                            if(!(TextUtils.isEmpty(a))){
                                meminfo.setAlias(a);
                            }
                            if(!(TextUtils.isEmpty(d))){
                                meminfo.setDepartment(d);
                            }
                            if(!(TextUtils.isEmpty(n))){
                                meminfo.setPhoneNumber(n);
                            }

                            db.collection("users").document(user.getUid()).set(meminfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    AlertDialog.Builder oh = new AlertDialog.Builder(SettingUserinfo.this);
                                    oh.setMessage("정보 저장 성공");
                                    oh.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(SettingUserinfo.this, HomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                    );
                                    oh.setCancelable(false);
                                    oh.show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SettingUserinfo.this, "오류 발생", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingUserinfo.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}
