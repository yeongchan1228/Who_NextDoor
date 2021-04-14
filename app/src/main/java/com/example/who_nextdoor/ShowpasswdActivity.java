package com.example.who_nextdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ShowpasswdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpasswd);
    }

    public void GoMain(View v){
        Intent intent = new Intent(this, com.example.who_nextdoor.MainActivity.class);
        startActivity(intent);
        finish();
    }
}