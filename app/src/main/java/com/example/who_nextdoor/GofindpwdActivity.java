package com.example.who_nextdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class GofindpwdActivity extends AppCompatActivity {
    private EditText email;
    private Button requestEmail;
    private ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gofindpwd);
        email = (EditText) findViewById(R.id.email);
        requestEmail = (Button) findViewById(R.id.requestEmail);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void onClick() {
        progressDialog.setMessage("처리 중입니다. 잠시 기다려 주세요...");
        progressDialog.show();

        //비밀번호 재설정 이메일 보내기
        String emailAddress = email.getText().toString().trim();
        firebaseAuth.sendPasswordResetEmail(emailAddress)
        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(GofindpwdActivity.this, "이메일을 보냈습니다.", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(GofindpwdActivity.this, "메일 보내기 실패!", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
        }
    }


