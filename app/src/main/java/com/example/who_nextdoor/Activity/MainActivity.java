package com.example.who_nextdoor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.who_nextdoor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity{
    private EditText userId;
    private EditText userPwcheck;
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        userId = (EditText) findViewById(R.id.Email);
        userPwcheck = (EditText) findViewById(R.id.Pwd);
        Button button1 = (Button) findViewById(R.id.button);
        userPwcheck.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER){
                    button1.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }
    public void GoHome(View v){
        String email = userId.getText().toString().trim();
        String password = userPwcheck.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email??? ????????? ?????????.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password??? ????????? ?????????.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else { // ????????? ?????? ?????? ??? ?????? ?????? ?????? ?????? ??????
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete();
                                Toast.makeText(getApplicationContext(), "????????? ?????? ?????? ?????? ??????????????? ????????????.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    public void GoFindPwd(View v){
        Intent intent = new Intent(this, GofindpwdActivity.class);
        startActivity(intent);
        finish();
    }

    public void GoJoin2(View v){
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
        finish();
    }

}