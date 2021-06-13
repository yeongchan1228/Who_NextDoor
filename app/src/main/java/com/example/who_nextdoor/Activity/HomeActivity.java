package com.example.who_nextdoor.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.app.Person;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import androidx.core.view.GravityCompat;


import com.bumptech.glide.Glide;
import com.example.who_nextdoor.HomeRecycler.Data;
import com.example.who_nextdoor.HomeRecycler.HomeRecyclerAdapter;
import com.example.who_nextdoor.R;
import com.example.who_nextdoor.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private MenuItem item;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        //View nav_header_view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        View nav_header_view = navigationView.getHeaderView(0);
        ImageView nv_profile = (ImageView) nav_header_view.findViewById(R.id.nh_image);
        nv_profile.setBackground(new ShapeDrawable(new OvalShape()));
        nv_profile.setClipToOutline(true);
        TextView nv_school = (TextView) nav_header_view.findViewById(R.id.student_id);
        TextView nv_name = (TextView) nav_header_view.findViewById(R.id.nv_name);
        TextView nv_email = (TextView) nav_header_view.findViewById(R.id.nv_email);

        DocumentReference documentReference2 = firebaseFirestore.collection("users").document(user.getUid());
        documentReference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);
                nv_school.setText(userInfo.getShcoolNumber());
                nv_name.setText(userInfo.getName());
                nv_email.setText(userInfo.getAddress());

                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com");
                StorageReference pathReference = storageReference.child("users/"+user.getEmail()+"profile"+".png");
                if(pathReference != null){
                    pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Glide.with(nv_profile).load(task.getResult()).into(nv_profile);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            nv_profile.setImageResource(R.drawable.human);
                        }
                    });
                }
                else{
                    nv_profile.setImageResource(R.drawable.human);
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                nv_school.setText("미정");
                nv_name.setText("미정");
                nv_email.setText("미정");
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if(id == R.id.viewsetting){
                    Intent intent = new Intent(HomeActivity.this, ViewSettingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                if(id == R.id.setting){
                    Intent intent = new Intent(HomeActivity.this, SettingUserinfo.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                if(id == R.id.chat){
                    Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                else if(id == R.id.logout){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(HomeActivity.this, getUserInfoActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(id == R.id.getout){

                    /* 회원 탈퇴 - 아래서 코드 긁어왔습니다 */
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
                    /* 회원 탈퇴 끝- 아래서 코드 긁어왔습니다 */

                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



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

                                    Intent intent = new Intent(HomeActivity.this, NoAccessWaitActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatting, menu) ;

        return true ;
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

    public void showMyMessage(View v) {
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }

    public void foodMenu(View v) {
        Intent intent = new Intent(this, foodActivity.class);
        startActivity(intent);
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new HomeRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        List<String> listTitle = Arrays.asList("정보 게시판", "거래 게시판", "자유 게시판", "기타 게시판");
        List<String> listContent = Arrays.asList(
                "기숙사 정보 공유는 여기서.",
                "필요한거 거래해요!",
                "자유롭게 이야기하는 자유 게시판",
                "기타 이야기"
        );
        List<Integer> listResId = Arrays.asList(
                0,0,0,0//게시판 기존 화살표 일단 없앰
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
            case R.id.menu_chat :{
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                return true;}


        }
        return super.onOptionsItemSelected(item);
    }
}