package com.example.who_nextdoor.Activity;

import android.app.Person;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.who_nextdoor.BoardRecycler.InformationAdapter;
import com.example.who_nextdoor.BoardRecycler.MessageAdapter;
import com.example.who_nextdoor.BoardRecycler.MessageAdapter2;
import com.example.who_nextdoor.MessageDataInfo;
import com.example.who_nextdoor.MessageDataInfo2;
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

import java.util.ArrayList;
import java.util.Collections;


public class Personal_MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter2 adapter;
    private MessageAdapter2 adapter2;

    private RecyclerView.LayoutManager layoutManager;
    private ArrayList arrayList;
    private ArrayList arrayList2;
    private ArrayList arrayList3;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String myuid;
    String receiveruid;
    String now_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Toast.makeText(Personal_MessageActivity.this, "엥여기는??", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_message);

        recyclerView = findViewById(R.id.per_mes_recyclerView);
        recyclerView.setHasFixedSize(true); // 성능 강화

        //Toast.makeText(Personal_MessageActivity.this, "여긴??", Toast.LENGTH_SHORT).show();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();
        arrayList2 = new ArrayList<>();
        arrayList3 = new ArrayList<>();

        //Toast.makeText(Personal_MessageActivity.this, "여기는??", Toast.LENGTH_SHORT).show();

        Intent intent = getIntent();
        receiveruid =intent.getStringExtra("RECEIVER_UID");
        now_date = intent.getStringExtra("NOW_DATE");
        myuid = user.getUid();

        //Toast.makeText(Personal_MessageActivity.this, receiveruid + now_date, Toast.LENGTH_SHORT).show();

        db.collection("m_board").document(receiveruid).collection(myuid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //Toast.makeText(Personal_MessageActivity.this, "3333333333", Toast.LENGTH_SHORT).show(); // 3
                //arrayList2.clear();
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    MessageDataInfo2 messageDataInfo2 = documentSnapshot.toObject(MessageDataInfo2.class);
                    arrayList.add(messageDataInfo2);
                }

                Collections.sort(arrayList);
                Collections.reverse(arrayList);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Personal_MessageActivity.this,"쪽지가 아무것도 없어요... :(",Toast.LENGTH_SHORT).show();
            }
        });


        db.collection("m_board").document(myuid).collection(receiveruid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //Toast.makeText(Personal_MessageActivity.this, "1111111", Toast.LENGTH_SHORT).show(); // 4
                //arrayList.clear();
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    MessageDataInfo2 messageDataInfo = documentSnapshot.toObject(MessageDataInfo2.class);
                    arrayList.add(messageDataInfo);
                }

                Collections.sort(arrayList);
                Collections.reverse(arrayList);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Personal_MessageActivity.this,"쪽지가 아무것도 없어요... :(",Toast.LENGTH_SHORT).show();
            }
        });

        //Toast.makeText(Personal_MessageActivity.this, "22222", Toast.LENGTH_SHORT).show(); // 얘가 제일 먼저임


        adapter = new MessageAdapter2(arrayList,this);
        //Toast.makeText(Personal_MessageActivity.this, "4444444444", Toast.LENGTH_SHORT).show(); // 그리고 얘
        recyclerView.setAdapter(adapter);

        //adapter = new MessageAdapter2(arrayList2,this);
        //recyclerView.setAdapter(adapter);
    }

    public void openMessage2(View v) {
        Intent intent = new Intent(Personal_MessageActivity.this, Message_WritingActivity.class);
        intent.putExtra("Uid", receiveruid);
        startActivity(intent);
        //finish();
    }

    public void goback(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Information_BoardActivity.class);
        startActivity(intent);
        //super.onBackPressed();
    }
    */
}