package com.example.nabi.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nabi.DBHelper;
import com.example.nabi.DiaryDataBase;
import com.example.nabi.DiaryListItem;
import com.example.nabi.DiaryListViewAdapter;
import com.example.nabi.R;

import java.util.ArrayList;
import java.util.Calendar;

// 흐린날 날씨 목록 Fragment
public class DiaryList_Cloud extends Fragment {
    @Nullable
    View view;
    RecyclerView diaryListView;
    DiaryListViewAdapter diaryListViewAdapter;
    ArrayList<DiaryListItem> diaryListItems = new ArrayList<DiaryListItem>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.diarylist_adapter, container,false);
        initUI(view);

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        //String YMD = (cYear+"년 "+(cMonth+1)+"월 "+cDay+"일").toString();

        loadNoteListData();



        //diaryListItems.add(new DiaryListItem("3","일기"));

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

    public int loadNoteListData(){
        String sql = "select reporting_date, content_1 from diary_post where diary_weather = 0"; //흐린 날 일기 선택

        int recordCount = 0;

        DiaryDataBase db = DiaryDataBase.getInstance(getContext());

        if (db != null){
            // cursor에 rawQuery문 저장
            Cursor outCursor = db.rawQuery(sql);

            if(outCursor!=null){
                recordCount = outCursor.getCount();

                //date, title 담길 배열 생성
                ArrayList<DiaryListItem> items = new ArrayList<>();

                //하나하나 추가
                for(int i=0;i<recordCount;i++){
                    outCursor.moveToNext();

                    String diaryDate = outCursor.getString(0);
                    String title = outCursor.getString(1);
                    items.add(new DiaryListItem(diaryDate, title));
                }
                outCursor.close();

                //어댑터 연걸, 데이터셋 변경
                diaryListViewAdapter.setItems(items);
                diaryListViewAdapter.notifyDataSetChanged();

            }
        }
        return recordCount;
    }

}
