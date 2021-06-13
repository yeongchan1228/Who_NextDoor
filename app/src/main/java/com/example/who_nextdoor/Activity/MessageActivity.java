package com.example.who_nextdoor.Activity;

import android.content.Context;
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
import com.example.who_nextdoor.HomeRecycler.Data;
import com.example.who_nextdoor.HomeRecycler.HomeRecyclerAdapter;

import com.example.who_nextdoor.MessageDataInfo;
import com.example.who_nextdoor.MessageDataInfo2;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText search;
    private MessageAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList arrayList;

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

        DocumentReference docRef = db.collection("m_board").document(myuid);
        Intent intent = getIntent();
        ArrayList<String> UidList = (ArrayList<String>) intent.getSerializableExtra("Uid_list");

        for(int i=0; i<UidList.size(); i++) {
            /*
            docRef.collection(UidList.get(i)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    arrayList.clear();
                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                        MessageDataInfo messageDataInfo = documentSnapshot.toObject(MessageDataInfo.class);
                        arrayList.add(messageDataInfo);
                    }
                    Collections.sort(arrayList);
                    Collections.reverse(arrayList);
                    adapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MessageActivity.this,"쪽지가 아무것도 없어요 :(",Toast.LENGTH_SHORT).show();
                }
            });
            */
        }


        adapter = new MessageAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                MessageDataInfo getmessageinfo = adapter.getmsginfo(pos);;
                Intent intent = new Intent(MessageActivity.this, Personal_MessageActivity.class);
                intent.putExtra("Image", getmessageinfo.getboard_image());
                intent.putExtra("Contents", getmessageinfo.getContents());
                intent.putExtra("Date", getmessageinfo.getDate());
                intent.putExtra("Uid", getmessageinfo.getUid());
                startActivity(intent);
            }
        });
    }


    /*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //super.onBackPressed();
    }
    */
}