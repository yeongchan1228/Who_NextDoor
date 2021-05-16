package com.example.who_nextdoor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.who_nextdoor.Activity.MainActivity;
import com.example.who_nextdoor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textTitle, textContents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();
        String Title = intent.getStringExtra("Title");
        String Contents = intent.getStringExtra("Contents");

        imageView = findViewById(R.id.post_imageview);
        textTitle = findViewById(R.id.post_title);
        textContents = findViewById(R.id.post_contents);
        textTitle.setText(Title);
        textContents.setText(Contents);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com");
        StorageReference pathReference = storageReference.child("i_board");
        if(pathReference != null){
            StorageReference submitimage = storageReference.child("i_board/"+Title+".png");
            submitimage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Glide.with(PostActivity.this).load(task.getResult()).into(imageView);
                    }
                }
            });
        }
    }


}