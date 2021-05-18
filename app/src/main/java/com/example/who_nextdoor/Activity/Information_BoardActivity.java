package com.example.who_nextdoor.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.who_nextdoor.BoardRecycler.InformationAdapter;
import com.example.who_nextdoor.R;
import com.example.who_nextdoor.informationInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Collections;

public class Information_BoardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText search;
    private InformationAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList arrayList;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("i_board");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informationboard);

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
                    informationInfo informationInfo = documentSnapshot.toObject(informationInfo.class);
                    arrayList.add(informationInfo);
                }
                Collections.sort(arrayList);
                Collections.reverse(arrayList);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Information_BoardActivity.this,"게시글이 아무것도 없습니다.",Toast.LENGTH_SHORT).show();
            }
        });
            adapter = new InformationAdapter(arrayList,this);
            recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new InformationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                informationInfo getwriteinfo = adapter.getwriteinfo(pos);
                Intent intent = new Intent(Information_BoardActivity.this,PostActivity.class);
                intent.putExtra("Title", getwriteinfo.getTitle());
                intent.putExtra("Contents", getwriteinfo.getContents());
                intent.putExtra("Date", getwriteinfo.getDate());
                startActivity(intent);
            }
        });
    }
    public void writePost(View v) {
        Intent intent = new Intent(this, Information_InputActivity.class);
        startActivity(intent);
        finish();
    }
    public void Search_check(View v){
        search = findViewById(R.id.ib_search);
        String Search = search.getText().toString();
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
                        informationInfo informationInfo = documentSnapshot.toObject(informationInfo.class);
                        if(informationInfo.getTitle().contains(Search) || informationInfo.getContents().contains(Search)){
                            arrayList.add(informationInfo);
                        }
                }
                Collections.sort(arrayList);
                Collections.reverse(arrayList);
                adapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Information_BoardActivity.this,"게시글이 아무것도 없습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new InformationAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new InformationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                informationInfo getwriteinfo = adapter.getwriteinfo(pos);
                Intent intent = new Intent(Information_BoardActivity.this,PostActivity.class);
                intent.putExtra("Title", getwriteinfo.getTitle());
                intent.putExtra("Contents", getwriteinfo.getContents());
                intent.putExtra("Date", getwriteinfo.getDate());
                startActivity(intent);
            }
        });
    }
}
