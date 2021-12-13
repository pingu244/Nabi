package com.example.nabi.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nabi.R;
import com.example.nabi.WritingDiary;
import com.google.android.flexbox.FlexboxLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

// 일기 세번째 페이지

public class WritingFrag3 extends Fragment {

    View view;
    EditText content_3;
    String keywords = "";
    // 선택시 선택된 id들이 저장되는 동적 배열
    ArrayList<Integer> page3_selectbtn = new ArrayList<>();


    // 프래그먼트간의 이동 위한 인스턴스 생성
    public static WritingFrag3 newInstance() {
        return new WritingFrag3();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        content_3 = getActivity().findViewById(R.id.diary_content_3);

        // 이전 버튼 작동 : 두번째 페이지로 이동
        Button third_prior = getActivity().findViewById(R.id.third_prior);
        third_prior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WritingDiary)getActivity()).replaceFragment("page2",WritingFrag2.newInstance());
            }
        });
        // 다음 버튼 작동 : 네번째 페이지로 이동
        Button third_next = getActivity().findViewById(R.id.third_next);
        third_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WritingDiary)getActivity()).replaceFragment("page4", WritingFrag4.newInstance());
                ((WritingDiary)getActivity()).q4_why = content_3.getText().toString();
                Log.v("frag3",((WritingDiary)getActivity()).q4_why);
                Log.v("frag2&3", ((WritingDiary)getActivity()).q3_todayKeyword);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_writing_frag3, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getParentFragmentManager().setFragmentResultListener("selectbtns", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                page3_selectbtn = bundle.getIntegerArrayList("selectbtns");
                // Do something with the result...
            }
        });

        for(int i = 0; i<page3_selectbtn.size(); i++)
        {
            TextView txt = new TextView(getActivity());

            TextView temp = getActivity().findViewById(page3_selectbtn.get(i));

            FlexboxLayout.LayoutParams pm = new FlexboxLayout.LayoutParams
                    (FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);

            DisplayMetrics dm = getResources().getDisplayMetrics(); // 단위를 dp로 맞춰주기 위함
            pm.setMargins(Math.round(5*dm.density),Math.round(5*dm.density),0,Math.round(5*dm.density));
            txt.setPadding(Math.round(12*dm.density),Math.round(6*dm.density),Math.round(12*dm.density),Math.round(6*dm.density));


            txt.setText(temp.getText()); //버튼에 들어갈 텍스트를 지정(String)
            txt.setTextSize(15);
            keywords = keywords  + temp.getText()+ ",";
            txt.setBackgroundResource(R.drawable.q3_moodword_normal);

            txt.setLayoutParams(pm); //앞서 설정한 레이아웃파라미터를 버튼에 적용
            ViewCompat.setBackgroundTintList(txt, ColorStateList.valueOf(Color.parseColor("#686868"))); // 배경 색 지정
            txt.setTextColor(Color.parseColor("#ffffff"));  // 글씨 색 지정

            FlexboxLayout mView = getActivity().findViewById(R.id.page3_selectbox);
            mView.addView(txt); //지정된 뷰에 셋팅완료된 mButton을 추가
        }

        Log.v("page3", page3_selectbtn.toString());
        Log.v("page3", " ");

    }
}