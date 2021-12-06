package com.example.nabi.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nabi.R;
import com.example.nabi.WritingDiary;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

// 일기 작성할 때 두번째 페이지

public class WritingFrag2 extends Fragment {

    View view;
    String keywords = "";


    // 몇개 선택되었는지 count하는 변수
    int selected_count = 0;
    // 선택시 추가되는 버튼들에 붙여질 id들
    Integer selectbtn_ids[] = {R.id.mood_select1,R.id.mood_select2,R.id.mood_select3,R.id.mood_select4,R.id.mood_select5,R.id.mood_select6};
    // 6개의 아이디의 생성 여부
    Boolean isSelectbtnExist[] = {false,false,false,false,false,false};
    // 선택시 선택된 id들이 저장되는 동적 배열
    ArrayList<Integer> selectbtn = new ArrayList<>();


    // 감정 버튼 6개
    Button mood[][] = new Button[6][6];
    // 감정 버튼들 id
    Integer mood_ids[][] = {{R.id.joyful1, R.id.joyful2, R.id.joyful3, R.id.joyful4, R.id.joyful5, R.id.joyful6},
            {R.id.peaceful1, R.id.peaceful2, R.id.peaceful3, R.id.peaceful4, R.id.peaceful5, R.id.peaceful6},
            {R.id.powerful1, R.id.powerful2, R.id.powerful3, R.id.powerful4, R.id.powerful5, R.id.powerful6},
            {R.id.sad1, R.id.sad2, R.id.sad3, R.id.sad4, R.id.sad5, R.id.sad6},
            {R.id.mad1, R.id.mad2, R.id.mad3, R.id.mad4, R.id.mad5, R.id.mad6},
            {R.id.scared1, R.id.scared2, R.id.scared3, R.id.scared4, R.id.scared5, R.id.scared6}};
    // 감정 버튼들 선택되어져있는지 보여주는 변수 배열
    Boolean iswordselected[][] = {{false,false,false,false,false,false},{false,false,false,false,false,false},
            {false,false,false,false,false,false},{false,false,false,false,false,false},
            {false,false,false,false,false,false},{false,false,false,false,false,false}};




