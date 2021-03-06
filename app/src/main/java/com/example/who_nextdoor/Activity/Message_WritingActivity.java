package com.example.who_nextdoor.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.who_nextdoor.BoardRecycler.InformationAdapter;
import com.example.who_nextdoor.MessageDataInfo2;
import com.example.who_nextdoor.R;
import com.example.who_nextdoor.UidDataInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Message_WritingActivity extends AppCompatActivity {
    String filename;
    String p_title;
    String path;
    String receiveruid;
    String now_date;
    String myuid;


    private FloatingActionButton photoadd;
    private ImageView mesPreview;
    private Uri filePath;
    private Toast toast;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userid = user.getUid();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("i_board");
    private ArrayList arrayList;
    private InformationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        arrayList = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_message); //here view sul-jung

        if(user == null){ // if not jung-bo -> go main dszzzzzzzzzzzzzzzzzzz what are you doing i See your code
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        photoadd=findViewById(R.id.photo_add); // this is photo add button
        mesPreview = findViewById(R.id.mes_preview); //this  is photo preview okay!

        photoadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "???????????? ???????????????."), 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // this is... maybe? photo see function
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mesPreview.setImageBitmap(bitmap); // photo preview function (maybe) dzdz...
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void sendMessage(View v) { // this is main function. send Message. I think this code is so bad..... T-T
        final ProgressDialog progressDialog = new ProgressDialog(this);
        final String contents = ((EditText) findViewById(R.id.mes_content2)).getText().toString(); //this is message content!

        if(filePath != null){
            progressDialog.setTitle("????????????...");
            progressDialog.show();
            now_date = getTime();
            filename =  now_date + ".png";

            StorageReference storageRef = storage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com").child("m_board/" + userid + "/" + filename);
            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Upload_mboard_T(contents);
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                            //Intent intent = new Intent(Message_WritingActivity.this, Personal_MessageActivity.class);
                            //startActivity(intent);
                            //finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100 * snapshot.getBytesTransferred()) /  snapshot.getTotalByteCount();
                    //dialog??? ???????????? ???????????? ????????? ??????
                    progressDialog.setMessage("Uploaded " + /*((int) progress) +* "%*/"...");
                }
            });
        }
        else{
            Upload_mboard(contents);
        }

    }


    public void Upload_mboard_T(String contents){ // ????????? ????????? ?????? ???
        if(!(TextUtils.isEmpty(contents))) {
            MessageDataInfo2 messageDataInfo = new MessageDataInfo2(contents);
            messageDataInfo.setUid(user.getUid());
            messageDataInfo.setDate(now_date);
            messageDataInfo.setboard_image("T");
            UidDataInfo uidDataInfo = new UidDataInfo(receiveruid);
            if (user != null) {
                Intent intent = getIntent();

                if(receiveruid == null) {
                    receiveruid = intent.getStringExtra("Uid");
                    arrayList.add(receiveruid);
                    //Intent intent2 = new Intent(this, MessageActivity.class);
                    //intent2.putExtra("Uid_list", arrayList);
                    //startActivity(intent2);
                    uidDataInfo.setUid(receiveruid);
                    messageDataInfo.setRcvuid(receiveruid);
                    now_date = getTime();
                    myuid = user.getUid();
                }else{messageDataInfo.setRcvuid(receiveruid);}

                db.collection("m_board").document(myuid).collection("receiver").document(receiveruid).collection("chat").document(now_date).set(messageDataInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("m_board").document(myuid).collection("receiver").document(receiveruid).set(uidDataInfo);
                        Intent intent2 = new Intent(Message_WritingActivity.this, Personal_MessageActivity.class);
                        intent2.putExtra("NOW_DATE", now_date);
                        intent2.putExtra("RECEIVER_UID", receiveruid);
                        intent2.putExtra("USER_ID", userid);
                        startActivity(intent2);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Message_WritingActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


            }
        } else {
            Toast.makeText(Message_WritingActivity.this, "????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
        }
    }

    public void Upload_mboard(String contents){
        if(!(TextUtils.isEmpty(contents))) {
            MessageDataInfo2 messageDataInfo = new MessageDataInfo2(contents);
            messageDataInfo.setUid(user.getUid());
            messageDataInfo.setDate(getTime());
            UidDataInfo uidDataInfo = new UidDataInfo(receiveruid);
            if (user != null) {
                Intent intent = getIntent();

                if(receiveruid == null) {
                    receiveruid = intent.getStringExtra("Uid");
                    arrayList.add(receiveruid);
                    //Intent intent2 = new Intent(this, MessageActivity.class);
                    //intent2.putExtra("Uid_list", arrayList);
                    //startActivity(intent2);
                    uidDataInfo.setUid(receiveruid);
                    messageDataInfo.setRcvuid(receiveruid);
                    now_date = getTime();
                    myuid = user.getUid();
                }else{messageDataInfo.setRcvuid(receiveruid);}
                db.collection("m_board").document(myuid).collection("receiver").document(receiveruid).collection("chat").document(now_date).set(messageDataInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("m_board").document(myuid).collection("receiver").document(receiveruid).set(uidDataInfo);
                        Intent intent2 = new Intent(Message_WritingActivity.this, Personal_MessageActivity.class);
                        intent2.putExtra("NOW_DATE", now_date);
                        intent2.putExtra("RECEIVER_UID", receiveruid);
                        intent2.putExtra("USER_ID", userid);
                        startActivity(intent2);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Message_WritingActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        } else {
            Toast.makeText(Message_WritingActivity.this, "????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
        }
    }
    public String getTime(){ // ?????? ?????????
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
        long mNow;
        Date mDate;
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    /*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Information_BoardActivity.class);
        startActivity(intent);
    }
    */
}