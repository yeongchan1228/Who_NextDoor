package com.example.who_nextdoor.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.who_nextdoor.R;
import com.example.who_nextdoor.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;

public class ViewSettingActivity extends AppCompatActivity {
    TextView mail, number, depart, name, phone, birth;
    private Uri filePath;
    private ImageView stimage;
    private String filename;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewsetting);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mail = (TextView)findViewById(R.id.UserMail);
        number = (TextView)findViewById(R.id.UserNumber);
        depart = (TextView)findViewById(R.id.UserDepartment);
        name = (TextView)findViewById(R.id.UserName);
        phone = (TextView)findViewById(R.id.UserPhonenumber);
        birth = (TextView)findViewById(R.id.Userbirth);


        if (user == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        stimage = (ImageView) findViewById(R.id.as_profile);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com");
        StorageReference pathReference = storageReference.child("users/"+user.getEmail()+"profile"+".png");

        DocumentReference documentReference2 = firebaseFirestore.collection("users").document(user.getUid());

        documentReference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);
                String name_nick = userInfo.getName() + "(" + userInfo.getAlias() + ")";
                name.setText("😎 " + name_nick + " 님");
                String umail = "💌 " + userInfo.getAddress();
                mail.setText(umail);
                String unumber = "🎉 " + userInfo.getShcoolNumber();
                number.setText(unumber);
                String udepart = "✏ " + userInfo.getDepartment();
                depart.setText(udepart);
                String uphone = "📞 " + userInfo.getPhoneNumber();
                phone.setText(uphone);
                String ubirth = userInfo.getBirthDay();
                String subbirth = "🍰 " + ubirth.substring(0, 4) + "년 " + ubirth.substring(4, 6) + "월 " + ubirth.substring(6, 8) + "일";
                birth.setText(subbirth);

                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com");
                StorageReference pathReference = storageReference.child("users/"+user.getEmail()+"profile"+".png");
                if(pathReference != null){
                    pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Glide.with(stimage).load(task.getResult()).circleCrop().into(stimage);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            stimage.setImageResource(R.drawable.human);
                            stimage.setBackground(new ShapeDrawable(new OvalShape()));
                            stimage.setClipToOutline(true);
                        }
                    });
                }
                else{
                    stimage.setImageResource(R.drawable.human);
                    stimage.setBackground(new ShapeDrawable(new OvalShape()));
                    stimage.setClipToOutline(true);
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                name.setText("미정");
                mail.setText("미정");
                number.setText("미정");
                depart.setText("미정");
                phone.setText("미정");
                birth.setText("미정");
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ViewSettingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void Okay(View v){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}