    // 프래그먼트간의 이동 위한 인스턴스 생성
    public static WritingFrag2 newInstance() {
        return new WritingFrag2();
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // 감정 버튼들 설정
        for (int i = 0; i<6; i++)
        {
            final int index1;
            index1 = i;
            for (int j = 0; j<6; j++) {
                final int index2;
                index2 = j;
                mood[index1][index2] = getActivity().findViewById(mood_ids[index1][index2]);
                mood[index1][index2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (iswordselected[index1][index2]) // 선택 되어져있었다면 -> 선택 비활성화
                        {
                            // 선택 버튼 배경색깔을 선택되지않은 색으로 바꾸기
                            ViewCompat.setBackgroundTintList(mood[index1][index2], ColorStateList.valueOf(Color.parseColor("#31000000")));
                            //  선택 버튼 글씨색깔을 선택되지않은 색으로 바꾸기
                            mood[index1][index2].setTextColor(Color.parseColor("#000000"));

                            // 선택 버튼 삭제
                            findequalone(mood[index1][index2]);

                            // 선택버튼 개수 조정
                            selected_count--;
                            // 선택여부 조정
                            iswordselected[index1][index2] = false;
                        } else if(selected_count < 6)   // 선택되어져있지않았다면 -> 선택 활성화
                        {
                            // 선택 버튼 배경색깔을 선택한 색으로 바꾸기
                            ViewCompat.setBackgroundTintList(mood[index1][index2], ColorStateList.valueOf(Color.parseColor("#95000000")));
                            // 선택 버튼 글씨색깔을 선택한 색으로 바꾸기
                            mood[index1][index2].setTextColor(Color.parseColor("#ffffff"));
                            // 선택버튼 생성
                            createBtn(mood[index1][index2].getText().toString());
                            // 선택버튼 개수 조정
                            selected_count++;
                            // 선택여부 조정
                            iswordselected[index1][index2] = true;
                        }
                        else
                            Toast.makeText(getContext(), "6개까지 선택해주세요", Toast.LENGTH_SHORT).show();

                    }
                });
            }

        }


        // 두번째 페이지의 이전으로 이동하는 버튼이어서 이름을 이렇게 지음(페이지_prior)
        // 이전 버튼 작동 : 첫번째 페이지로 이동
        Button second_prior = getActivity().findViewById(R.id.second_prior);
        second_prior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WritingDiary)getActivity()).replaceFragment("page1",WritingFrag1.newInstance());
            }
        });
        // 다음 버튼 작동 : 세번째 페이지로 이동
        Button second_next = getActivity().findViewById(R.id.second_next);
        second_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keywords = "";

                // 세번째 페이지가 이미 있는 경우
                if(getActivity().getSupportFragmentManager().findFragmentByTag("page3") != null)
                {
                    // 세번째 페이지의 단어 보여주는 곳을 삭제 후 다시 만들기
                    FlexboxLayout originalView = getActivity().findViewById(R.id.page3_selectbox);
                    LinearLayout View = getActivity().findViewById(R.id.page3_selectbox_linear);
                    View.removeView(originalView);


                    FlexboxLayout newview = new FlexboxLayout(getActivity());
                    newview.setId(R.id.page3_selectbox);
                    FlexboxLayout.LayoutParams pm1 = new FlexboxLayout.LayoutParams
                            (FlexboxLayout.LayoutParams.MATCH_PARENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                    newview.setFlexWrap(FlexWrap.WRAP);
                    newview.setAlignItems(AlignItems.STRETCH);
                    newview.setAlignContent(AlignContent.STRETCH);
                    newview.setLayoutParams(pm1);
                    View.addView(newview);

                    for(int i = 0; i<selectbtn.size(); i++)
                    {
                        Button mButton = new Button(getActivity()); //버튼을 선언
                        Button temp = getActivity().findViewById(selectbtn.get(i));

                        FlexboxLayout.LayoutParams pm = new FlexboxLayout.LayoutParams
                                (FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                        pm.setMargins(5,5,5,5);

                        mButton.setText(temp.getText()); //버튼에 들어갈 텍스트를 지정(String)

                        keywords = keywords  + temp.getText()+ ",";

                        mButton.setBackgroundResource(R.drawable.q3_moodword_normal);

                        mButton.setLayoutParams(pm); //앞서 설정한 레이아웃파라미터를 버튼에 적용
                        ViewCompat.setBackgroundTintList(mButton, ColorStateList.valueOf(Color.parseColor("#000000"))); // 배경 색 지정
                        mButton.setTextColor(Color.parseColor("#ffffff"));  // 글씨 색 지정

                        FlexboxLayout mView = getActivity().findViewById(R.id.page3_selectbox);
                        mView.addView(mButton); //지정된 뷰에 셋팅완료된 mButton을 추가
                    }
                }
                else{
                    for (int i = 0; i<selectbtn.size(); i++)
                    {
                        // fragment 처음 작동할때만 bundle로 넘기기 <-- 해결방안이 없을까..?
                        Bundle result = new Bundle();
                        result.putIntegerArrayList("selectbtns",selectbtn);//번들에 넘길 값 저장
                        getParentFragmentManager().setFragmentResult("selectbtns", result);

                        Button temp = getActivity().findViewById(selectbtn.get(i));
                        keywords = keywords  + temp.getText()+ ",";
                    }
                }



                ((WritingDiary)getActivity()).replaceFragment("page3",WritingFrag3.newInstance());
                ((WritingDiary)getActivity()).q3_todayKeyword = keywords;
                Log.v("frag2", ((WritingDiary)getActivity()).q3_todayKeyword);
            }
        });







    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_writing_frag2, container, false);
        return view;
    }


    // 버튼 생성 함수
    // 매개변수: 버튼의 id와 그 버튼의 text
    void createBtn(String text)
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
        for(int j = 0; j<6; j++)
        {
            if(!isSelectbtnExist[j])
            {
                mButton.setId(selectbtn_ids[j]);    // 아이디 지정: selectbtn_ids 중에 지정됨
                isSelectbtnExist[j] = true;
                selectbtn.add(selectbtn_ids[j]);    // 동적 배열에 저장
                break;
            }
        }
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

    // getText()로 비교하여 삭제시키는 함수
    void findequalone(Button what)
    {
        Boolean find = false;
        int j = 0;
        while(j<selectbtn_ids.length)
        {
            try {
                Button temp = getActivity().findViewById(selectbtn_ids[j]);
                find = temp.getText().equals(what.getText());
                if(find) {
                    deleteBtn(selectbtn_ids[j]);
                    selectbtn.remove(Integer.valueOf(selectbtn_ids[j]));    // 동적 배열에서 삭제
                    isSelectbtnExist[j] = false;
                    break;
                }
            }
            catch (NullPointerException e){}
            j++;
        }
    }
}