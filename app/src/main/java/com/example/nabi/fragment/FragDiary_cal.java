package com.example.nabi.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.nabi.R;
import com.example.nabi.WritingDiary;


public class FragDiary_cal extends Fragment {

    View view;
    Button goWriting;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_frag_diary_cal, container, false);



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