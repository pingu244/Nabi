package com.example.nabi.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.nabi.R;


public class FragDiary_list extends Fragment {

    View view;
    ImageButton btnCloudy;
    DiaryList_Cloud cloudyDiary;

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
        cloudyDiary = new DiaryList_Cloud();


        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.list_container, cloudyDiary).commitAllowingStateLoss();

        //        흐린 날 아이콘 클릭 시 흐린 날 날씨 목록 화면으로 이동
        btnCloudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.list_container, cloudyDiary).commitAllowingStateLoss();
            }
        });



        return view;
    }
}