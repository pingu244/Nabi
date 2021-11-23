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

// 일기 맨 처음 페이지

public class WritingFrag1 extends Fragment {

    // fragment에서 fragment로 이동하기 위한 장치
    public static WritingFrag1 newInstance() {
        return new WritingFrag1();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 첫번째 페이지에 있는 '다음'버튼이어서 이름을 이렇게 지음 (페이지번째_next)
        // '다음'버튼을 누르면 다음 fragment로 넘어감
        Button first_next = getActivity().findViewById(R.id.first_next);
        first_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WritingDiary)getActivity()).replaceFragment(WritingFrag2.newInstance());

            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_writing_frag1, container, false);
    }
}