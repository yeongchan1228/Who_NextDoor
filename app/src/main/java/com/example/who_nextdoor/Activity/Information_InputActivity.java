package com.example.who_nextdoor.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.who_nextdoor.R;
import com.example.who_nextdoor.informationInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Information_InputActivity extends AppCompatActivity {
    String filename;
    //private Button ibimage;
    private FloatingActionButton photoadd;
    private ImageView ibPreview;
    private Uri filePath;
    private Toast toast;
    String a, getDate;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_board_input);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        photoadd=findViewById(R.id.photo_add);
        ibPreview = findViewById(R.id.ib_preview);
        photoadd.setOnClickListener(new View.OnClickListener() {
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
                ibPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    public void uploadPost(View v) {

        final String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.contentsEditText)).getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(this);

        if(filePath != null){
            progressDialog.setTitle("업로드중...");
            progressDialog.show();
            Upload_iboard_T(title, contents);
            filename = getDate + ".png";
            StorageReference storageRef = storage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com").child("i_board/" + filename);
            storageRef.putFile(filePath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Information_InputActivity.this, Information_BoardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    db.collection("i_board").document(getDate).delete();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    @SuppressWarnings("VisibleForTests")
                    double progress = (100 * snapshot.getBytesTransferred()) /  snapshot.getTotalByteCount();
                    //dialog에 진행률을 퍼센트로 출력해 준다
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "...");
                }
            });
        }
        else{
            String[] filter_list = {"시발", "씨발", "ㅅㅂ", "슈발", "씨바" , "ㅆㅃ", "ㅆㅂ", "병신", "ㅄ", "ㅂㅅ", "븅신", "개새끼",
                    "지랄", "ㅈㄹ", "염병", "좆", "미친놈", "미친년", "미친 놈", "미친 년", "미친새끼", "미친 새끼"};
            for(int i=0; i<filter_list.length; i++) {
                if (contents.contains(filter_list[i])) {
                    Toast.makeText(getApplicationContext(), "바르고 고운 말을 사용합시다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (title.contains(filter_list[i])) {
                    Toast.makeText(getApplicationContext(), "바르고 고운 말을 사용합시다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Upload_iboard(title, contents);
            AlertDialog.Builder oh = new AlertDialog.Builder(Information_InputActivity.this);
            oh.setMessage("글 등록 성공");
            oh.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Information_InputActivity.this, Information_BoardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
            );
            oh.setCancelable(false);
            oh.show();
        }


    }
    public void Upload_iboard_T(String title, String contents){ // 사진을 업로드 했을 때
        if(!(TextUtils.isEmpty(title)) && !(TextUtils.isEmpty(contents))) {
            informationInfo informationInfo = new informationInfo(title, contents);
            informationInfo.setUid(user.getUid());
            informationInfo.setDate(getTime());
            informationInfo.setboard_image("T");
            getDate = informationInfo.getDate();
            String date = informationInfo.getDate();
            if (user != null) {

                db.collection("i_board").document(date).set(informationInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Information_InputActivity.this, "오류 발생", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        } else {
            Toast.makeText(Information_InputActivity.this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Information_InputActivity.this, Information_InputActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void Upload_iboard(String title, String contents){
        if(!(TextUtils.isEmpty(title)) && !(TextUtils.isEmpty(contents))) {
            informationInfo informationInfo = new informationInfo(title, contents);
            informationInfo.setUid(user.getUid());
            informationInfo.setDate(getTime());
            String date = informationInfo.getDate();
            if (user != null) {
                db.collection("i_board").document(date).set(informationInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Information_InputActivity.this, "오류 발생", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        } else {
            Toast.makeText(Information_InputActivity.this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Information_InputActivity.this, Information_InputActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public String getTime(){ // 시간 구하기
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
        long mNow;
        Date mDate;
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}