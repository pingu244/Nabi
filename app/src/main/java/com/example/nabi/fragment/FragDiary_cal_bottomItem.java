package com.example.nabi.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nabi.DiaryDataBase;
import com.example.nabi.DiaryListItem;
import com.example.nabi.DiaryListViewAdapter;
import com.example.nabi.DiaryResult;
import com.example.nabi.R;

import java.util.ArrayList;

// FragDiary_cal 밑부분 (버튼/ 오늘의 일기)
// 선택한 날짜에 일기 정보가 있으면 띄우고 없으면 안띄우기
public class FragDiary_cal_bottomItem extends Fragment {

    @Nullable
    View view;
    RecyclerView diaryListView;
    DiaryListViewAdapter diaryListViewAdapter;
    ArrayList<DiaryListItem> diaryListItems = new ArrayList<DiaryListItem>();

    String sltDate, sltKeyword;
    Integer sltMood;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.diarylist_adapter, container,false);
        initUI(view);


        if (getArguments() != null)
        {
            sltDate = getArguments().getString("selectDate");
            sltKeyword = getArguments().getString("selectKeyword");
            sltMood = getArguments().getInt("selectMood");
            Log.v("sltDate",sltDate);
        }

        loadNoteListData();


        return view;
    }
    private void initUI(View view){
        diaryListView = view.findViewById(R.id.diaryListRecyclerView); // diarylist_adapter.xml에서 만든 리사이클러뷰 연결

        // 리니어레이아웃메니저를 이용하여 리니어레이아웃에 recyclerView를 붙임.
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        diaryListView.setLayoutManager(layoutManager);

        // 어댑터 연결
        diaryListViewAdapter = new DiaryListViewAdapter();
        diaryListView.setAdapter(diaryListViewAdapter);
    }

    public void loadNoteListData(){

        DiaryDataBase db = DiaryDataBase.getInstance(getContext());

        if (sltDate != null){

                //date, title 담길 배열 생성
                ArrayList<DiaryListItem> items = new ArrayList<>();

                String[] date_array = sltDate.split("/");
                String date_day = "";
                if(date_array.length>1)
                    date_day = date_array[2];
                items.add(new DiaryListItem(date_day, sltKeyword, sltMood));

                // 리스트 클릭하면 그 결과화면 나오는 것
                diaryListViewAdapter.setOnItemClickListener(new DiaryListViewAdapter.OnItemClickListener()
                {
                    public void onItemClick(View v, int pos)
                    {
                        Intent intent = new Intent(getActivity(), DiaryResult.class);
                        intent.putExtra("SelectedDate", sltDate);
                        startActivity(intent);
                    }
                });

                //어댑터 연걸, 데이터셋 변경
                diaryListViewAdapter.setItems(items);

        }diaryListViewAdapter.notifyDataSetChanged();

    }
}