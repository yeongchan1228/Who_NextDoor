package com.example.who_nextdoor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.who_nextdoor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    public void sendEmail(View view) {
        //비밀번호 재설정 이메일 보내기
        String emailAddress = email.getText().toString().trim();
        if(TextUtils.isEmpty(emailAddress)){
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("처리 중입니다. 잠시 기다려 주세요...");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(emailAddress)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(GofindpwdActivity.this, "이메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(GofindpwdActivity.this, "메일 보내기 실패!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
        }
    public void GoMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    }


