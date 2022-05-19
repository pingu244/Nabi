package com.example.nabi;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import org.w3c.dom.Text;

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

        ImageView imageView;
        TextView tv_pass, tv_next, tv_onBoarding;
        LinearLayout onBoardingLayout;

        view = layoutInflater.inflate(R.layout.onboarding_adapter, null);

        imageView = view.findViewById(R.id.imgView);
        tv_onBoarding = view.findViewById(R.id.tv_onBoarding);
        onBoardingLayout = view.findViewById(R.id.onBoardingLayout);


        switch (position){
            case 0:
                imageView.setImageResource(R.drawable.img_onboarding_1);
                tv_onBoarding.setText("우울한 날씨를 확인하세요");
                onBoardingLayout.setBackgroundColor(Color.parseColor("#C2E2E8")); //색깔 바꿔줘요
                break;
            case 1:
                imageView.setImageResource(R.drawable.img_onboarding_2);
                tv_onBoarding.setText("힐링 컨텐츠로 힐링해 보아요");
                onBoardingLayout.setBackgroundColor(Color.parseColor("#FFD8D8"));
                break;
            case 2:
                imageView.setImageResource(R.drawable.img_onboarding_3);
                tv_onBoarding.setText("나의 하루를 기록해 보아요");
                onBoardingLayout.setBackgroundColor(Color.parseColor("#98C593"));
                break;
            case 3:
                imageView.setImageResource(R.drawable.img_onboarding_4);
                tv_onBoarding.setText("감정 보고서를 열람해 보세요");
                onBoardingLayout.setBackgroundColor(Color.parseColor("#D6C7ED"));
                break;
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