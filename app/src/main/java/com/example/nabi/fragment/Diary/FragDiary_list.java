package com.example.nabi.fragment.Diary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner monthSpinner = getActivity().findViewById(R.id.spinner_month);

        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_date_month, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 선택되면
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않은 상태일 때
            }
        });
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

        tvWeather = view.findViewById(R.id.tvWeather);

        cloudyDiary = new DiaryList_Cloud();
        littleCloudyDiary = new DiaryList_LittleCloud();
        clearDiary = new DiaryList_Clear();
        rainDiary = new DiaryList_Rain();
        snowDiary = new DiaryList_Snow();



        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.list_container, clearDiary).commitAllowingStateLoss();

        //흐림어리 이동
        btnCloudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.list_container, cloudyDiary).commitAllowingStateLoss();
                tvWeather.setText("흐린 날");

            }
        });

        //약간 흐림어리 이동
        btnLittleCloudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.list_container, littleCloudyDiary).commitAllowingStateLoss();
                tvWeather.setText("약간 흐린 날");

            }
        });

        //맑음어리 이동
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.list_container, clearDiary).commitAllowingStateLoss();
                tvWeather.setText("맑은 날");


            }
        });

        //레인어리 이동
        btnRain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.list_container, rainDiary).commitAllowingStateLoss();
                tvWeather.setText("비 오는 날");

            }
        });

        //스노어리 이동
        btnSnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.list_container, snowDiary).commitAllowingStateLoss();
                tvWeather.setText("눈 오는 날");

            }
        });
        return view;
    }
}