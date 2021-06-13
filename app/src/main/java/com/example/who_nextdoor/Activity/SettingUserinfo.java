package com.example.who_nextdoor.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Set;

public class SettingUserinfo extends AppCompatActivity {
    private final String Tag = "SettingUserinfo";
    private EditText Alias, number, depart;
    private Uri filePath;
    private ImageView stimage;
    private String filename;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Alias = findViewById(R.id.UserAlias);
        number = findViewById(R.id.UserPhonenumber);
        depart = findViewById(R.id.UserDepartment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (user == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        Button stChoose = (Button) findViewById(R.id.st_choose);
        stimage = (ImageView) findViewById(R.id.as_profile);

        stChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                stimage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void profileUpdate(View v){
        String a = Alias.getText().toString();
        String n = number.getText().toString();
        String d = depart.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        if (documentSnapshot.exists()) {
                            UserInfo meminfo = documentSnapshot.toObject(UserInfo.class);
                            if(!(TextUtils.isEmpty(a))){
                                meminfo.setAlias(a);
                            }
                            if(!(TextUtils.isEmpty(d))){
                                meminfo.setDepartment(d);
                            }
                            if(!(TextUtils.isEmpty(n))){
                                meminfo.setPhoneNumber(n);
                            }

                            db.collection("users").document(user.getUid()).set(meminfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if(filePath != null){
                                        final ProgressDialog progressDialog = new ProgressDialog(SettingUserinfo.this);
                                        progressDialog.setTitle("업로드중...");
                                        progressDialog.show();
                                        filename =  user.getEmail()+ "profile" + ".png";
                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                        StorageReference storageRef = storage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com").child("users/" + filename);
                                        storageRef.putFile(filePath)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        progressDialog.dismiss();
                                                        Intent intent = new Intent(SettingUserinfo.this, HomeActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                                        double progress = (100 * snapshot.getBytesTransferred()) /  snapshot.getTotalByteCount();
                                                //dialog에 진행률을 퍼센트로 출력해 준다
                                                progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                                            }
                                        });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SettingUserinfo.this, "오류 발생", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingUserinfo.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}
