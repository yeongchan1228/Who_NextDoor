package com.example.who_nextdoor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import androidx.core.view.GravityCompat;


import com.example.who_nextdoor.HomeRecycler.Data;
import com.example.who_nextdoor.HomeRecycler.HomeRecyclerAdapter;
import com.example.who_nextdoor.R;
import com.example.who_nextdoor.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private HomeRecyclerAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 화면 회전 막기
        init();
        getData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.profilebutton);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        adapter.setOnItemClickListener(new HomeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Data data = adapter.getItem(pos);
                if(data.getTitle().equals("정보 게시판")){
                    Intent intent = new Intent(HomeActivity.this, Information_BoardActivity.class);
                    startActivity(intent);
                }
                else if(data.getTitle().equals("거래 게시판")){
                    Intent intent = new Intent(HomeActivity.this, Trade_BoardActivity.class);
                    startActivity(intent);
                }
                

            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(user == null || !(firebaseAuth.getCurrentUser().isEmailVerified())){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.collection("users").document(user.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot != null){
                            if(documentSnapshot.exists()){
                                UserInfo userinfo = documentSnapshot.toObject(UserInfo.class); // 정보 받아와서 class에 저장
                                if(userinfo.getAccess().equals("F")){
                                    Intent intent = new Intent(HomeActivity.this, getUserInfo2Activity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(userinfo.getAccess().equals("W")){
                                    /* 네비게이션 드로어 테스트를 위해 임시로 주석처리 (0517)
                                    Intent intent = new Intent(HomeActivity.this, NoAccessWaitActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                     */
                                }
                            }
                            else{
                                Intent intent = new Intent(HomeActivity.this, getUserInfoActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }
            });
        }
      
    }
    public void Withdraw(View v){ // 회원 탈퇴 클릭 시
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(HomeActivity.this);
        alert_confirm.setMessage("정말로 계정을 삭제할까요?");
        alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseFirestore.collection("users").document(user.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                String filename = user.getUid() + "/" + user.getEmail() + ".png";
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                Task<Void> storageRef = storage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com").child("images/" + filename)
                                        .delete();
                            }
                        });
                        Toast.makeText(HomeActivity.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            }
        });
        alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert_confirm.show();

    }

    public void goProfile(View v){
        Intent intent = new Intent(this, getUserInfoActivity.class);
        startActivity(intent);
        finish();
    }
    public void Logout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, getUserInfoActivity.class);
        startActivity(intent);
        finish();
    }

    /*public void Letter(View v){
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }*/


    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new HomeRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        List<String> listTitle = Arrays.asList("정보 게시판", "거래 게시판", "**", "**");
        List<String> listContent = Arrays.asList(
                "정보 게시판입니다.",
                "거래 게시판입니다.",
                "**게시판입니다.",
                "**게시판입니다."
        );
        List<Integer> listResId = Arrays.asList(
                R.drawable.icon1,
                R.drawable.icon1,
                R.drawable.icon1,
                R.drawable.icon1
        );
        for (int i = 0; i < listTitle.size(); i++) {
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));
            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
