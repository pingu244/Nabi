package com.example.nabi.fragment.Diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nabi.DBHelper;
import com.example.nabi.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class DiaryResult extends AppCompatActivity {

    TextView date, mood_per, whatHappen, keywords, why, again;
    ImageButton backToDiaryMain, editBtn;
    ProgressBar mood;
    FlexboxLayout selected_keywords;

//    DBHelper dbHelper;
//    private SQLiteDatabase db;
    private FirebaseFirestore db;

    // db에서 받아온 것들 변수
    Integer diary_mood, diary_weather;
    String content_1, diary_keyword, content_2, content_3, finalDate, reporting_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_result);

//        dbHelper = new DBHelper(this);
//        db = dbHelper.getReadableDatabase();
        db = FirebaseFirestore.getInstance();
        
        
        date = findViewById(R.id.DiaryResult_date);
        backToDiaryMain = findViewById(R.id.DiaryResult_Back);
        mood = findViewById(R.id.DiaryResult_progressbar);
        mood_per = findViewById(R.id.DiaryResult_percent);
        whatHappen = findViewById(R.id.DiaryResult_1);
        why = findViewById(R.id.DiaryResult_2);
        again = findViewById(R.id.DiaryResult_3);
        selected_keywords = findViewById(R.id.DiaryResult_selctMood);
        editBtn = findViewById(R.id.diary_result_edit);

        // 뒤로가기
        backToDiaryMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // WritingDiary 에서 여기로 intent
        Intent receive_intent = getIntent();
        String diary_writing_date = receive_intent.getStringExtra("Diary_WritingResult");
        // 목록에서 선택한다면 여기로 intent
        String selected_date = receive_intent.getStringExtra("SelectedDate");

        // intent한 값으로 날짜 설정
        if(diary_writing_date != null)
            finalDate = diary_writing_date;
        else
            finalDate = selected_date;
        String[] date_array = finalDate.split("/");
        date.setText(date_array[0]+"년 "+date_array[1]+"월 "+date_array[2]+"일");





        // db로 변수에 값 받아오기
//        String sql = "select diary_mood, content_1, diary_keyword, content_2, content_3, diary_weather, reporting_date " +
//                "from diary_post where reporting_date='"+finalDate+"'";

//        if (db != null){
//            // cursor에 rawQuery문 저장
//            Cursor cursor = db.rawQuery(sql, null);
//
//            if(cursor!=null){
//                while(cursor.moveToNext()){
//                    diary_mood = cursor.getInt(0);
//                    content_1 = cursor.getString(1);
//                    diary_keyword = cursor.getString(2);
//                    content_2 = cursor.getString(3);
//                    content_3 = cursor.getString(4);
//                    diary_weather = cursor.getInt(5);
//                    reporting_date = cursor.getString(6);
//                }
//
//            }
//                cursor.close();
//            }
//        dbHelper.close();
//        db.close();


        // 파이어스토어 경로지정
        DocumentReference docRef = db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("diary").document(finalDate);

        // document가져오는 리스너
        Task<DocumentSnapshot> documentSnapshotTask = docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("dataOutput", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> mymap = document.getData();
                        diary_mood = Integer.parseInt(mymap.get("q1_mood").toString());
                        content_1 = (String) mymap.get("q2_whatHappen");
                        diary_keyword = (String) mymap.get("q3_todayKeyword");
                        content_2 = (String) mymap.get("q4_why");
                        content_3 = (String) mymap.get("q5_again");
                        diary_weather = Integer.parseInt(mymap.get("weather").toString());

                        // 0. 날씨따라 배경 바꾸기
                        switch (diary_weather)
                        {
                            case 0:
                                findViewById(R.id.bg_diary).setBackgroundResource(R.drawable.diary_sunny_background);
                                break;
                            case 1:
                                findViewById(R.id.bg_diary).setBackgroundResource(R.drawable.diary_halfcloudy_background);
                                break;
                            case 2:
                                findViewById(R.id.bg_diary).setBackgroundResource(R.drawable.diary_cloudy_background);
                                break;
                            case 3:
                                findViewById(R.id.bg_diary).setBackgroundResource(R.drawable.diary_rainy_background);
                                break;
                            case 4:
                                findViewById(R.id.bg_diary).setBackgroundResource(R.drawable.diary_snowy_background);
                                break;
                        }



                        // 1. 기분 어느정도인지 나타내기
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
                        String[] array = diary_keyword.split(",");
                        for(int i = 0; i<array.length; i++)
                            Log.v("Result", array[i]);

                        for(int i = 0; i<array.length; i++)
                        {
                            TextView txt = new TextView(DiaryResult.this);

                            FlexboxLayout.LayoutParams pm = new FlexboxLayout.LayoutParams
                                    (FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);

                            DisplayMetrics dm = getResources().getDisplayMetrics(); // 단위를 dp로 맞춰주기 위함
                            pm.setMargins(Math.round(5*dm.density),Math.round(5*dm.density),0,Math.round(5*dm.density));
                            txt.setPadding(Math.round(12*dm.density),Math.round(6*dm.density),Math.round(12*dm.density),Math.round(6*dm.density));

                            txt.setText(array[i]); //버튼에 들어갈 텍스트를 지정(String)
                            txt.setTextSize(13);

                            txt.setBackgroundResource(R.drawable.q3_moodword_normal);

                            txt.setLayoutParams(pm); //앞서 설정한 레이아웃파라미터를 버튼에 적용
                            ViewCompat.setBackgroundTintList(txt, ColorStateList.valueOf(Color.parseColor("#686868"))); // 배경 색 지정
                            txt.setTextColor(Color.parseColor("#ffffff"));  // 글씨 색 지정

                            FlexboxLayout mView = selected_keywords;
                            mView.addView(txt); //지정된 뷰에 셋팅완료된 mButton을 추가
                        }


                        // 4. 왜 느낌?
                        why.setText(content_2);

                        // 5. 다시 살아본다면?
                        again.setText(content_3);

                        // 수정하기
                        editBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(DiaryResult.this, WritingDiary.class);

                                intent.putExtra("EditDiary_date", finalDate);
                                intent.putExtra("EditDiary_q1", diary_mood);
                                intent.putExtra("EditDiary_q2", content_1);
                                intent.putExtra("EditDiary_q3", diary_keyword);
                                intent.putExtra("EditDiary_q4", content_2);
                                intent.putExtra("EditDiary_q5", content_3);
                                intent.putExtra("EditDiary_weather", diary_weather);

                                startActivity(intent);
                                finish();

                            }
                        });

                    } else {
                        Log.d("dataOutput", "No such document");
                    }
                } else {
                    Log.d("dataOutput", "get failed with ", task.getException());
                }
            }
        });




    }
}