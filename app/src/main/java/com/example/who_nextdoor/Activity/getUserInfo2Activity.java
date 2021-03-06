package com.example.who_nextdoor.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class getUserInfo2Activity extends AppCompatActivity {
    private long backKeyPressedTime = 0;
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

        btUpload = (Button) findViewById(R.id.bt_upload);
        ivPreview = (ImageView) findViewById(R.id.iv_preview);
        btChoose = (Button) findViewById(R.id.bt_choose);

        btChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "???????????? ???????????????."), 0);
            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // ?????? ?????? ?????? ????????? ????????? ?????? ?????? ?????? ?????? ?????? ??????
        // ??????????????? ?????? ?????? ????????? ????????? ????????? 2.5?????? ?????? ?????? ????????? ?????? ???
        // ??????????????? ?????? ?????? ????????? ????????? ????????? 2.5?????? ???????????? Toast ??????
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "?????? ?????? ????????? ??? ??? ??? ???????????? ???????????????.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // ??????????????? ?????? ?????? ????????? ????????? ????????? 2.5?????? ?????? ?????? ????????? ?????? ???
        // ??????????????? ?????? ?????? ????????? ????????? ????????? 2.5?????? ????????? ???????????? ??????
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"????????? ????????? ???????????????.",Toast.LENGTH_LONG);
            toast.show();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            try {
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
            progressDialog.setTitle("????????????...");
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
                                UserInfo meminfo = documentSnapshot.toObject(UserInfo.class); // ?????? ???????????? class??? ??????
                                filename =  user.getUid()+"/"+ user.getEmail() + ".png";
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
                                                Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        //?????????
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        //?????????
                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                @SuppressWarnings("VisibleForTests") //?????? ?????? ?????? ???????????? ????????? ????????????. ??? ??????????
                                                        double progress = (100 * snapshot.getBytesTransferred()) /  snapshot.getTotalByteCount();
                                                //dialog??? ???????????? ???????????? ????????? ??????
                                                progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(getUserInfo2Activity.this,"??????",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
        }
    }


}