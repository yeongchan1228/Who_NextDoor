package com.example.who_nextdoor;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class getUserInfo2Activity extends AppCompatActivity {
    private long backKeyPressedTime = 0;
    private static final String TAG = "MainActivity";
    String filename;
    private Button btChoose;
    private Button btUpload;
    private ImageView ivPreview;
    private Uri filePath;
    private Toast toast;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getuserinfo2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        btChoose = (Button) findViewById(R.id.bt_choose);
        btUpload = (Button) findViewById(R.id.bt_upload);
        ivPreview = (ImageView) findViewById(R.id.iv_preview);

        //버튼 클릭 이벤트
        btChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //업로드
                uploadFile();
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
            toast.show();
        }
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    //upload the file
    private void uploadFile() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.collection("users").document(user.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot != null){
                            if(documentSnapshot.exists()){
                                UserInfo meminfo = documentSnapshot.toObject(UserInfo.class); // 정보 받아와서 class에 저장
                                filename =  user.getUid()+"/"+meminfo.getShcoolNumber() + ".png";
                                StorageReference storageRef = storage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com").child("images/" + filename);

                                storageRef.putFile(filePath)

                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                progressDialog.dismiss();
                                                UserInfo userinfo = new UserInfo();
                                                userinfo.setAccess("W");
                                                userinfo.setName(meminfo.getName());
                                                userinfo.setPhoneNumber(meminfo.getPhoneNumber());
                                                userinfo.setBirthDay(meminfo.getBirthDay());
                                                userinfo.setAlias(meminfo.getAlias());
                                                userinfo.setShcoolNumber(meminfo.getShcoolNumber());
                                                userinfo.setGender(meminfo.getGender());
                                                userinfo.setAddress(user.getEmail());
                                                userinfo.setFirst("T");
                                                firebaseFirestore.collection("users").document(user.getUid()).set(userinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Intent intent = new Intent(getUserInfo2Activity.this, NoAccessWaitActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                                Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        //실패시
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        //진행중
                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                                        double progress = (100 * snapshot.getBytesTransferred()) /  snapshot.getTotalByteCount();
                                                //dialog에 진행률을 퍼센트로 출력해 준다
                                                progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(getUserInfo2Activity.this,"없음",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }


}
