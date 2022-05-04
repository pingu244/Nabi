package com.example.nabi.fragment.Diary;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


public class WritingFrag4 extends Fragment {

    Button btnComplete;
    EditText content_4;

    DBHelper dbHelper;
//    private SQLiteDatabase db;
    private FirebaseFirestore db;

    Integer one;
    String two,three,four,five;
    int six;
    String YMD2;

    // 프래그먼트간의 이동 위한 인스턴스 생성
    public static WritingFrag4 newInstance() {
        return new WritingFrag4();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnComplete = view.findViewById(R.id.fourth_complete);
        content_4 = getActivity().findViewById(R.id.diary_content_4);

        // 수정하는거면 값 띄우기
        if(((WritingDiary)getActivity()).q5_again != null)
            content_4.setText(((WritingDiary)getActivity()).q5_again);

        // 이전 버튼 작동 : 두번째 페이지로 이동
        Button fourth_prior = getActivity().findViewById(R.id.fourth_prior);
        fourth_prior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WritingDiary)getActivity()).replaceFragment("page3",WritingFrag3.newInstance());
            }
        });

//        dbHelper = new DBHelper(requireContext());
//        db = dbHelper.getWritableDatabase();
        db = FirebaseFirestore.getInstance();

        Calendar cal = Calendar.getInstance();
        int cYEAR = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH)+1;
        int cDay = cal.get(Calendar.DATE);
//        String YMD = (cYEAR+"/"+(cMonth+1)+"/"+cDay);
        YMD2 = ((WritingDiary)getActivity()).YMD;
        if(YMD2 == null)
            YMD2 = (cYEAR+"/"+cMonth+"/"+cDay);

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
                six = ((WritingDiary)getActivity()).weather;

                Log.v("variable",one.toString());
                Log.v("variable",two);
                Log.v("variable",three);
                Log.v("variable",four);
                Log.v("variable",five);
//                Log.v("daterrr", YMD);


//                db.execSQL("insert into diary_post (post_id, user_id, diary_title, content_1, content_2, content_3, diary_mood, diary_keyword, diary_weather,reporting_date) " +
//                        "values (?, 'jungin-2','희애의 일기','"+two+"','"+four+"','"+five+"',"+one+",'"+three+"',0,'"+YMD+"')");





//                //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("q1_mood", one);
                hashMap.put("q2_whatHappen", two);
                hashMap.put("q3_todayKeyword", three);
                hashMap.put("q4_why", four);
                hashMap.put("q5_again", five);
                hashMap.put("weather", six);

                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("diary").document(YMD2).set(hashMap, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("dataPut", "DocumentSnapshot successfully written!");
                                ((WritingDiary)getActivity()).goToResult();
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("dataPut", "Error writing document", e);
                            }
                        });

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
