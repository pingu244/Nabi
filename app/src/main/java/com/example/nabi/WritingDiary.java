package com.example.nabi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nabi.fragment.WritingFrag1;
import com.example.nabi.fragment.WritingFrag2;
import com.example.nabi.fragment.WritingFrag3;
import com.example.nabi.fragment.WritingFrag4;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//일기 쓰는 부분 activity

public class WritingDiary extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    WritingFrag1 frag1 = new WritingFrag1(); // 첫번째 페이지 fragment
    WritingFrag2 frag2 = new WritingFrag2(); // 두번째 페이지 fragment
    WritingFrag3 frag3 = new WritingFrag3(); // 세번째 페이지 fragment
    WritingFrag4 frag4 = new WritingFrag4(); // 네번째 페이지 fragment

    Date todaydate;
    TextView today_date;
    Button backbtn;

    ArrayList<Integer> selectbtns = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_diary);

        fragmentTransaction.add(R.id.writingdiaryfrag, frag1, "page1").commit(); // 첫번째 페이지 보여주기

        today_date = findViewById(R.id.today_date);
        todaydate = new Date(System.currentTimeMillis());
        today_date.setText(new SimpleDateFormat("yyyy년 MM월 dd일").format(todaydate));    // 오늘 날짜 띄우기

        backbtn = findViewById(R.id.WritingDiary_back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    // fragment 안에서 사용하는 함수
    // fragment에서 fragment로 화면 이동하기 위한 장치 (프래그먼트 유지)
    public void replaceFragment(String tag, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment TAG1 = fragmentManager.findFragmentByTag("page1");
        Fragment TAG2 = fragmentManager.findFragmentByTag("page2");
        Fragment TAG3 = fragmentManager.findFragmentByTag("page3");
        Fragment TAG4 = fragmentManager.findFragmentByTag("page4");

        // 일단 다 숨기기
        if(TAG1 != null)
            fragmentTransaction.hide(TAG1);
        if(TAG2 != null)
            fragmentTransaction.hide(TAG2);
        if(TAG3 != null)
            fragmentTransaction.hide(TAG3);
        if(TAG4 != null)
            fragmentTransaction.hide(TAG4);

        // 요구되는 페이지인 경우 : 그게 null이 아니면 show, null이라면 add
        switch (tag)
        {
            case "page1":
                if(TAG1 == null)
                    fragmentTransaction.add(R.id.writingdiaryfrag, frag1, tag);
                else fragmentTransaction.show(TAG1);    break;
            case "page2":
                if(TAG2 == null)
                    fragmentTransaction.add(R.id.writingdiaryfrag, frag2, tag);
                else fragmentTransaction.show(TAG2);    break;
            case "page3":
                if(TAG3 == null)
                    fragmentTransaction.add(R.id.writingdiaryfrag, frag3, tag);
                else fragmentTransaction.show(TAG3);    break;
            case "page4":
                if(TAG4 == null)
                    fragmentTransaction.add(R.id.writingdiaryfrag, frag4, tag);
                else fragmentTransaction.show(TAG4);    break;
        }

        fragmentTransaction.commit();      // commit로 실행
    }
}

