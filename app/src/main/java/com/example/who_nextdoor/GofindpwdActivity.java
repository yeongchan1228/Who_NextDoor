package com.example.who_nextdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

        requestEmail.setOnClickListener(this);

    }

    public void onClick() {
        progressDialog.setMessage("처리 중입니다. 잠시 기다려 주세요...");
        progressDialog.show();

        //비밀번호 재설정 이메일 보내기
        String emailAddress = email.getText().toString().trim();
        firebaseAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new onCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(FindActivity.this, "이메일을 보냈습니다.", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            } else {
                                Toast.makeText(FindActivity.this, "메일 보내기 실패!", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
    }
}

