package com.example.who_nextdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 화면 회전 막기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.collection("users").document(user.getUid());
            //FirebaseAuth auth = FirebaseAuth.getInstance();
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot != null){
                            if(documentSnapshot.exists()){
                                UserInfo userinfo = documentSnapshot.toObject(UserInfo.class); // 정보 받아와서 class에 저장
                                if(userinfo.getAccess().equals("F")){
                                    Intent intent = new Intent(HomeActivity.this, getUserInfo2Activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(userinfo.getAccess().equals("W")){
                                    Intent intent = new Intent(HomeActivity.this, NoAccessWaitActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else{
                                Intent intent = new Intent(HomeActivity.this, getUserInfoActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }
            });
        }
      
    }
    public void Withdraw(View v){ // 회원 탈퇴 클릭 시
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(HomeActivity.this);
        alert_confirm.setMessage("정말로 계정을 삭제할까요?");
        alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(HomeActivity.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            }
        });
        alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert_confirm.show();

    }

    public void goProfile(View v){
        Intent intent = new Intent(this, getUserInfoActivity.class);
        startActivity(intent);
        finish();
    }
    public void Logout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, getUserInfoActivity.class);
        startActivity(intent);
        finish();
    }
}