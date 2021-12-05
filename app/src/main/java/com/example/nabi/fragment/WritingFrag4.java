package com.example.nabi.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.nabi.DBHelper;
import com.example.nabi.R;
import com.example.nabi.WritingDiary;

import java.util.Calendar;


public class WritingFrag4 extends Fragment {

    Button btnComplete;
    EditText content_4;

    DBHelper dbHelper;
    private SQLiteDatabase db;

    Integer one;
    String two,three,four,five;

    // 프래그먼트간의 이동 위한 인스턴스 생성
    public static WritingFrag4 newInstance() {
        return new WritingFrag4();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnComplete = view.findViewById(R.id.fourth_complete);
        content_4 = getActivity().findViewById(R.id.diary_content_4);

        // 이전 버튼 작동 : 두번째 페이지로 이동
        Button fourth_prior = getActivity().findViewById(R.id.fourth_prior);
        fourth_prior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WritingDiary)getActivity()).replaceFragment("page3",WritingFrag3.newInstance());
            }
        });

        dbHelper = new DBHelper(requireContext());
        db = dbHelper.getWritableDatabase();

        Calendar cal = Calendar.getInstance();
        int cYEAR = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DATE);
        String YMD = (cYEAR+"년 "+(cMonth+1)+"월 "+cDay+"일");

        // '완료'버튼 눌렀을 때
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WritingDiary)getActivity()).q5_again = content_4.getText().toString();

                one = ((WritingDiary)getActivity()).q1_mood;
                two = ((WritingDiary)getActivity()).q2_whatHappen;
                three = ((WritingDiary)getActivity()).q3_todayKeyword;
                four = ((WritingDiary)getActivity()).q4_why;
                five = ((WritingDiary)getActivity()).q5_again;

                Log.v("variable",one.toString());
                Log.v("variable",two);
                Log.v("variable",three);
                Log.v("variable",four);
                Log.v("variable",five);


                db.execSQL("insert into diary_post (post_id, user_id, diary_title, content_1, content_2, content_3, diary_mood, diary_keyword, diary_weather,reporting_date) " +
                        "values (?, 'jungin-2','희애의 일기','"+two+"','"+four+"','"+five+"',"+one+",'"+three+"',0,'"+YMD+"')");

//                                db.execSQL("insert into diary_post (post_id, user_id, diary_title, content_1, content_2, content_3, diary_keyword, diary_weather,reporting_date) " +
//                        "values (?, 'jungin-2','희애의 일기','"+two+"','"+four+"','"+five+"','"+three+"',0,'"+YMD+"')");

//                db.execSQL("insert into diary_post (post_id, user_id, diary_title, " +
//                        "diary_mood, content_1, diary_keyword, content_2, content_3, diary_weather, reporting_date) " +
//                        "values (?, 'jungin-2','희애의 일기',one,'"+two+"'," +
//                        "'"+three+"','"+four+"','"+five+"',0,'"+YMD+"')");

                ((WritingDiary)getActivity()).goToResult();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_writing_frag4, container, false);
    }
}