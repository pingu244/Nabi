package com.example.nabi.fragment.Diary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.DrawableMarginSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nabi.DBHelper;
import com.example.nabi.MainActivity;
import com.example.nabi.R;
import com.example.nabi.fragment.Remind.RemindAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static java.lang.Thread.sleep;


public class FragDiary_cal extends Fragment {

    View view;
    Button goWriting;

    private FirebaseFirestore db;

    FragDiary_cal_bottomItem bottomitem;

    String selectKeyword, selectDate;
    Integer selectMood;


    MaterialCalendarView materialCalendarView;  // 캘린더
    // materialCalendar 깃허브 주소
//    https://github.com/prolificinteractive/material-calendarview



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 달력 만들기
        materialCalendarView = (MaterialCalendarView) getView().findViewById(R.id.calendarView);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2018, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        // 안희애가 해본 월 보이는 커스텀(0404)
        SimpleDateFormat format = new SimpleDateFormat("M");
        materialCalendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                String formattedDate  = format.format(day.getDate());
                return (formattedDate+"월");
            }
        });
        // 월 헤더 글자크기, 색 설정
        materialCalendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);

        materialCalendarView.setDateSelected(CalendarDay.today(), true);    // 오늘 선택되어있게

        materialCalendarView.addDecorators(
                new FragDiary_cal.MySelectorDecorator(this),  // 선택된 애 어떻게 꾸밀지
                new FragDiary_cal.OneDayDecorator()                // 오늘꺼 어떻게 꾸밀지
        );

        // 날짜 클릭할때 작동하는 함수
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();
                String select = Year + "/" + Month + "/" + Day;

                Log.i("Year test", select);
                search(select);
            }
        });

        // 월 바꿀 때 작동하는 함수
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                // 선택한 달의 무드트래커 띄우기
                new MoodAsyncTask(date.getMonth() + 1).execute();
            }
        });

        // 처음 떴을때 오늘날짜 일기있는지 보여주기
        Calendar cal = Calendar.getInstance();
        int cYEAR = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH)+1;
        int cDay = cal.get(Calendar.DATE);
        String YMD = (cYEAR+"/"+cMonth+"/"+cDay);
        search(YMD);

        // 처음 떴을때 이번 달 무드트래커 띄우기
        new MoodAsyncTask(cMonth).execute();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_frag_diary_cal, container, false);

        getActivity().findViewById(R.id.diaryBg).setBackgroundResource(R.drawable.diarybg);

        goWriting = view.findViewById(R.id.go_writing);
        // 달력 밑에 일기쓰는 버튼 누르면 일기쓰는 액티비티인 WritingDiary를 보여줌
        goWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WritingDiary.class);
                // 오늘 날씨 정보 보내서 배경 바꾸기
                intent.putExtra("diaryWrite_todayWeather", ((MainActivity)getActivity()).diary_weather);
                Log.v("diaryWrite_todayWeather", ((MainActivity)getActivity()).diary_weather+"입니다");
                startActivity(intent);
            }
        });
        return view;
    }


    // 클릭한 날짜 일기있는지 파악해서 FragDiary_cal_bottomItem을 통해 리사이클뷰 띄우기
    private void search(String date)
    {
        selectMood = null;
        bottomitem = new FragDiary_cal_bottomItem();
        goWriting = view.findViewById(R.id.go_writing);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();


        selectDate = date;

        DocumentReference docRef = db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("diary").document(date);

        Task<DocumentSnapshot> documentSnapshotTask = docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> mymap = document.getData();
                                selectMood = Integer.parseInt(mymap.get("q1_mood").toString());
                                selectKeyword = (String) mymap.get("q3_todayKeyword");

                                if(selectMood != null)
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("selectMood",selectMood); //번들에 넘길 값 저장
                                    bundle.putString("selectKeyword",selectKeyword);
                                    bundle.putString("selectDate",selectDate);
                                    bottomitem.setArguments(bundle);

                                    goWriting.setVisibility(View.INVISIBLE);
                                }

                            }
                            else    goWriting.setVisibility(View.VISIBLE);
                            transaction.replace(R.id.Diary_cal_bottom_item, bottomitem);
                            transaction.commitAllowingStateLoss();

                        }

                    }
                });

    }



    // 오늘 표시 어떻게 할지
    public class OneDayDecorator implements DayViewDecorator {
        private CalendarDay date;
        private Drawable drawable;

        public OneDayDecorator() {
            date = CalendarDay.today();
            drawable = getResources().getDrawable(R.drawable.showtoday);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {return date != null && day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.2f));
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.addSpan(new BackgroundColorSpan(Color.rgb(250,133,116)));
            view.setBackgroundDrawable(drawable);
            view.setSelectionDrawable(drawable); //선택했을때 백그라운드 드로어블(선택 취소하면 나타났다사라짐)
        }

        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
    }

    // 선택한 것 어떻게 나오는지 설정
    public class MySelectorDecorator implements DayViewDecorator {
        private final Drawable drawable;

        public MySelectorDecorator(Fragment context) {
            drawable = context.getResources().getDrawable(R.drawable.myselector);
        }

        //true 리턴으로 모든요일에 drawable 적용
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        //일자 선택시 drawable 적용되도록
        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(drawable);
            view.addSpan(new ForegroundColorSpan(Color.GRAY)); //모든 date 색상
        }
    }

    // 무드트래커
    public class MoodDecorator implements DayViewDecorator{
        private Drawable drawable;
        Drawable drawable0 = getResources().getDrawable(R.drawable.mood_circle2);
        Drawable drawable1 = getResources().getDrawable(R.drawable.btnrain);
        Drawable drawable2 = getResources().getDrawable(R.drawable.btncloudy);
        Drawable drawable3 = getResources().getDrawable(R.drawable.btnlittlecloud);
        Drawable drawable4 = getResources().getDrawable(R.drawable.btnclear);
        Drawable drawable5 = getResources().getDrawable(R.drawable.mood_circle);

        ArrayList<String> deco_dates;
        boolean checkMood = false;
        int moodValue;

        public MoodDecorator(ArrayList<String> deco_dates, int mood){
            this.deco_dates = deco_dates;
            this.moodValue = mood;

            switch (moodValue)
            {
                case 0:
                    drawable = drawable0; break;
                case 1:
                    drawable = drawable1; break;
                case 2:
                    drawable = drawable2; break;
                case 3:
                    drawable = drawable3; break;
                case 4:
                    drawable = drawable4; break;
                case 5:
                    drawable = drawable5; break;
            }
            drawable.setBounds(0, 0, 80, 80);
        }


        @Override
        public boolean shouldDecorate(CalendarDay day) {
            int cYEAR = day.getYear();
            int cMonth = day.getMonth()+1;
            int cDay = day.getDay();
            String YMD = (cYEAR+"/"+cMonth+"/"+cDay);

            checkMood = false;
            for(int i = 0; i<deco_dates.size(); i++)
                if(deco_dates.get(i).equals(YMD))
                    checkMood = true;

            return checkMood;
        }

        @Override
        public void decorate(DayViewFacade view) {

//            view.addSpan(new ForegroundColorSpan(Color.BLACK));
//            view.setBackgroundDrawable(drawable);
            ImageSpan span = new ImageSpan(drawable,ImageSpan.ALIGN_CENTER);
            view.addSpan(span);
        }
    }



    // async로 특정 month 데베 돌아서 값 있으면 무드따라 날짜배열에 저장, onProgressUpdate에서 decorater 호출
    private class MoodAsyncTask extends AsyncTask<Void, Long, Boolean> {

        ArrayList<String> deco_mood0_dates = new ArrayList<>();
        ArrayList<String> deco_mood1_dates = new ArrayList<>();
        ArrayList<String> deco_mood2_dates = new ArrayList<>();
        ArrayList<String> deco_mood3_dates = new ArrayList<>();
        ArrayList<String> deco_mood4_dates = new ArrayList<>();
        ArrayList<String> deco_mood5_dates = new ArrayList<>();
        Integer month;

        public MoodAsyncTask(Integer month) {
            this.month = month;
        }

        @Override
        protected Boolean doInBackground(Void...voids) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("diary").document("2022").collection(month.toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> mymap = document.getData();
                                    int diary_mood = Integer.parseInt(mymap.get("q1_mood").toString());

                                    // 각 감정별로 배열에 날짜 저장
                                    switch (diary_mood)
                                    {
                                        case 0:
                                            deco_mood0_dates.add("2022/"+month+"/"+document.getId()); break;
                                        case 1:
                                            deco_mood1_dates.add("2022/"+month+"/"+document.getId()); break;
                                        case 2:
                                            deco_mood2_dates.add("2022/"+month+"/"+document.getId()); break;
                                        case 3:
                                            deco_mood3_dates.add("2022/"+month+"/"+document.getId()); break;
                                        case 4:
                                            deco_mood4_dates.add("2022/"+month+"/"+document.getId()); break;
                                        case 5:
                                            deco_mood5_dates.add("2022/"+month+"/"+document.getId()); break;
                                    }

                                }
                                // 파베 다 읽어오면 함수 실행
                                publishProgress();
                            }

                        }
                    });

            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);

            materialCalendarView.addDecorators(
                    new FragDiary_cal.MoodDecorator(deco_mood0_dates, 0),
                    new FragDiary_cal.MoodDecorator(deco_mood1_dates, 1),
                    new FragDiary_cal.MoodDecorator(deco_mood2_dates, 2),
                    new FragDiary_cal.MoodDecorator(deco_mood3_dates, 3),
                    new FragDiary_cal.MoodDecorator(deco_mood4_dates, 4),
                    new FragDiary_cal.MoodDecorator(deco_mood5_dates, 5)

            );
        }

    }


}