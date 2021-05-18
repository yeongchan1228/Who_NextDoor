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


        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = EditText_chat.getText().toString();

                if(msg != null) {
                    ChatDataInfo chat = new ChatDataInfo();
                    Intent intent = new Intent(ChatActivity.this, ChatActivity.class);

                    chat.setUid(user.getUid());
                    chat.setNickname(nick);
                    chat.setMsg(msg);
                    chat.setDate(getTime());
                    collectionReference.add(chat);
                    startActivity(intent);
                    finish();
                }
            }
        });

        recyclerView = findViewById(R.id.my_recyclerView);
        recyclerView.setHasFixedSize(true); // 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList, ChatActivity.this, nick);
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

}