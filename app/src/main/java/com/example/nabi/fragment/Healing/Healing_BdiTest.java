package com.example.nabi.fragment.Healing;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nabi.R;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag1;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag2;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag3;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag4;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag5;

public class Healing_BdiTest extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    BdiTestFrag1 frag1 = new BdiTestFrag1(); // 첫번째 페이지 fragment


    // fragment 안에서 사용하는 함수
    // fragment에서 fragment로 화면 이동하기 위한 장치 (프래그먼트 유지)
    public void replaceFragment(String tag, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment TAG1 = fragmentManager.findFragmentByTag("page1");



        if(TAG1 != null)
            fragmentTransaction.hide(TAG1);

        // 요구되는 페이지인 경우 : 그게 null이 아니면 show, null이라면 add
        switch (tag)
        {
            case "page1":
                if(TAG1 == null)
                    fragmentTransaction.add(R.id.bdi_test_frag, frag1, tag);
                else fragmentTransaction.show(TAG1);    break;

        }

        fragmentTransaction.commit();      // commit로 실행
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bdi_test_back);

        ImageButton btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fragmentTransaction.add(R.id.bdi_test_frag, frag1, "page1").commit(); // 첫번째 페이지 보여주기
    }
}
