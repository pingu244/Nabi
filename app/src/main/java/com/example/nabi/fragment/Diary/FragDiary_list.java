package com.example.nabi.fragment.Diary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nabi.R;


public class FragDiary_list extends Fragment {

    View view;
    ImageButton btnCloudy;
    ImageButton btnLittleCloudy;
    ImageButton btnClear;
    ImageButton btnRain;
    ImageButton btnSnow;
    DiaryList_Cloud cloudyDiary;
    DiaryList_LittleCloud littleCloudyDiary;
    DiaryList_Clear clearDiary;
    DiaryList_Rain rainDiary;
    DiaryList_Snow snowDiary;
    TextView tvWeather;

    int selectMonth = 1;
    int weather;
    RecyclerView diaryListView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cloudyDiary = new DiaryList_Cloud();
        littleCloudyDiary = new DiaryList_LittleCloud();
        clearDiary = new DiaryList_Clear();
        rainDiary = new DiaryList_Rain();
        snowDiary = new DiaryList_Snow();

        diaryListView = view.findViewById(R.id.diaryListRecyclerView);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_diary_list, container, false);

        btnCloudy = view.findViewById(R.id.btnCloudy);
        btnLittleCloudy = view.findViewById(R.id.btnLittleCloudy);
        btnClear = view.findViewById(R.id.btnClear);
        btnRain = view.findViewById(R.id.btnRain);
        btnSnow = view.findViewById(R.id.btnSnow);


        //맑음어리 이동
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.list_container, clearDiary).commitAllowingStateLoss();
            }
        });

        //약간 흐림어리 이동
        btnLittleCloudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.list_container, littleCloudyDiary).commitAllowingStateLoss();
            }
        });


        //흐림어리 이동
        btnCloudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.list_container, cloudyDiary).commitAllowingStateLoss();

            }
        });

        //레인어리 이동
        btnRain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.list_container, rainDiary).commitAllowingStateLoss();
            }
        });

        //스노어리 이동
        btnSnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.list_container, snowDiary).commitAllowingStateLoss();
            }
        });
        return view;
    }


}