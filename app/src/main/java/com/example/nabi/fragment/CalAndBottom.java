package com.example.nabi.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nabi.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.Date;

// 세번째 탭 안에서 달력부분과 밑에 일기내역 나오는 부분
// (이부분만 동적이어서 따로 fragment로 만듦) --> 다른 의견있으면 말해주세요

public class CalAndBottom extends Fragment {


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
                new MySelectorDecorator(this),  // 선택된 애 어떻게 꾸밀지
                new OneDayDecorator()                   // 오늘꺼 어떻게 꾸밀지
        );


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cal_and_bottom, container, false);
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
            view.addSpan(new RelativeSizeSpan(1.4f));
            view.addSpan(new ForegroundColorSpan(Color.GREEN));
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
        }
    }



}