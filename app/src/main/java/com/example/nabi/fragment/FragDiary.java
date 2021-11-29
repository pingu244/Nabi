package com.example.nabi.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

    Fragment fragdiary = new FragDiary_cal();
    Fragment fraglist = new FragDiary_list();

    Button goToCal, goToList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_diary, container,false);

        goToCal = view.findViewById(R.id.goCal);
        goToList = view.findViewById(R.id.goList);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Diary_frag, fragdiary).commit();


        goToCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Diary_frag, fragdiary).commit();
            }
        });
        goToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Diary_frag, fraglist).commit();
            }
        });

        return view;
    }
}