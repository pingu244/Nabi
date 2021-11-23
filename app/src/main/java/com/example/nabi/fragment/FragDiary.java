package com.example.nabi.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.nabi.R;
import com.example.nabi.WritingDiary;

// Diary 탭 fragment
public class FragDiary extends Fragment {

    @Nullable
    View view;
    ImageButton btnCloudy;
    Button goWriting;
    Fragment cloudyDiary;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_diary, container,false);

        btnCloudy = view.findViewById(R.id.btnCloudy);
        cloudyDiary = new DiaryList_Cloud();

        //흐린 날 아이콘 클릭 시 흐린 날 날씨 목록 화면으로 이동
        btnCloudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, cloudyDiary).commitAllowingStateLoss();
            }
        });

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
}