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
    public void GoMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void GoJoin2(View v){
        Intent intent = new Intent(this, Join2.class);
        startActivity(intent);
        finish();
    }
}
