package com.example.who_nextdoor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
public class Join2 extends AppCompatActivity {
    private EditText userId;
    private EditText userPwcheck;
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);
        userId = (EditText) findViewById(R.id.Identity);
        userPwcheck = (EditText) findViewById(R.id.PasswordCheck);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void GoStart(View v){
        Intent intent = new Intent(this, com.example.who_nextdoor.StartActivity.class);
        startActivity(intent);
        finish();
    }
    public void GoJoin1(View v){
        Intent intent = new Intent(this, Join1.class);
        startActivity(intent);
        finish();
    }
    public void Finish(View v){
        final String email = userId.getText().toString().trim(); // trim = 공백 제거
        final String password = userPwcheck.getText().toString().trim();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Join2.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Join2.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(Join2.this, "등록 에러", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

    }
}
