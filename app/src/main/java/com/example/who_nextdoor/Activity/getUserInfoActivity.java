package com.example.who_nextdoor.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class getUserInfoActivity extends AppCompatActivity {
    private final String Tag = "getUserInfoActivity";
    Spinner spinner_u, spinner_m;
    ArrayAdapter majorAdapter;
    String m;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getuser);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        // 스피너 설정
        spinner_u = findViewById(R.id.Usershcooluniver);
        spinner_m = findViewById(R.id.Usershcoolmajor);

        ArrayAdapter univerAdapter = ArrayAdapter.createFromResource(
                this, R.array.univer_array, android.R.layout.simple_spinner_dropdown_item);

        univerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_u.setAdapter(univerAdapter);
        spinnerUpdate_u(spinner_u);

    }


    public void spinnerUpdate_u(Spinner spinner){
        spinner_u.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerOnSelected spinnerOnSelected = new SpinnerOnSelected();
                switch (position){
                    case 0:
                        majorAdapter = ArrayAdapter.createFromResource(getUserInfoActivity.this, R.array.major_array1, android.R.layout.simple_spinner_dropdown_item);
                        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_m.setAdapter(majorAdapter);
                        spinner_m.setOnItemSelectedListener(spinnerOnSelected);
                        break;
                    case 1:
                        majorAdapter = ArrayAdapter.createFromResource(getUserInfoActivity.this, R.array.major_array2, android.R.layout.simple_spinner_dropdown_item);
                        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_m.setAdapter(majorAdapter);
                        spinner_m.setOnItemSelectedListener(spinnerOnSelected);
                        break;
                    case 2:
                        majorAdapter = ArrayAdapter.createFromResource(getUserInfoActivity.this, R.array.major_array3, android.R.layout.simple_spinner_dropdown_item);
                        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_m.setAdapter(majorAdapter);
                        spinner_m.setOnItemSelectedListener(spinnerOnSelected);
                        break;
                    case 3:
                        majorAdapter = ArrayAdapter.createFromResource(getUserInfoActivity.this, R.array.major_array4, android.R.layout.simple_spinner_dropdown_item);
                        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_m.setAdapter(majorAdapter);
                        spinner_m.setOnItemSelectedListener(spinnerOnSelected);
                        break;
                    case 4:
                        majorAdapter = ArrayAdapter.createFromResource(getUserInfoActivity.this, R.array.major_array5, android.R.layout.simple_spinner_dropdown_item);
                        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_m.setAdapter(majorAdapter);
                        spinner_m.setOnItemSelectedListener(spinnerOnSelected);
                        break;
                    case 5:
                        majorAdapter = ArrayAdapter.createFromResource(getUserInfoActivity.this, R.array.major_array6, android.R.layout.simple_spinner_dropdown_item);
                        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_m.setAdapter(majorAdapter);
                        spinner_m.setOnItemSelectedListener(spinnerOnSelected);
                        break;
                    case 6:
                        majorAdapter = ArrayAdapter.createFromResource(getUserInfoActivity.this, R.array.major_array7, android.R.layout.simple_spinner_dropdown_item);
                        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_m.setAdapter(majorAdapter);
                        spinner_m.setOnItemSelectedListener(spinnerOnSelected);
                        break;
                    case 7:
                        majorAdapter = ArrayAdapter.createFromResource(getUserInfoActivity.this, R.array.major_array8, android.R.layout.simple_spinner_dropdown_item);
                        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_m.setAdapter(majorAdapter);
                        spinner_m.setOnItemSelectedListener(spinnerOnSelected);
                        break;
                    case 8:
                        majorAdapter = ArrayAdapter.createFromResource(getUserInfoActivity.this, R.array.major_array9, android.R.layout.simple_spinner_dropdown_item);
                        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_m.setAdapter(majorAdapter);
                        spinner_m.setOnItemSelectedListener(spinnerOnSelected);
                        break;
                    case 9:
                        majorAdapter = ArrayAdapter.createFromResource(getUserInfoActivity.this, R.array.major_array10, android.R.layout.simple_spinner_dropdown_item);
                        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_m.setAdapter(majorAdapter);
                        spinner_m.setOnItemSelectedListener(spinnerOnSelected);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    class SpinnerOnSelected implements  AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            m = parent.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getUserInfoActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void profileUpdate(View v) {
        RadioButton radioMan, radioWoman;
        RadioGroup radioGender;
        radioMan = findViewById(R.id.radioMan);
        radioWoman = findViewById(R.id.radioWoman);
        radioGender = findViewById(R.id.radioGender);
        final String department;
        final String name = ((EditText) findViewById(R.id.UserName)).getText().toString();
        final String phoneNumber = ((EditText) findViewById(R.id.UserPhonenumber)).getText().toString();
        final String birthDay = ((EditText) findViewById(R.id.Userbirth)).getText().toString();
        final String alias = ((EditText) findViewById(R.id.UserAlias)).getText().toString();
        final String schoolnumber = ((EditText) findViewById(R.id.Usershcoolnumber)).getText().toString();
        final String gender = radioMan.getText().toString();
        department = m;
        Toast.makeText(getUserInfoActivity.this, department, Toast.LENGTH_SHORT).show();

        boolean is_validity = true;
        if (name.length() < 2 || name.length() > 10) {
            is_validity = false;
            Toast.makeText(getUserInfoActivity.this, "올바른 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else if(phoneNumber.length()<10 || phoneNumber.length()>11){
            is_validity = false;
            Toast.makeText(getUserInfoActivity.this, "올바른 전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else if(birthDay.length()!=8){
            is_validity = false;
            Toast.makeText(getUserInfoActivity.this, "올바른 생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else if(alias.length()>8){
            is_validity = false;
            Toast.makeText(getUserInfoActivity.this, "닉네임의 길이가 너무 깁니다. 8글자 미만으로 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else if(schoolnumber.length()!=8){
            is_validity = false;
            Toast.makeText(getUserInfoActivity.this, "올바른 학번을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

        if(is_validity == false) return;

        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                if(i == R.id.radioMan){
                    final String gender = radioMan.getText().toString();
                    Toast.makeText(getUserInfoActivity.this, "남자", Toast.LENGTH_SHORT).show();
                }
                else if(i == R.id.radioWoman){
                    final String gender = radioWoman.getText().toString();
                    Toast.makeText(getUserInfoActivity.this, "여자", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //final String gender = ((EditText) findViewById(R.id.Usergneder)).getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserInfo userinfo = new UserInfo();

        DocumentReference documentReference = db.collection("users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null){
                        if(documentSnapshot.exists()){
                            UserInfo meminfo = documentSnapshot.toObject(UserInfo.class); // 정보 받아와서 class에 저장
                            userinfo.setName(meminfo.getName());
                            Log.d(Tag, userinfo.getName());
                            userinfo.setShcoolNumber(meminfo.getShcoolNumber());
                            userinfo.setTemperature(0);
                            userinfo.setAlias(meminfo.getAlias());
                            userinfo.setBirthDay(meminfo.getBirthDay());
                            userinfo.setPhoneNumber(meminfo.getPhoneNumber());
                            userinfo.setAccess(meminfo.getAccess());
                            userinfo.setGender(meminfo.getGender());
                            userinfo.setAddress(meminfo.getAddress());
                            userinfo.setFirst(meminfo.getFirst());
                            userinfo.setDepartment(meminfo.getDepartment());
                            if(userinfo.getFirst().equals("T")) {
                                if(!(TextUtils.isEmpty(name))){
                                    userinfo.setName(name);
                                }
                                if(!(TextUtils.isEmpty(phoneNumber))){
                                    userinfo.setPhoneNumber(phoneNumber);
                                }
                                if(!(TextUtils.isEmpty(birthDay))){
                                    userinfo.setBirthDay(birthDay);
                                }
                                if(!(TextUtils.isEmpty(alias))){
                                    userinfo.setAlias(alias);
                                }
                                if(!(TextUtils.isEmpty(schoolnumber))){
                                    userinfo.setShcoolNumber(schoolnumber);
                                }
                                if(!(TextUtils.isEmpty(gender))){
                                    userinfo.setGender(gender);
                                }
                                if(!(TextUtils.isEmpty(department))){
                                    userinfo.setDepartment(department);
                                }
                                userinfo.setAddress(user.getEmail());
                                if (user != null) {
                                    db.collection("users").document(user.getUid()).set(userinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (userinfo.getAccess().equals("T")) {
                                                AlertDialog.Builder oh = new AlertDialog.Builder(getUserInfoActivity.this);
                                                oh.setMessage("정보 수정 성공");
                                                oh.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent = new Intent(getUserInfoActivity.this, HomeActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                );
                                                oh.setCancelable(false);
                                                oh.show();
                                            } else {
                                                Toast.makeText(getUserInfoActivity.this, "기숙사 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getUserInfoActivity.this, getUserInfo2Activity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getUserInfoActivity.this, "오류 발생", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                        else{
                            if (!(TextUtils.isEmpty(name)) && !(TextUtils.isEmpty(phoneNumber)) && !(TextUtils.isEmpty(birthDay))
                                    && !(TextUtils.isEmpty(alias)) && !(TextUtils.isEmpty(schoolnumber)) && !(TextUtils.isEmpty(gender)) && !(TextUtils.isEmpty(department)) ) {
                                userinfo.setName(name);
                                userinfo.setPhoneNumber(phoneNumber);
                                userinfo.setBirthDay(birthDay);
                                userinfo.setAlias(alias);
                                userinfo.setTemperature(0);
                                userinfo.setShcoolNumber(schoolnumber);
                                userinfo.setGender(gender);
                                userinfo.setAddress(user.getEmail());
                                Toast.makeText(getUserInfoActivity.this,  m, Toast.LENGTH_SHORT).show();
                                userinfo.setDepartment(m);
                                userinfo.setFirst("T");
                                if (user != null) {
                                    db.collection("users").document(user.getUid()).set(userinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            AlertDialog.Builder oh = new AlertDialog.Builder(getUserInfoActivity.this);
                                            oh.setMessage("정보 저장 성공");
                                            oh.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(getUserInfoActivity.this, getUserInfo2Activity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                            );
                                            oh.setCancelable(false);
                                            oh.show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getUserInfoActivity.this, "오류 발생", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            else {
                                Toast.makeText(getUserInfoActivity.this, "회원 정보를 다 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
    }

}