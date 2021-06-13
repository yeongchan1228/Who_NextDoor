package com.example.who_nextdoor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.who_nextdoor.R;
import com.example.who_nextdoor.TradeInfo;
import com.example.who_nextdoor.BoardRecycler.TradeAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Trade_BoardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TradeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<TradeInfo> arrayList;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("t_board");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradeboard);

        recyclerView = findViewById(R.id.ib_recyclerView);
        recyclerView.setHasFixedSize(true); // 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                arrayList.clear();
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    TradeInfo tradeInfo = documentSnapshot.toObject(TradeInfo.class);
                    arrayList.add(tradeInfo);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Trade_BoardActivity.this,"게시글이 아무것도 없습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new TradeAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TradeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                TradeInfo tradeInfo = adapter.gettradeinfo(pos);
                Intent intent = new Intent(Trade_BoardActivity.this,PostActivity.class);
                intent.putExtra("Title", tradeInfo.getTitle());
                intent.putExtra("Contents", tradeInfo.getContents());
                startActivity(intent);
            }
        });
    }
    public void writePost(View v) {
        Intent intent = new Intent(this, Information_InputActivity.class);
        startActivity(intent);
        finish();
    }
}