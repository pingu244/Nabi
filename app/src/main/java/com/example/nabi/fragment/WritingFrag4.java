package com.example.nabi.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nabi.R;
import com.example.nabi.WritingDiary;


public class WritingFrag4 extends Fragment {

    Button btnComplete;
    // 프래그먼트간의 이동 위한 인스턴스 생성
    public static WritingFrag4 newInstance() {
        return new WritingFrag4();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnComplete = view.findViewById(R.id.fourth_complete);

        // 이전 버튼 작동 : 두번째 페이지로 이동
        Button fourth_prior = getActivity().findViewById(R.id.fourth_prior);
        fourth_prior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WritingDiary)getActivity()).replaceFragment("page3",WritingFrag3.newInstance());
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_writing_frag4, container, false);
    }
}