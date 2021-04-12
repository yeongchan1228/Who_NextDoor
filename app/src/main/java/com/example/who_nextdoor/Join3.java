package com.example.who_nextdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.who_nextdoor.R;

public class Join3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join3);
    }
    public void GoMain(View v){
        Intent intent = new Intent(this, com.example.who_nextdoor.MainActivity.class);
        startActivity(intent);
        finish();
    }
}