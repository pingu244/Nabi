package com.example.nabi;

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

public class OnBoardingActivity extends AppCompatActivity {

    TextView tv_pass, tv_next, tv_onBoarding;
    ImageView imageView;
    ViewPager viewPager;
    LayoutInflater layoutInflater;
    TabLayout tabLayout;
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


        tv_onBoarding = findViewById(R.id.tv_onBoarding);

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
                if(viewPager.getCurrentItem()==4){
                    tv_pass.setVisibility(View.INVISIBLE);
                    tv_next.setText("완료");
                }
            }
        });

    }

    class OnBoardingAdapter extends PagerAdapter {

        LayoutInflater layoutInflater;

        public OnBoardingAdapter(LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }

        @Override

        public int getCount() {

            return 4; //이미지 개수 리턴

        }


        public Object instantiateItem(ViewGroup container, int position) {


            View view = null;


            view = layoutInflater.inflate(R.layout.onboarding, null);


            if (position == 0) {
                imageView.setImageResource(R.drawable.img_onboarding_1);
                tv_onBoarding.setText("우울한 날씨를 확인하세요");

            }
            else if(position ==1){
                imageView.setImageResource(R.drawable.img_onboarding_2);
                tv_onBoarding.setText("힐링 컨텐츠로 힐링해 보아요");
            }
            else if(position==2){
                imageView.setImageResource(R.drawable.img_onboarding_3);
                tv_onBoarding.setText("나의 하루를 기록해 보아요");
            }
            else{
                imageView.setImageResource(R.drawable.img_onboarding_4);
                tv_onBoarding.setText("감정 보고서를 열람해 보세요");
            }


            container.addView(view);

            return view;

        }

        @Override

        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);


        }

        @Override

        public boolean isViewFromObject(View v, Object obj) {

            return v == obj;

        }

    }
}
