package com.example.who_nextdoor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.who_nextdoor.Activity.MainActivity;
import com.example.who_nextdoor.BoardRecycler.InformationAdapter;
import com.example.who_nextdoor.BoardRecycler.PostAdapter;
import com.example.who_nextdoor.BoardRecycler.SubcomentAdapter;
import com.example.who_nextdoor.ComentsInfo;
import com.example.who_nextdoor.R;
import com.example.who_nextdoor.informationInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class PostActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList arrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    ImageView imageView;
    TextView textTitle, textContents, textdate;
    String Title, Contents, date, uid;

    EditText comentsInput;
    String getTitle, getsubText, getDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();
        Title = intent.getStringExtra("Title");
        Contents = intent.getStringExtra("Contents");
        date = intent.getStringExtra("Date");
        uid = intent.getStringExtra("Uid");

        getTitle = Title;
        getDate = date;
        comentsInput = findViewById(R.id.post_comentsinput);
        recyclerView = findViewById(R.id.postrecycle_coments);
        recyclerView.setHasFixedSize(true); // 성능 강화
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();


        imageView = findViewById(R.id.post_imageview);
        textTitle = findViewById(R.id.post_title);
        textContents = findViewById(R.id.post_contents);
        textdate = findViewById(R.id.post_date);
        textTitle.setText(Title);
        textContents.setText(Contents);
        textdate.setText(date);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com");
        StorageReference pathReference = storageReference.child("i_board");

        CollectionReference collectionReference = firebaseFirestore.collection("i_board").document(getDate).collection("coments");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                arrayList.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    ComentsInfo comentsInfo = documentSnapshot.toObject(ComentsInfo.class);
                    arrayList.add(comentsInfo);
                    Collections.sort(arrayList);
                    //Collections.reverse(arrayList);
                    adapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostActivity.this,"게시글이 아무것도 없습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new PostAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);

        if(pathReference != null){
            StorageReference submitimage = storageReference.child("i_board/"+getDate+".png");
            submitimage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Glide.with(PostActivity.this).load(task.getResult()).into(imageView);
                    }
                }
            });
        }
    }

    public void postcoments_inputkey(View v){
        String coments_input = comentsInput.getText().toString();
        if(!(TextUtils.isEmpty(coments_input))) {
            ComentsInfo comentsInfo = new ComentsInfo();
            comentsInfo.setComents(coments_input);
            comentsInfo.setDate(getTime());
            comentsInfo.setIamsub("F");
            comentsInfo.setParentTitle(getTitle);
            comentsInfo.setParentDate(getDate);
            comentsInfo.setUid_date(user.getUid()+getTime());
            if (user != null) {
                db.collection("i_board").document(getDate).collection("coments")
                        .document(user.getUid()+comentsInfo.getDate()).set(comentsInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        comentsInput.setText(null);
                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                        CollectionReference collectionReference = firebaseFirestore.collection("i_board").document(getDate).collection("coments");
                        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                arrayList.clear();
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                    ComentsInfo comentsInfo = documentSnapshot.toObject(ComentsInfo.class);
                                    arrayList.add(comentsInfo);
                                    Collections.sort(arrayList);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostActivity.this,"게시글이 아무것도 없습니다.",Toast.LENGTH_SHORT).show();
                            }
                        });

                        adapter = new PostAdapter(arrayList,PostActivity.this);
                        recyclerView.setAdapter(adapter);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostActivity.this, "오류 발생", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        } else {
            Toast.makeText(PostActivity.this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
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

    public void openMessage(View v){
        Intent intent = new Intent(PostActivity.this, Message_WritingActivity.class);
        intent.putExtra("Date", date);
        intent.putExtra("Uid", uid);
        startActivity(intent);
    }


}