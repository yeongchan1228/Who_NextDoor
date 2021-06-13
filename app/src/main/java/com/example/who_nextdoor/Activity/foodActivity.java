package com.example.who_nextdoor.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.who_nextdoor.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class foodActivity extends AppCompatActivity {
    String menu;                // 식단 저장
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);
        textView = (TextView) findViewById(R.id.food);
        final Bundle bundle = new Bundle();
        String url = "https://ssudorm.ssu.ac.kr:444/SShostel/mall_main.php?viewform=B0001_foodboard_list&board_no=1";

        new Thread() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();
                    //Elements contents = doc.select(".boxstyle02").select("th");   // 식단 테이블 가져오기

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            textView.setText(bundle.getString("numbers"));                      //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다.
        }
    };
}
