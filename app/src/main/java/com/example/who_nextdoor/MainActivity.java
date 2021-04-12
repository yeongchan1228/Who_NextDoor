package com.example.who_nextdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.who_nextdoor.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void GoHome(View v){
        Intent intent = new Intent(this, com.example.who_nextdoor.HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void Join1(View v){
        Intent intent = new Intent(this, com.example.who_nextdoor.Join1.class);
        startActivity(intent);
        finish();
    }
}