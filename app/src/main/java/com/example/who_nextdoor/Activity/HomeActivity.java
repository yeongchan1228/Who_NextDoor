package com.example.who_nextdoor.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// ?????? ?????? ??????
        init();
        getData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.profilebutton);
        TextView textView10 = findViewById(R.id.time);
        textView10.setText("?????? ?????? "+getTime());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //View nav_header_view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        View nav_header_view = navigationView.getHeaderView(0);
        ImageView nv_profile = (ImageView) nav_header_view.findViewById(R.id.nh_image);
        ImageView nv_temperature = (ImageView) nav_header_view.findViewById(R.id.nv_temperature);
        nv_profile.setBackground(new ShapeDrawable(new OvalShape()));
        nv_profile.setClipToOutline(true);
        TextView nv_school = (TextView) nav_header_view.findViewById(R.id.student_id);
        TextView nv_name = (TextView) nav_header_view.findViewById(R.id.nv_name);
        TextView nv_email = (TextView) nav_header_view.findViewById(R.id.nv_email);



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

                if(id == R.id.message){
                    Intent intent = new Intent(HomeActivity.this, MessageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                if(id == R.id.godorm){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ssudorm.ssu.ac.kr:444/SShostel/mall_main.php?viewform=B0001_foodboard_list&board_no=1"));
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

                    /* ?????? ?????? - ????????? ?????? ?????????????????? */
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(HomeActivity.this);
                    alert_confirm.setMessage("????????? ????????? ????????????????");
                    alert_confirm.setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
                                    Toast.makeText(HomeActivity.this, "????????? ?????? ???????????????.", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }
                            });
                        }
                    });
                    alert_confirm.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert_confirm.show();
                    /* ?????? ?????? ???- ????????? ?????? ?????????????????? */

                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        adapter.setOnItemClickListener(new HomeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Data data = adapter.getItem(pos);
                if(data.getTitle().equals("?????? ?????????")){
                    Intent intent = new Intent(HomeActivity.this, Information_BoardActivity.class);
                    startActivity(intent);
                }
                else if(data.getTitle().equals("?????? ?????????")){
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
            DocumentReference documentReference2 = firebaseFirestore.collection("users").document(user.getUid());
            documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot != null){
                            if(documentSnapshot.exists()){
                                UserInfo userinfo = documentSnapshot.toObject(UserInfo.class); // ?????? ???????????? class??? ??????
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
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Intent intent = new Intent(HomeActivity.this, getUserInfoActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        DocumentReference documentReference2 = firebaseFirestore.collection("users").document(user.getUid());
        documentReference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);
                nv_school.setText(userInfo.getShcoolNumber());
                nv_name.setText(userInfo.getName());
                nv_email.setText(userInfo.getAddress());
                if(userInfo.getTemperature() > 30){
                    nv_temperature.setImageResource(R.drawable.temperature5);
                }
                else if(userInfo.getTemperature() > 10){
                    nv_temperature.setImageResource(R.drawable.temperature4);
                }

                else if(userInfo.getTemperature() > -5){
                    nv_temperature.setImageResource(R.drawable.temperature3);
                }
                else if(userInfo.getTemperature() <= -5){
                    nv_temperature.setImageResource(R.drawable.temperature1);
                }
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
                nv_school.setText("??????");
                nv_name.setText("??????");
                nv_email.setText("??????");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatting, menu) ;

        return true ;
    }


    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new HomeRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        List<String> listTitle = Arrays.asList("?????? ?????????", "?????? ?????????", "?????? ?????????", "?????? ?????????");
        List<String> listContent = Arrays.asList(
                "????????? ?????? ????????? ?????????.",
                "???????????? ????????????!",
                "???????????? ??????????????? ?????? ?????????",
                "?????? ?????????"
        );
        List<Integer> listResId = Arrays.asList(
                0,0,0,0//????????? ?????? ????????? ?????? ??????
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
            case android.R.id.home:{ // ?????? ?????? ?????? ????????? ???
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
    public String getTime(){ // ?????? ?????????
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd k:mm");
        long mNow;
        Date mDate;
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}