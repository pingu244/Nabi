package com.example.nabi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nabi.DiaryListItem;
import com.example.nabi.DiaryListViewAdapter;
import com.example.nabi.R;

import java.util.ArrayList;

// 흐린날 날씨 목록 Fragment
public class DiaryList_Cloud extends Fragment {
    @Nullable
    View view;
    RecyclerView diaryListView;
    DiaryListViewAdapter diaryListViewAdapter;
    ArrayList<DiaryListItem> diaryListItems;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.diarylist_adapter, container,false);
        diaryListView = view.findViewById(R.id.diaryListRecyclerView);
        diaryListViewAdapter = new DiaryListViewAdapter();

        diaryListItems = new ArrayList<>();

        //테스트용
        for(int i=1;i<=20;i++){
            diaryListItems.add(new DiaryListItem("23","일기 목록 테스트"));
        }
        diaryListViewAdapter.setDiaryList(diaryListItems);

        diaryListView.setAdapter(diaryListViewAdapter);
        diaryListView.setLayoutManager(new LinearLayoutManager(getContext()));




        return view;
    }


}
