package com.example.nabi.fragment.Diary;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nabi.DBHelper;
import com.example.nabi.MainActivity;
import com.example.nabi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;


public class FragDiary_cal extends Fragment {

    View view;
    Button goWriting;

    DBHelper dbHelper;
//    private SQLiteDatabase db;
    private FirebaseFirestore db;

    FragDiary_cal_bottomItem bottomitem;

    String selectKeyword, selectDate;
    Integer selectMood, moodValue = null;
//    boolean checkMood;


    MaterialCalendarView materialCalendarView;  // 캘린더




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

        //월 한글로 보이게 설정
//        materialCalendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));

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

        // 처음 떴을때 오늘날짜 일기있는지 보여주기
        Calendar cal = Calendar.getInstance();
        int cYEAR = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH)+1;
        int cDay = cal.get(Calendar.DATE);
        String YMD = (cYEAR+"/"+cMonth+"/"+cDay);
        search(YMD);


        // 무드트래커를 향한 나의 몸부림
        db = FirebaseFirestore.getInstance();
        Calendar calendar = Calendar.getInstance();


        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH)+1;

        CollectionReference docRef = db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("diary");


        Task<QuerySnapshot> documentSnapshotTask = docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        CalendarDay dayy = null;
                        Log.v("tellmewhattodo", "첫번째 입니다");
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> mmap = document.getData();
                                String d = document.getId();
                                Log.v("tellmewhattodo", d+"일 입니다");
                                moodValue = Integer.parseInt(mmap.get("q1_mood").toString());
                                dayy.from(y,m,Integer.parseInt(d));
                                materialCalendarView.addDecorators(new MoodDecorator(dayy, moodValue));
                            }
                        } else {
                            Log.d("EEE", "Error getting documents: ", task.getException());
                        }
                    }
                });


//        calendarDayList.add(CalendarDay.today());
//        calendarDayList.add(CalendarDay.from(2020, 11, 25));

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
//        dbHelper = new DBHelper(getContext());
//        db = dbHelper.getReadableDatabase();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();


//        String sql = "select diary_keyword, diary_mood from diary_post where reporting_date = '"+selectDate+"'";
//
//        if (db != null){
//            // cursor에 rawQuery문 저장
//            Cursor cursor = db.rawQuery(sql, null);
//
//            if(cursor!=null){
//                while(cursor.moveToNext()){
//                    selectKeyword = cursor.getString(0);
//                    selectMood = cursor.getInt(1);
//                }
//            }
//            cursor.close();
//        }
//        dbHelper.close();
//        db.close();

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
                                    bundle.putInt("selectMood",selectMood);//번들에 넘길 값 저장
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
        private Drawable drawable0 = getResources().getDrawable(R.drawable.btnsnow);
        private Drawable drawable1 = getResources().getDrawable(R.drawable.btnrain);
        private Drawable drawable2 = getResources().getDrawable(R.drawable.btnclear);
        private Drawable drawable3 = getResources().getDrawable(R.drawable.btncloudy);
        private Drawable drawable4 = getResources().getDrawable(R.drawable.btnclear);
        private Drawable drawable5 = getResources().getDrawable(R.drawable.btnclear);

        private CalendarDay date;
        private final Calendar calendar = Calendar.getInstance();
//        private HashSet<CalendarDay> dates;
        boolean checkMood;
        int mood;





        public MoodDecorator(CalendarDay dates, int moodValue) {

            this.date = dates;
            mood = moodValue;
////            int cYEAR = calendar.get(Calendar.YEAR);
////            int cMonth = calendar.get(Calendar.MONTH)+1;
////            int cDay = calendar.get(Calendar.DATE);
////            String YMD = (cYEAR+"/"+cMonth+"/"+cDay);
////
////            Log.v("whatyourfavoriteidea", YMD + " 입니다");
//
//            switch (moodValue){
//                case 0:
//                    drawable1 = getResources().getDrawable(R.drawable.btnclear); break;
//                case 1:
//                    drawable1 = getResources().getDrawable(R.drawable.btncloudy); break;
//                case 2:
//                    drawable1 = getResources().getDrawable(R.drawable.img); break;
//                case 3:
//                    drawable1 = getResources().getDrawable(R.drawable.img); break;
//                case 4:
//                    drawable1 = getResources().getDrawable(R.drawable.img); break;
//                case 5:
//                    drawable1 = getResources().getDrawable(R.drawable.img); break;
//                default:
//                    drawable1 = getResources().getDrawable(R.drawable.mood_circle2); break;
//
//            };




        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {



//            int cYEAR = day.getYear();
//            int cMonth = day.getMonth()+1;
//            int cDay = day.getDay();
//            String YMD = (cYEAR+"/"+cMonth+"/"+cDay);
//
//            Log.v("whatyourfavoriteidea", YMD + " 입니다");
//            FirebaseFirestore db = FirebaseFirestore.getInstance();

//            try {
//                DocumentReference docRef = db.collection("users")
//                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .collection("diary").document(YMD);
//
//                Task<DocumentSnapshot> documentSnapshotTask = docRef.get()
//                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    DocumentSnapshot document = task.getResult();
//                                    if (document.exists()) {
//                                        Map<String, Object> mymap = document.getData();
//                                        moodValue = Integer.parseInt(mymap.get("q1_mood").toString());
//
//                                        checkMood = true;
//
//                                        Log.v("really?", YMD+"이고! "+moodValue+" : "+checkMood);
//// 시도해봐야할것 : 여기말고 위에서 생성자 부르기 전에 db불러서 exist하면 arrayList에 넣기, 생성자에서 drawable설정
//                                    }
//                                    else
//                                        checkMood = false;
//                                }
//
//                            }
//                        });
//            }
//            catch (NullPointerException e){ e.printStackTrace(); }


//            Log.v("really?", YMD+"이고! "+checkMood);
//            return checkMood;
            return date.equals(day);
//            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            switch (mood)
            {
                case 0:
                    view.setBackgroundDrawable(drawable0); break;
                case 1:
                    view.setBackgroundDrawable(drawable1); break;
                case 2:
                    view.setBackgroundDrawable(drawable2); break;
                case 3:
                    view.setBackgroundDrawable(drawable3); break;
                case 4:
                    view.setBackgroundDrawable(drawable4); break;
                case 5:
                    view.setBackgroundDrawable(drawable5); break;
                default:
                    view.setBackgroundDrawable(drawable0);
            }
//            view.setBackgroundDrawable(drawable1);
        }
    }
}