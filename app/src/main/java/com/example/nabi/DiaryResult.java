package com.example.nabi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

public class DiaryResult extends AppCompatActivity {

    TextView date, mood_per, whatHappen, keywords, why, again;
    Button backToDiaryMain;
    ProgressBar mood;
    FlexboxLayout selected_keywords;

    DBHelper dbHelper;
    private SQLiteDatabase db;

    // db에서 받아온 것들 변수
    Integer diary_mood, diary_weather;
    String content_1, diary_keyword, content_2, content_3, reporting_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_result);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        
        
        date = findViewById(R.id.DiaryResult_date);
        backToDiaryMain = findViewById(R.id.DiaryResult_Back);
        mood = findViewById(R.id.DiaryResult_progressbar);
        mood_per = findViewById(R.id.DiaryResult_percent);
        whatHappen = findViewById(R.id.DiaryResult_1);
        why = findViewById(R.id.DiaryResult_2);
        again = findViewById(R.id.DiaryResult_3);
        selected_keywords = findViewById(R.id.DiaryResult_selctMood);

        // 뒤로가기
        backToDiaryMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // WritingDiary 에서 여기로 intent
        Intent receive_intent = getIntent();
        String select_date = receive_intent.getStringExtra("date");


        // db로 변수에 값 받아오기
        String sql = "select diary_mood, content_1, diary_keyword, content_2, content_3, diary_weather, reporting_date " +
                "from diary_post where reporting_date='"+select_date+"'";

        if (db != null){
            // cursor에 rawQuery문 저장
            Cursor cursor = db.rawQuery(sql, null);

            if(cursor!=null){
                while(cursor.moveToNext()){
                    diary_mood = cursor.getInt(0);
                    content_1 = cursor.getString(1);
                    diary_keyword = cursor.getString(2);
                    content_2 = cursor.getString(3);
                    content_3 = cursor.getString(4);
                    diary_weather = cursor.getInt(5);
                    reporting_date = cursor.getString(6);
                }

            }
                cursor.close();
            }
        dbHelper.close();
        db.close();





        // intent한 값으로 날짜 설정
        if(select_date != null)
            date.setText(select_date);
        else
            date.setText(reporting_date);

        // 1. 기분 어느정도인지 나타내기
//        diary_mood = 0;
        mood.setProgress(diary_mood);
        switch (diary_mood)
        {
            case 0:
                mood_per.setText("0%"); break;
            case 1:
                mood_per.setText("20%");    break;
            case 2:
                mood_per.setText("40%");    break;
            case 3:
                mood_per.setText("60%");    break;
            case 4:
                mood_per.setText("80%");    break;
            case 5:
                mood_per.setText("100%");    break;
        }

        // 2. 오늘 무슨 일이 있었나요?
        whatHappen.setText(content_1);

        // 3. 그때 느낀 감정단어
//        diary_keyword = "안희애,안녕,좀,제대로,안녕";
        String[] array = diary_keyword.split(",");
        for(int i = 0; i<array.length; i++)
            Log.v("Result", array[i]);

        for(int i = 0; i<array.length; i++)
        {
            Button mButton = new Button(this); //버튼을 선언

            FlexboxLayout.LayoutParams pm = new FlexboxLayout.LayoutParams
                    (FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            pm.setMargins(5,5,5,5);

            mButton.setText(array[i]); //버튼에 들어갈 텍스트를 지정(String)

            mButton.setBackgroundResource(R.drawable.q3_moodword_normal);

            mButton.setLayoutParams(pm); //앞서 설정한 레이아웃파라미터를 버튼에 적용
            ViewCompat.setBackgroundTintList(mButton, ColorStateList.valueOf(Color.parseColor("#000000"))); // 배경 색 지정
            mButton.setTextColor(Color.parseColor("#ffffff"));  // 글씨 색 지정

            FlexboxLayout mView = selected_keywords;
            mView.addView(mButton); //지정된 뷰에 셋팅완료된 mButton을 추가
        }


        // 4. 왜 느낌?
        why.setText(content_2);

        // 5. 다시 살아본다면?
        again.setText(content_3);




    }
}