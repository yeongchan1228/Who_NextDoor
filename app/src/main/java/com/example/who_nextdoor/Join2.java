package com.example.who_nextdoor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    private EditText userPwcheck, userPw;
    String sld, sPw, sPw_chk;
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);

        userId = (EditText) findViewById(R.id.Identity);
        userPw = (EditText) findViewById(R.id.Password);
        userPwcheck = (EditText) findViewById(R.id.PasswordCheck);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void GoStart(View v){
        Intent intent = new Intent(this, com.example.who_nextdoor.StartActivity.class);
        startActivity(intent);
        finish();
    }
    public void GoMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void Finish(View v){
        final String email = userId.getText().toString().trim(); // trim = 공백 제거
        final String password = userPwcheck.getText().toString().trim();
        sPw = userPw.getText().toString();
        sPw_chk = userPwcheck.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(sPw.equals(sPw_chk)){
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                AlertDialog.Builder oh=new AlertDialog.Builder(Join2.this);
                                oh.setTitle("회원가입 성공");
                                oh.setMessage("성공");
                                oh.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Join2.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                oh.setCancelable(false);
                                oh.show();
                            } else {
                                Toast.makeText(Join2.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
        }

    }
}
