package com.example.who_nextdoor.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.who_nextdoor.R;
import com.example.who_nextdoor.TradeInfo;
import com.example.who_nextdoor.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class Trade_InputActivity extends AppCompatActivity {
    String filename;
    private FloatingActionButton photoadd;
    private ImageView ibPreview;
    private Uri filePath;
    private Toast toast;
    private DocumentReference firebaseFirestore;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_board_input);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        photoadd = findViewById(R.id.Tphoto_add);
        ibPreview = findViewById(R.id.tb_preview);
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
        final ProgressDialog progressDialog = new ProgressDialog(this);
        final String title = ((EditText) findViewById(R.id.TtitleEditText)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.TcontentsEditText)).getText().toString();
        final String price = ((EditText) findViewById(R.id.TpriceEditText)).getText().toString();

        if(filePath != null){
            progressDialog.setTitle("????????????...");
            progressDialog.show();
            filename =  title + ".png";
            StorageReference storageRef = storage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com").child("t_board/" + filename);
            storageRef.putFile(filePath)



                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Upload_tboard_T(title, contents, price);

                            DocumentReference documentReference2 = db.collection("t_board").document(title)
                                    .collection("checkTemperature").document(user.getUid());
                            CheckInfo checkInfo = new CheckInfo();
                            checkInfo.setCheck("F");
                            checkInfo.setFirst(0);
                            documentReference2.set(checkInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });



                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(com.example.who_nextdoor.Activity.Trade_InputActivity.this, Trade_BoardActivity.class);
                            startActivity(intent);
                            finish();
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
                    @SuppressWarnings("VisibleForTests")
                    double progress = (100 * snapshot.getBytesTransferred()) /  snapshot.getTotalByteCount();
                    //dialog??? ???????????? ???????????? ????????? ??????
                    progressDialog.setMessage("Uploaded " + /*((int) progress) +* "%*/"...");
                }
            });
        }
        else{
            String[] filter_list = {"??????", "??????", "??????", "??????", "??????" , "??????", "??????", "??????", "???", "??????", "??????", "?????????",
                    "??????", "??????", "??????", "???", "?????????", "?????????", "?????? ???", "?????? ???", "????????????", "?????? ??????"};
            for(int i=0; i<filter_list.length; i++) {
                if (contents.contains(filter_list[i])) {
                    Toast.makeText(getApplicationContext(), "????????? ?????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (title.contains(filter_list[i])) {
                    Toast.makeText(getApplicationContext(), "????????? ?????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Upload_tboard(title, contents, price);

            DocumentReference documentReference2 = db.collection("t_board").document(title)
                    .collection("checkTemperature").document(user.getUid());
            CheckInfo checkInfo = new CheckInfo();
            checkInfo.setCheck("F");
            checkInfo.setFirst(0);
            documentReference2.set(checkInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            });


            AlertDialog.Builder oh = new AlertDialog.Builder(com.example.who_nextdoor.Activity.Trade_InputActivity.this);

            oh.setMessage("??? ?????? ??????");
            oh.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(com.example.who_nextdoor.Activity.Trade_InputActivity.this, Trade_BoardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
            );
            oh.setCancelable(false);
            oh.show();
        }


    }

    public void Upload_tboard_T(String title, String contents, String price){ // ????????? ????????? ?????? ???


        if(!(TextUtils.isEmpty(title)) && !(TextUtils.isEmpty(contents))) {
            TradeInfo tradeInfo = new TradeInfo(title, contents, price);
            tradeInfo.setUid(user.getUid());
            tradeInfo.setDate(getTime());
            tradeInfo.setboard_image("T");
            String email = user.getEmail();
            tradeInfo.setEmail(email);


            DocumentReference documentReference2 = db.collection("users").document(user.getUid());
            documentReference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    UserInfo userinfo = documentSnapshot.toObject(UserInfo.class);
                    tradeInfo.setAlias(userinfo.getAlias());
                    if (user != null) {
                        db.collection("t_board").document(title).set(tradeInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(com.example.who_nextdoor.Activity.Trade_InputActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                }
            });
        } else {
            Toast.makeText(com.example.who_nextdoor.Activity.Trade_InputActivity.this, "????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
        }
    }

    public void Upload_tboard(String title, String contents, String price){


        if(!(TextUtils.isEmpty(title)) && !(TextUtils.isEmpty(contents))) {
            TradeInfo tradeInfo = new TradeInfo(title, contents, price);
            tradeInfo.setDate(getTime());
            tradeInfo.setUid(user.getUid());
            String email = user.getEmail();
            tradeInfo.setEmail(email);

            DocumentReference documentReference2 = db.collection("users").document(user.getUid());
            documentReference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    UserInfo userinfo = documentSnapshot.toObject(UserInfo.class);
                    tradeInfo.setAlias(userinfo.getAlias());
                    if (user != null) {
                        db.collection("t_board").document(title).set(tradeInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(com.example.who_nextdoor.Activity.Trade_InputActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                }
            });


        } else {
            Toast.makeText(com.example.who_nextdoor.Activity.Trade_InputActivity.this, "????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
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
}

