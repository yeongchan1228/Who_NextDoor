package com.example.who_nextdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GofindpwdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gofindpwd);
    }
    public void GoShowpasswd(View v){
        Intent intent = new Intent(this, ShowpasswdActivity.class);
        startActivity(intent);
        finish();
    }
}