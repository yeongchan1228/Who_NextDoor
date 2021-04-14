package com.example.who_nextdoor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import javax.crypto.MacSpi;

public class Join1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join1);
    }
    public void GoJoin2(View v){
        Intent intent = new Intent(this, com.example.who_nextdoor.Join2.class);
        startActivity(intent);
        finish();
    }
    public void GoStart(View v){
        Intent intent = new Intent(this, com.example.who_nextdoor.StartActivity.class);
        startActivity(intent);
        finish();
    }
    public void GoMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void GoSingupDone(View v){
        Intent intent = new Intent(this, com.example.who_nextdoor.SignupdoneActivity.class);
        startActivity(intent);
        finish();
    }
}
