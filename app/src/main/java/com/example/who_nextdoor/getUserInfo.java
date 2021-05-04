package com.example.who_nextdoor;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class getUserInfo extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getuser);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getUserInfo.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void profileUpdate(View v) {
            final String name = ((EditText) findViewById(R.id.UserName)).getText().toString();
            final String phoneNumber = ((EditText) findViewById(R.id.UserPhonenumber)).getText().toString();
            final String birthDay = ((EditText) findViewById(R.id.Userbirth)).getText().toString();
            final String alias = ((EditText) findViewById(R.id.UserAlias)).getText().toString();
            final String schoolnumber = ((EditText) findViewById(R.id.Usershcoolnumber)).getText().toString();
            final String gender = ((EditText) findViewById(R.id.Usergneder)).getText().toString();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if(!(TextUtils.isEmpty(name)) && !(TextUtils.isEmpty(phoneNumber)) && !(TextUtils.isEmpty(birthDay))
                    && !(TextUtils.isEmpty(alias)) && !(TextUtils.isEmpty(schoolnumber)) && !(TextUtils.isEmpty(gender))) {
                UserInfo userInfo = new UserInfo(name, phoneNumber, birthDay, alias, schoolnumber, gender, user.getEmail());
                if (user != null) {
                    db.collection("users").document(user.getUid()).set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            AlertDialog.Builder oh = new AlertDialog.Builder(getUserInfo.this);
                            oh.setMessage("정보 입력 성공");
                            oh.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getUserInfo.this, HomeActivity.class);
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
                            Toast.makeText(getUserInfo.this, "오류 발생", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(getUserInfo.this, "회원 정보를 다 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
}
