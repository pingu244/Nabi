package com.example.nabi.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nabi.R;
import com.example.nabi.WritingDiary;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

// 일기 작성할 때 두번째 페이지

public class WritingFrag2 extends Fragment {



    // 몇개 선택되었는지 count하는 변수
    int selected_count = 0;
    // 선택시 추가되는 버튼들에 붙여질 id들
    Integer selectbtn_ids[] = {R.id.mood_select1,R.id.mood_select2,R.id.mood_select3,R.id.mood_select4,R.id.mood_select5,R.id.mood_select6};
    // 선택시 선택된 id들이 저장되는 동적 배열
    ArrayList<Integer> selectbtn = new ArrayList<>();

    // '기쁨' 버튼 6개
    Button joyful[] = new Button[6];
    // '기쁨' 버튼들 id
    Integer joyful_id[] = {R.id.joyful1, R.id.joyful2, R.id.joyful3, R.id.joyful4, R.id.joyful5, R.id.joyful6};
    // '기쁨' 버튼들 선택되어져있는지 보여주는 변수 배열
    Boolean isjoyfulselected[] = {false,false,false,false,false,false};




    // 프래그먼트간의 이동 위한 인스턴스 생성
    public static WritingFrag2 newInstance() {
        return new WritingFrag2();
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // '기쁨' 버튼들 설정
        for (int i = 0; i<joyful_id.length; i++)
        {
            final int index;
            index = i;
            joyful[index] = getActivity().findViewById(joyful_id[index]);
            joyful[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isjoyfulselected[index]) // 선택 되어져있었다면 -> 선택 비활성화
                    {
                        // 선택 버튼 배경색깔을 선택되지않은 색으로 바꾸기
                        ViewCompat.setBackgroundTintList(joyful[index], ColorStateList.valueOf(Color.parseColor("#31000000")));
                        //  선택 버튼 글씨색깔을 선택되지않은 색으로 바꾸기
                        joyful[index].setTextColor(Color.parseColor("#000000"));


                        // 선택 버튼 삭제 : 작업중..
//                        findequalone(joyful[index]);

                        deleteBtn(selectbtn_ids[selected_count-1]);

                        // 선택버튼 저장하는 동적배열에서 빼기
                        selectbtn.remove(Integer.valueOf(joyful_id[index]));
                        // 선택버튼 개수 조정
                        selected_count--;
                        // 선택여부 조정
                        isjoyfulselected[index] = false;
                    }
                    else    // 선택되어져있지않았다면 -> 선택 활성화
                    {
                        // 선택 버튼 배경색깔을 선택한 색으로 바꾸기
                        ViewCompat.setBackgroundTintList(joyful[index], ColorStateList.valueOf(Color.parseColor("#95000000")));
                        // 선택 버튼 글씨색깔을 선택한 색으로 바꾸기
                        joyful[index].setTextColor(Color.parseColor("#ffffff"));
                        // 선택버튼 생성
                        createBtn(selectbtn_ids[selected_count],joyful[index].getText().toString());
                        // 동적배열에 저장
                        selectbtn.add(selectbtn_ids[selected_count]);
                        // 선택버튼 개수 조정
                        selected_count++;
                        // 선택여부 조정
                        isjoyfulselected[index] = true;
                    }

                }
            });

        }


        // 두번째 페이지의 이전으로 이동하는 버튼이어서 이름을 이렇게 지음(페이지_prior)
        // 이전 버튼 작동 : 첫번째 페이지로 이동
        Button second_prior = getActivity().findViewById(R.id.second_prior);
        second_prior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WritingDiary)getActivity()).replaceFragment(WritingFrag1.newInstance());
            }
        });







    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_writing_frag2, container, false);
    }


    // 버튼 생성 함수
    // 매개변수: 버튼의 id와 그 버튼의 text
    void createBtn(Integer what, String text)
    {

        Button mButton = new Button(getActivity()); //버튼을 선언

        FlexboxLayout.LayoutParams pm = new FlexboxLayout.LayoutParams
                (FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        pm.setMargins(5,5,5,5);

        mButton.setText(text); //버튼에 들어갈 텍스트를 지정(String)
        mButton.setBackgroundResource(R.drawable.q3_moodword_normal);

        mButton.setLayoutParams(pm); //앞서 설정한 레이아웃파라미터를 버튼에 적용
        ViewCompat.setBackgroundTintList(mButton, ColorStateList.valueOf(Color.parseColor("#000000"))); // 배경 색 지정
        mButton.setTextColor(Color.parseColor("#ffffff"));  // 글씨 색 지정
        mButton.setId(what);    // 아이디 지정: selectbtn_ids 중에 지정됨
        FlexboxLayout mView = getActivity().findViewById(R.id.mood_word_selectbox);
        mView.addView(mButton); //지정된 뷰에 셋팅완료된 mButton을 추가

    }

    // 버튼 삭제 함수
    // 매개변수 : 버튼 id
    void deleteBtn(Integer what)
    {
        Button mButton = getActivity().findViewById(what);
        FlexboxLayout mView = getActivity().findViewById(R.id.mood_word_selectbox);
        mView.removeView(mButton);
    }

    // 작업중인 getText()로 비교하여 삭제시키는 함수
    void findequalone(Button what)
    {
        Boolean find = false;
        int j = 0;
        while(j<6)
        {
            Button temp = getActivity().findViewById(selectbtn_ids[j]);
            if(temp == null)
                continue;
            find = temp.getText().equals(what.getText());
            if(find)
            {
                deleteBtn(selectbtn_ids[j]);
                break;
            }
            j++;
        }
    }
}