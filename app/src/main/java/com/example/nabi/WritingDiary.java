package com.example.nabi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import com.example.nabi.fragment.WritingFrag1;
import com.example.nabi.fragment.WritingFrag2;

import java.text.SimpleDateFormat;
import java.util.Date;

//일기 쓰는 부분 activity

public class WritingDiary extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    WritingFrag1 frag1;
    WritingFrag2 frag2;
    Date todaydate;
    TextView today_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_diary);

        frag1 = new WritingFrag1(); // 첫번째 페이지 fragment
        frag2 = new WritingFrag2(); // 두번째 페이지 fragment

        fragmentTransaction.replace(R.id.writingdiaryfrag, frag1).commit(); // 첫번째 페이지 보여주기

        today_date = findViewById(R.id.today_date);
        todaydate = new Date(System.currentTimeMillis());
        today_date.setText(new SimpleDateFormat("yyyy년 MM월 dd일").format(todaydate));    // 오늘 날짜 띄우기


    }

    // fragment에서 사용하는 함수
    // fragment에서 fragment로 화면 이동하기 위한 장치
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.writingdiaryfrag, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택함
    }
}