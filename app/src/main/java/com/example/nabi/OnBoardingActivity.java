package com.example.nabi;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rd.PageIndicatorView;

public class OnBoardingActivity extends AppCompatActivity {

    TextView tv_pass, tv_next, tv_onBoarding;
    ImageView imageView;
    ViewPager viewPager;
    LayoutInflater layoutInflater;
    TabLayout  tabLayout;
    LinearLayout onBoardingLayout;

    public OnBoardingActivity(int position) {
    }

    public OnBoardingActivity(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.onboarding);

        OnBoardingAdapter onBoardingAdapter = new OnBoardingAdapter(getLayoutInflater());


        tv_pass = findViewById(R.id.tv_pass);
        tv_next = findViewById(R.id.tv_next);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_onBoarding);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(onBoardingAdapter);

        imageView = findViewById(R.id.imgView);
        onBoardingLayout = findViewById(R.id.onBoardingLayout);


        tv_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(viewPager.getCurrentItem()==0){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }else if(viewPager.getCurrentItem()==1){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }else if(viewPager.getCurrentItem()==2){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    tv_next.setText("완료");
                    tv_pass.setVisibility(View.INVISIBLE);
                }else if(viewPager.getCurrentItem()==3){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


}
