package com.example.nabi.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.nabi.DBHelper;
import com.example.nabi.R;
import com.example.nabi.WritingDiary;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;


public class FragDiary_cal extends Fragment {

    View view;
    Button goWriting;

    DBHelper dbHelper;
    private SQLiteDatabase db;

    FragDiary_cal_bottomItem bottomitem;

    String selectKeyword, selectDate;
    Integer selectMood;


    MaterialCalendarView materialCalendarView;  // 캘린더




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 달력 만들기
        materialCalendarView = (MaterialCalendarView) getView().findViewById(R.id.calendarView);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();


        materialCalendarView.setDateSelected(CalendarDay.today(), true);    // 오늘 선택되어있게

        materialCalendarView.addDecorators(
                new FragDiary_cal.MySelectorDecorator(this),  // 선택된 애 어떻게 꾸밀지
                new FragDiary_cal.OneDayDecorator()                   // 오늘꺼 어떻게 꾸밀지
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
        dbHelper = new DBHelper(getContext());
        db = dbHelper.getReadableDatabase();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();


        selectDate = date;

        String sql = "select diary_keyword, diary_mood from diary_post where reporting_date = '"+selectDate+"'";

        if (db != null){
            // cursor에 rawQuery문 저장
            Cursor cursor = db.rawQuery(sql, null);

            if(cursor!=null){
                while(cursor.moveToNext()){
                    selectKeyword = cursor.getString(0);
                    selectMood = cursor.getInt(1);
                }
            }
            cursor.close();
        }
        dbHelper.close();
        db.close();

        if(selectMood != null)
        {
            Bundle bundle = new Bundle();
            bundle.putInt("selectMood",selectMood);//번들에 넘길 값 저장
            bundle.putString("selectKeyword",selectKeyword);
            bundle.putString("selectDate",selectDate);
            bottomitem.setArguments(bundle);

            goWriting.setVisibility(View.INVISIBLE);
        }
        else
            goWriting.setVisibility(View.VISIBLE);

        transaction.replace(R.id.Diary_cal_bottom_item, bottomitem);
        transaction.commitAllowingStateLoss();
    }



    // 오늘 표시 어떻게 할지
    public class OneDayDecorator implements DayViewDecorator {
        private CalendarDay date;

        public OneDayDecorator() {
            date = CalendarDay.today();
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return date != null && day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.5f));
            view.addSpan(new ForegroundColorSpan(Color.BLACK));
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

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(drawable);
            view.addSpan(new ForegroundColorSpan(Color.BLACK));
        }
    }
}