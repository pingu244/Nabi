package com.example.nabi.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.nabi.DBHelper;
import com.example.nabi.R;
import com.example.nabi.WritingDiary;

import java.util.Calendar;

// 일기 맨 처음 페이지

public class WritingFrag1 extends Fragment {

    WritingFrag2 frag2 = new WritingFrag2();
    EditText content_1;
    private SQLiteDatabase db;
    DBHelper dbHelper;
    Integer seekbarValue = 3;

    // fragment에서 fragment로 이동하기 위한 장치
    public static WritingFrag1 newInstance() {
        return new WritingFrag1();
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DBHelper(requireContext());
        db = dbHelper.getWritableDatabase();

        content_1 = view.findViewById(R.id.diary_content_1);

        Calendar cal = Calendar.getInstance();
        int cYEAR = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DATE);

        String YMD = (cYEAR+"년 "+(cMonth+1)+"월 "+cDay+"일");

        // 첫번째 페이지에 있는 '다음'버튼이어서 이름을 이렇게 지음 (페이지번째_next)
        // '다음'버튼을 누르면 다음 fragment로 넘어감
        Button first_next = getActivity().findViewById(R.id.first_next);
        first_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WritingDiary)getActivity()).replaceFragment("page2", WritingFrag2.newInstance());
                ((WritingDiary)getActivity()).q1_mood = seekbarValue;
                ((WritingDiary)getActivity()).q2_whatHappen = content_1.getText().toString();
                // db에 넣는 과정
//                db.execSQL("insert into diary_post (post_id, user_id, diary_title, content_1, diary_weather,reporting_date) " +
//                        "values (?, 'jungin-2','11월 30일의 일기','"+content_1.getText().toString()+"',0,'"+YMD+"')");
                Log.v("frag1",((WritingDiary)getActivity()).q1_mood.toString());
                Log.v("frag11",((WritingDiary)getActivity()).q2_whatHappen);
            }
        });

        // 질문1: 기분 색칠
        SeekBar seekbar = getActivity().findViewById(R.id.seekbar);
        TextView seekbar_per = getActivity().findViewById(R.id.seekbar_percent);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // 시크바 조작중
                switch (seekbar.getProgress())
                {
                    case 0:
                        seekbar_per.setText("0%");
                        seekbarValue = 0;   break;
                    case 1:
                        seekbar_per.setText("20%");
                        seekbarValue = 1;   break;
                    case 2:
                        seekbar_per.setText("40%");
                        seekbarValue = 2;   break;
                    case 3:
                        seekbar_per.setText("60%");
                        seekbarValue = 3;   break;
                    case 4:
                        seekbar_per.setText("80%");
                        seekbarValue = 4;   break;
                    case 5:
                        seekbar_per.setText("100%");
                        seekbarValue = 5;   break;

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 시크바 처음 터치
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 시크바 터치 끝났을때
            }
        });


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_writing_frag1, container, false);
    }
}