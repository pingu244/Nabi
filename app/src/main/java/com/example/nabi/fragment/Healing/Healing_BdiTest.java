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
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag10;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag11;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag12;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag13;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag14;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag15;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag16;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag17;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag18;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag19;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag2;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag20;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag21;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag3;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag4;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag5;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag6;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag7;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag8;
import com.example.nabi.fragment.Healing.BDI.BdiTestFrag9;

public class Healing_BdiTest extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


    BdiTestFrag1 bdi1 = new BdiTestFrag1();
    BdiTestFrag2 bdi2 = new BdiTestFrag2();
    BdiTestFrag3 bdi3 = new BdiTestFrag3();
    BdiTestFrag4 bdi4 = new BdiTestFrag4();
    BdiTestFrag5 bdi5 = new BdiTestFrag5();
    BdiTestFrag6 bdi6 = new BdiTestFrag6();
    BdiTestFrag7 bdi7 = new BdiTestFrag7();
    BdiTestFrag8 bdi8 = new BdiTestFrag8();
    BdiTestFrag9 bdi9 = new BdiTestFrag9();
    BdiTestFrag10 bdi10 = new BdiTestFrag10();
    BdiTestFrag11 bdi11 = new BdiTestFrag11();
    BdiTestFrag12 bdi12 = new BdiTestFrag12();
    BdiTestFrag13 bdi13 = new BdiTestFrag13();
    BdiTestFrag14 bdi14 = new BdiTestFrag14();
    BdiTestFrag15 bdi15 = new BdiTestFrag15();
    BdiTestFrag16 bdi16 = new BdiTestFrag16();
    BdiTestFrag17 bdi17 = new BdiTestFrag17();
    BdiTestFrag18 bdi18 = new BdiTestFrag18();
    BdiTestFrag19 bdi19 = new BdiTestFrag19();
    BdiTestFrag20 bdi20 = new BdiTestFrag20();
    BdiTestFrag21 bdi21 = new BdiTestFrag21();


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
        fragmentTransaction.add(R.id.bdi_test_frag, bdi1, "page1").commit(); // 첫번째 페이지 보여주기
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
        Fragment TAG5 = fragmentManager.findFragmentByTag("page5");
        Fragment TAG6 = fragmentManager.findFragmentByTag("page6");
        Fragment TAG7 = fragmentManager.findFragmentByTag("page7");
        Fragment TAG8 = fragmentManager.findFragmentByTag("page8");
        Fragment TAG9 = fragmentManager.findFragmentByTag("page9");
        Fragment TAG10 = fragmentManager.findFragmentByTag("page10");
        Fragment TAG11 = fragmentManager.findFragmentByTag("page11");
        Fragment TAG12 = fragmentManager.findFragmentByTag("page12");
        Fragment TAG13 = fragmentManager.findFragmentByTag("page13");
        Fragment TAG14 = fragmentManager.findFragmentByTag("page14");
        Fragment TAG15 = fragmentManager.findFragmentByTag("page15");
        Fragment TAG16 = fragmentManager.findFragmentByTag("page16");
        Fragment TAG17 = fragmentManager.findFragmentByTag("page17");
        Fragment TAG18 = fragmentManager.findFragmentByTag("page18");
        Fragment TAG19 = fragmentManager.findFragmentByTag("page19");
        Fragment TAG20 = fragmentManager.findFragmentByTag("page20");
        Fragment TAG21 = fragmentManager.findFragmentByTag("page21");


        if(TAG1 != null)
            fragmentTransaction.hide(TAG1);
        else if(TAG2 != null)
            fragmentTransaction.hide(TAG2);
        else if(TAG3 != null)
            fragmentTransaction.hide(TAG3);
        else if(TAG4 != null)
            fragmentTransaction.hide(TAG4);
        else if(TAG5 != null)
            fragmentTransaction.hide(TAG5);
        else if(TAG6 != null)
            fragmentTransaction.hide(TAG6);
        else if(TAG7 != null)
            fragmentTransaction.hide(TAG7);
        else if(TAG8 != null)
            fragmentTransaction.hide(TAG8);
        else if(TAG9 != null)
            fragmentTransaction.hide(TAG9);
        else if(TAG10 != null)
            fragmentTransaction.hide(TAG10);
        else if(TAG11 != null)
            fragmentTransaction.hide(TAG11);
        else if(TAG12 != null)
            fragmentTransaction.hide(TAG12);

        else if(TAG13 != null)
            fragmentTransaction.hide(TAG13);

        else if(TAG14 != null)
            fragmentTransaction.hide(TAG14);
        else if(TAG15 != null)
            fragmentTransaction.hide(TAG15);
        else if(TAG16 != null)
            fragmentTransaction.hide(TAG16);
        else if(TAG17 != null)
            fragmentTransaction.hide(TAG17);
        else if(TAG18 != null)
            fragmentTransaction.hide(TAG18);
        else if(TAG19 != null)
            fragmentTransaction.hide(TAG19);
        else if(TAG20 != null)
            fragmentTransaction.hide(TAG20);
        else if(TAG21 != null)
            fragmentTransaction.hide(TAG21);


        // 요구되는 페이지인 경우 : 그게 null이 아니면 show, null이라면 add
        switch (tag)
        {
            case "page1":
                if(TAG1 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi1, tag);
                else fragmentTransaction.show(TAG1);    break;

            case "page2":
                if(TAG2 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi2, tag);
                else fragmentTransaction.show(TAG2);    break;

            case "page3":
                if(TAG2 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi3, tag);
                else fragmentTransaction.show(TAG3);    break;
            case "page4":
                if(TAG4 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi4, tag);
                else fragmentTransaction.show(TAG4);    break;

            case "page5":
                if(TAG5 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi5, tag);
                else fragmentTransaction.show(TAG5);    break;

            case "page6":
                if(TAG6 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi6, tag);
                else fragmentTransaction.show(TAG6);    break;
            case "page7":
                if(TAG7 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi7, tag);
                else fragmentTransaction.show(TAG7);    break;

            case "page8":
                if(TAG8 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi8, tag);
                else fragmentTransaction.show(TAG8);    break;

            case "page9":
                if(TAG9 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi9, tag);
                else fragmentTransaction.show(TAG9);    break;
            case "page10":
                if(TAG10 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi10, tag);
                else fragmentTransaction.show(TAG10);    break;

            case "page11":
                if(TAG11 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi11, tag);
                else fragmentTransaction.show(TAG11);    break;

            case "page12":
                if(TAG12 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi12, tag);
                else fragmentTransaction.show(TAG12);    break;
            case "page13":
                if(TAG13 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi13, tag);
                else fragmentTransaction.show(TAG13);    break;

            case "page14":
                if(TAG14 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi14, tag);
                else fragmentTransaction.show(TAG14);    break;

            case "page15":
                if(TAG15 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi15, tag);
                else fragmentTransaction.show(TAG15);    break;
            case "page16":
                if(TAG16 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi16, tag);
                else fragmentTransaction.show(TAG16);    break;

            case "page17":
                if(TAG17 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi17, tag);
                else fragmentTransaction.show(TAG17);    break;

            case "page18":
                if(TAG18 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi18, tag);
                else fragmentTransaction.show(TAG18);    break;
            case "page19":
                if(TAG19 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi19, tag);
                else fragmentTransaction.show(TAG19);    break;

            case "page20":
                if(TAG20 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi20, tag);
                else fragmentTransaction.show(TAG20);    break;

            case "page21":
                if(TAG21 == null)
                    fragmentTransaction.replace(R.id.bdi_test_frag, bdi21, tag);
                else fragmentTransaction.show(TAG21);    break;
        }

        fragmentTransaction.commit();      // commit로 실행
    }

}
