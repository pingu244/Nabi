package com.example.nabi.fragment.Diary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.google.android.material.internal.ForegroundLinearLayout;
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


    MaterialCalendarView materialCalendarView;  // ?????????
    // materialCalendar ????????? ??????
//    https://github.com/prolificinteractive/material-calendarview



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ?????? ?????????
        materialCalendarView = (MaterialCalendarView) getView().findViewById(R.id.calendarView);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2018, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        // ???????????? ?????? ??? ????????? ?????????(0404)
        SimpleDateFormat format = new SimpleDateFormat("M");
        materialCalendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                String formattedDate  = format.format(day.getDate());
                return (formattedDate+"???");
            }
        });
        // ??? ?????? ????????????, ??? ??????
        materialCalendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);

        materialCalendarView.setDateSelected(CalendarDay.today(), true);    // ?????? ??????????????????

        materialCalendarView.addDecorators(
                new FragDiary_cal.MySelectorDecorator(this),
                new EveryDayDecorator1(),
                new EveryDayDecorator2(),
                new EveryDayDecorator3(),
                new EveryDayDecorator4(),
                new EveryDayDecorator5(),
                new EveryDayDecorator6(),
                new EveryDayDecorator7(),
                new EveryDayDecorator8(),
                new EveryDayDecorator9(),
                new EveryDayDecorator10(),
                new EveryDayDecorator11(),
                new EveryDayDecorator12(),
                new EveryDayDecorator13(),
                new EveryDayDecorator14(),
                new EveryDayDecorator15(),
                new EveryDayDecorator16(),
                new EveryDayDecorator17(),
                new EveryDayDecorator18(),
                new EveryDayDecorator19(),
                new EveryDayDecorator20(),
                new EveryDayDecorator21(),
                new EveryDayDecorator22(),
                new EveryDayDecorator23(),
                new EveryDayDecorator24(),
                new EveryDayDecorator25(),
                new EveryDayDecorator26(),
                new EveryDayDecorator27(),
                new EveryDayDecorator28(),
                new EveryDayDecorator29(),
                new EveryDayDecorator30(),
                new EveryDayDecorator31(),
                // ????????? ????????? ?????????
                new FragDiary_cal.OneDayDecorator(),
                new EveryDayDecorator()
        );


        // ?????? ???????????? ???????????? ??????
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();
                String select = Year + "/" + Month + "/" + Day;

                search(select);
            }
        });


        Calendar cal = Calendar.getInstance();
        // ??? ?????? ??? ???????????? ??????
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                // ????????? ?????? ??????????????? ?????????
                new MoodAsyncTask(date.getMonth() + 1).execute();
            }

        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_frag_diary_cal, container, false);

        getActivity().findViewById(R.id.diaryBg).setBackgroundResource(R.drawable.diarybg);

        goWriting = view.findViewById(R.id.go_writing);
        // ?????? ?????? ???????????? ?????? ????????? ???????????? ??????????????? WritingDiary??? ?????????
        goWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WritingDiary.class);
                // ?????? ?????? ?????? ????????? ?????? ?????????
                intent.putExtra("diaryWrite_todayWeather", ((MainActivity)getActivity()).diary_weather);
                Log.v("diaryWrite_todayWeather", ((MainActivity)getActivity()).diary_weather+"?????????");
                startActivity(intent);
            }
        });
        return view;
    }



    // ????????????
    @Override
    public void onStart() {
        super.onStart();

        // ?????? ????????????????????? ???????????? ????????? ?????????
        Calendar cal = Calendar.getInstance();
        int cYEAR = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH)+1;
        int cDay = cal.get(Calendar.DATE);
        String YMD = (cYEAR+"/"+cMonth+"/"+cDay);
        if(materialCalendarView.getSelectedDate().getYear()==cYEAR &&
                materialCalendarView.getSelectedDate().getMonth()==cMonth-1 &&
                materialCalendarView.getSelectedDate().getDay()==cDay)
            search(YMD);

        // ?????? ????????? ?????? ??? ??????????????? ?????????
        new MoodAsyncTask(cMonth).execute();
    }




    // ????????? ?????? ??????????????? ???????????? FragDiary_cal_bottomItem??? ?????? ??????????????? ?????????
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
                                try{
                                    Map<String, Object> mymap = document.getData();
                                    selectMood = Integer.parseInt(mymap.get("q1_mood").toString());
                                    selectKeyword = (String) mymap.get("q3_todayKeyword");

                                    if(selectMood != null)
                                    {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("selectMood",selectMood); //????????? ?????? ??? ??????
                                        bundle.putString("selectKeyword",selectKeyword);
                                        bundle.putString("selectDate",selectDate);
                                        bottomitem.setArguments(bundle);

                                        goWriting.setVisibility(View.INVISIBLE);
                                    }
                                }
                                catch (Exception e){
                                    goWriting.setVisibility(View.VISIBLE);
                                }


                            }
                            else    goWriting.setVisibility(View.VISIBLE);
                            transaction.replace(R.id.Diary_cal_bottom_item, bottomitem);
                            transaction.commitAllowingStateLoss();

                        }

                    }
                });

    }


    public class EveryDayDecorator implements DayViewDecorator{

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.TRANSPARENT));
        }
    }


    // ?????? ?????? ????????? ??????
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
            view.addSpan(new EveryDaySpan(getContext(),date.getDay(),true));
            view.setBackgroundDrawable(drawable);
        }

        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
    }

    // ????????? ??? ????????? ???????????? ??????
    public class MySelectorDecorator implements DayViewDecorator {
        private final Drawable drawable;

        public MySelectorDecorator(Fragment context) {
            drawable = context.getResources().getDrawable(R.drawable.myselector);
        }

        //true ???????????? ??????????????? drawable ??????
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        //?????? ????????? drawable ???????????????
        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(drawable);
            view.addSpan(new ForegroundColorSpan(Color.GRAY)); //?????? date ??????
        }
    }

    // ???????????????
    public class MoodDecorator implements DayViewDecorator{

        ArrayList<String> deco_dates;
        boolean checkMood = false;
        int moodValue;

        public MoodDecorator(ArrayList<String> deco_dates, int mood){
            this.deco_dates = deco_dates;
            this.moodValue = mood;
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

            view.addSpan(new CustomSpan(getContext(),0, false, moodValue));
        }
    }



    // async??? ?????? month ?????? ????????? ??? ????????? ???????????? ??????????????? ??????, onProgressUpdate?????? decorater ??????
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
                                    int diary_mood = -1;
                                    try{
                                        diary_mood = Integer.parseInt(mymap.get("q1_mood").toString());
                                    }catch (Exception e){}

                                    // ??? ???????????? ????????? ?????? ??????
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
                                // ?????? ??? ???????????? ?????? ??????
                                publishProgress();
                            }

                        }
                    });

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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












    // ??? ?????? ????????????
    public class EveryDayDecorator1 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 1)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),1,false));
        }
    }
    public class EveryDayDecorator2 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 2)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),2,false));
        }
    }
    public class EveryDayDecorator3 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 3)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),3,false));
        }
    }
    public class EveryDayDecorator4 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 4)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),4,false));
        }
    }
    public class EveryDayDecorator5 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 5)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),5,false));
        }
    }
    public class EveryDayDecorator6 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 6)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),6,false));
        }
    }
    public class EveryDayDecorator7 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 7)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),7,false));
        }
    }
    public class EveryDayDecorator8 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 8)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),8,false));
        }
    }
    public class EveryDayDecorator9 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 9)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),9,false));
        }
    }
    public class EveryDayDecorator10 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 10)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),10,false));
        }
    }
    public class EveryDayDecorator11 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 11)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),11,false));
        }
    }
    public class EveryDayDecorator12 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 12)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),12,false));
        }
    }
    public class EveryDayDecorator13 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 13)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),13,false));
        }
    }
    public class EveryDayDecorator14 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 14)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),14,false));
        }
    }
    public class EveryDayDecorator15 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 15)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),15,false));
        }
    }
    public class EveryDayDecorator16 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 16)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),16,false));
        }
    }
    public class EveryDayDecorator17 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 17)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),17,false));
        }
    }
    public class EveryDayDecorator18 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 18)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),18,false));
        }
    }
    public class EveryDayDecorator19 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 19)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),19,false));
        }
    }
    public class EveryDayDecorator20 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 20)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),20,false));
        }
    }
    public class EveryDayDecorator21 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 21)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),21,false));
        }
    }
    public class EveryDayDecorator22 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 22)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),22,false));
        }
    }
    public class EveryDayDecorator23 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 23)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),23,false));
        }
    }
    public class EveryDayDecorator24 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 24)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),24,false));
        }
    }
    public class EveryDayDecorator25 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 25)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),25,false));
        }
    }
    public class EveryDayDecorator26 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 26)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),26,false));
        }
    }
    public class EveryDayDecorator27 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 27)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),27,false));
        }
    }
    public class EveryDayDecorator28 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 28)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),28,false));
        }
    }
    public class EveryDayDecorator29 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 29)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),29,false));
        }
    }
    public class EveryDayDecorator30 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 30)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),30,false));
        }
    }
    public class EveryDayDecorator31 implements DayViewDecorator{
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(day.getDay() == 31)
                return true;
            else
                return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new EveryDaySpan(getContext(),31,false));
        }
    }


}