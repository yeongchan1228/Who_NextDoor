package com.example.who_nextdoor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.who_nextdoor.BoardRecycler.ChatAdapter;
import com.example.who_nextdoor.BoardRecycler.InformationAdapter;
import com.example.who_nextdoor.ChatDataInfo;
import com.example.who_nextdoor.R;
import com.example.who_nextdoor.informationInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import android.text.TextUtils;
import android.util.Log;
import com.example.who_nextdoor.UserInfo;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<ChatDataInfo> chatList;
    private EditText EditText_chat;
    private Button Button_send;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("c_board");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String nick = "익명";
    private String id = user.getUid();
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        Button_send = findViewById(R.id.Button_send);
        EditText_chat = findViewById(R.id.EditText_chat);

        recyclerView = findViewById(R.id.my_recyclerView);
        recyclerView.setHasFixedSize(true); // 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList, ChatActivity.this, nick, id);
        recyclerView.setAdapter(adapter);

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                chatList.clear();
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    ChatDataInfo chatDataInfo = documentSnapshot.toObject(ChatDataInfo.class);
                    chatList.add(chatDataInfo);
                }
                Collections.sort(chatList);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this,"채팅 내용이 없습니다.",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public String getTime(){ // 시간 구하기
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
        long mNow;
        Date mDate;
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    public void Chat_Button(View v){
        String msg = EditText_chat.getText().toString();

        if(!TextUtils.isEmpty(msg)) {
            ChatDataInfo chat = new ChatDataInfo();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            CollectionReference collectionReference2 = firebaseFirestore.collection("users");


            collectionReference2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                        UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);
                        if(user.getEmail().equals(userInfo.getAddress())){
                            nickname = userInfo.getShcoolNumber();
                            StringBuilder builder = new StringBuilder(nickname);
                            builder.setCharAt(2,'*');
                            builder.setCharAt(3,'*');
                            builder.setCharAt(6,'*');
                            builder.setCharAt(7,'*');
                            chat.setNickname(builder.toString());
                            chat.setUid(user.getUid());
                            chat.setMsg(msg);
                            chat.setDate(getTime());
                            collectionReference.add(chat);
                            recyclerView = findViewById(R.id.my_recyclerView);
                            recyclerView.setHasFixedSize(true); // 성능 강화
                            layoutManager = new LinearLayoutManager(ChatActivity.this);
                            recyclerView.setLayoutManager(layoutManager);

                            chatList = new ArrayList<>();
                            adapter = new ChatAdapter(chatList, ChatActivity.this, nick, id);
                            recyclerView.setAdapter(adapter);

                            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    chatList.clear();
                                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                        ChatDataInfo chatDataInfo = documentSnapshot.toObject(ChatDataInfo.class);
                                        chatList.add(chatDataInfo);
                                    }
                                    Collections.sort(chatList);
                                    adapter.notifyDataSetChanged();
                                    EditText_chat.setText(null);
                                }
                            }).addOnFailureListener(new OnFailureListener() {

                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatActivity.this,"채팅 내용이 없습니다.",Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        }
    }


}