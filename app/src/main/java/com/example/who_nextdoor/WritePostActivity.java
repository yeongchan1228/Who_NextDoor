package com.example.who_nextdoor;
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

public class WritePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void uploadPost(View v) {
        final String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.contentsEditText)).getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(!(TextUtils.isEmpty(title)) && !(TextUtils.isEmpty(contents))) {
            WriteInfo writeInfo = new WriteInfo(title, contents);

            if (user != null) {
                db.collection("posts").document(user.getUid()).set(writeInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AlertDialog.Builder oh = new AlertDialog.Builder(WritePostActivity.this);
                        oh.setMessage("글 등록 성공");
                        oh.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(WritePostActivity.this, HomeActivity.class);
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
                        Toast.makeText(WritePostActivity.this, "오류 발생", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(WritePostActivity.this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
