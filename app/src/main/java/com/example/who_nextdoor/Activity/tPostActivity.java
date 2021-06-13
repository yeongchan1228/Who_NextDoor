package com.example.who_nextdoor.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.who_nextdoor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class tPostActivity extends AppCompatActivity {
    ImageView imageView, imageuser;
    TextView textTitle, textContents, textdate, textuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();
        String Title = intent.getStringExtra("Title");
        String Contents = intent.getStringExtra("Contents");
        String date = intent.getStringExtra("Date");
        String Uid = intent.getStringExtra("Uid");

        imageView = findViewById(R.id.post_imageview);
        textTitle = findViewById(R.id.post_title);
        textContents = findViewById(R.id.post_contents);
        textdate = findViewById(R.id.post_date);
        textuid = findViewById(R.id.post_set);
        imageuser = findViewById(R.id.iuser);

        textTitle.setText(Title);
        textContents.setText(Contents);
        textdate.setText(date);
        textuid.setText(Uid);
        imageuser.setImageResource(R.drawable.userbasic);




        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com");
        StorageReference pathReference = storageReference.child("t_board");
        if (pathReference != null) {
            StorageReference submitimage = storageReference.child("t_board/" + Title + ".png");
            submitimage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Glide.with(com.example.who_nextdoor.Activity.tPostActivity.this).load(task.getResult()).into(imageView);
                    }
                }
            });
        }
    }
}