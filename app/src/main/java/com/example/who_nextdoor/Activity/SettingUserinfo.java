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

public class SettingUserinfo extends AppCompatActivity {
    private final String Tag = "SettingUserinfo";
    private EditText Alias, number, depart;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Alias = findViewById(R.id.UserAlias);
        number = findViewById(R.id.UserPhonenumber);
        depart = findViewById(R.id.UserPhonenumber);
        String a = Alias.getText().toString();
        String n = number.getText().toString();
        String d = depart.getText().toString();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(user == null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingUserinfo.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}
