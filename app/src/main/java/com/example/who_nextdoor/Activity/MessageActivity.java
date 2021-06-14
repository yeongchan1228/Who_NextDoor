package com.example.who_nextdoor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.who_nextdoor.BoardRecycler.MessageAdapter;
import com.example.who_nextdoor.MessageDataInfo;
import com.example.who_nextdoor.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private EditText search;

    private RecyclerView.LayoutManager layoutManager;
    private ArrayList arrayList;
    private ArrayList<String> rcvList;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String myuid = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView = findViewById(R.id.mes_recyclerView);
        recyclerView.setHasFixedSize(true); // 성능 강화

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        rcvList = new ArrayList<>();

        DocumentReference docRef = db.collection("m_board").document(myuid);
        CollectionReference collectionReference = firebaseFirestore.collection("m_board").document(myuid).collection("receiver");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                rcvList.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    rcvList.add(documentSnapshot.getId());
                    Collections.sort(rcvList);
                }
                arrayList.clear();
                for(int i=0; i<rcvList.size(); i++) {
                    docRef.collection("receiver").document(rcvList.get(i)).collection("chat").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots1) {
                            MessageDataInfo messageDataInfo = queryDocumentSnapshots1.getDocuments().get(0).toObject(MessageDataInfo.class);
                            arrayList.add(messageDataInfo);
                            Collections.sort(arrayList);
                            adapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MessageActivity.this,"쪽지가 아무것도 없어요 :(",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                adapter = new MessageAdapter(arrayList,MessageActivity.this);

                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        MessageDataInfo getmessageinfo = adapter.getmsginfo(pos);;
                        Intent intent = new Intent(MessageActivity.this, Personal_MessageActivity.class);
                        intent.putExtra("RECEIVER_UID", getmessageinfo.getRcvuid());
                        startActivity(intent);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MessageActivity.this,"쪽지가 아무것도 없어요 :(",Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void goback(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}