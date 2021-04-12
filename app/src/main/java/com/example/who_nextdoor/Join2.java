package com.example.who_nextdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.who_nextdoor.Join3;
import com.example.who_nextdoor.R;

public class Join2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);
    }
    public void GoJoin3(View v){
        Intent intent = new Intent(this, Join3.class);
        startActivity(intent);
        finish();
    }
    public void GoJoin1(View v){
        Intent intent = new Intent(this, com.example.who_nextdoor.Join1.class);
        startActivity(intent);
        finish();
    }
}