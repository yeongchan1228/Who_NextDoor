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

import com.example.who_nextdoor.R;
import com.example.who_nextdoor.BoardRecycler.TradeAdapter;
import com.example.who_nextdoor.TradeInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class Trade_BoardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText search;
    private TradeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList arrayList;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("t_board");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradeboard);

        recyclerView = findViewById(R.id.tb_recyclerView);
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
                Collections.sort(arrayList);
                Collections.reverse(arrayList);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(com.example.who_nextdoor.Activity.Trade_BoardActivity.this,"게시글이 아무것도 없습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new TradeAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TradeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                TradeInfo tradeInfo = adapter.gettradeinfo(pos);
                Intent intent = new Intent(com.example.who_nextdoor.Activity.Trade_BoardActivity.this, tPostActivity.class);
                intent.putExtra("Title", tradeInfo.getTitle());
                intent.putExtra("Contents", tradeInfo.getContents());
                intent.putExtra("Uid", tradeInfo.getAlias());
                intent.putExtra("Date", tradeInfo.getDate());
                intent.putExtra("Profile", tradeInfo.getInputuserEmail());
                startActivity(intent);
            }
        });
    }


    public void writePost(View v) {
        Intent intent = new Intent(this, Trade_InputActivity.class);
        startActivity(intent);
        finish();
    }




    public void Search_check(View v){
        search = findViewById(R.id.tb_search);
        String Search = search.getText().toString();
        recyclerView = findViewById(R.id.tb_recyclerView);
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
                    if(tradeInfo.getTitle().contains(Search) || tradeInfo.getContents().contains(Search)){
                        arrayList.add(tradeInfo);
                    }
                }
                Collections.sort(arrayList);
                Collections.reverse(arrayList);
                adapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(com.example.who_nextdoor.Activity.Trade_BoardActivity.this,"게시글이 아무것도 없습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new TradeAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TradeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                TradeInfo tradeInfo = adapter.gettradeinfo(pos);
                Intent intent = new Intent(com.example.who_nextdoor.Activity.Trade_BoardActivity.this, PostActivity.class);
                intent.putExtra("Title", tradeInfo.getTitle());
                intent.putExtra("Contents", tradeInfo.getContents());
                intent.putExtra("Uid", tradeInfo.getAlias());
                intent.putExtra("Date", tradeInfo.getDate());
                //intent.putExtra("Uimage", tradeInfo.getUid());//사용자 이미지 넣어야함
                startActivity(intent);
            }
        });
    }
}
