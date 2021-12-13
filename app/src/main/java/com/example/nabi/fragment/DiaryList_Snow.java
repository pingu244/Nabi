package com.example.nabi.fragment;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nabi.DBHelper;
import com.example.nabi.DiaryDataBase;
import com.example.nabi.DiaryListItem;
import com.example.nabi.DiaryListViewAdapter;
import com.example.nabi.DiaryResult;
import com.example.nabi.R;

import java.util.ArrayList;
import java.util.Calendar;

// 흐린날 날씨 목록 Fragment
public class DiaryList_Snow extends Fragment {
    @Nullable
    View view;
    RecyclerView diaryListView;
    DiaryListViewAdapter diaryListViewAdapter;
    ArrayList<DiaryListItem> diaryListItems = new ArrayList<DiaryListItem>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.diarylist_adapter, container,false);
        initUI(view);
        getActivity().findViewById(R.id.diaryBg).setBackgroundResource(R.drawable.bg_snow);
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

    public int loadNoteListData(){
        String sql = "select reporting_date, diary_keyword, diary_mood from diary_post where diary_weather = 4"; //눈 오는 날 일기 선택

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
                    String keyword = outCursor.getString(1);
                    Integer mood = outCursor.getInt(2);

                    String[] date_array = diaryDate.split("/");
                    String date_day = "";
                    if(date_array.length>1)
                        date_day = date_array[2];
                    items.add(new DiaryListItem(date_day, keyword, mood));


                }

                // 리스트 클릭하면 그 결과화면 나오는 것
                diaryListViewAdapter.setOnItemClickListener(new DiaryListViewAdapter.OnItemClickListener()
                {
                    public void onItemClick(View v, int pos)
                    {
                        Cursor cursor = db.rawQuery("select reporting_date from diary_post where diary_weather = 4");
                        cursor.moveToPosition(pos);
                        String diaryDate = cursor.getString(0);
                        Intent intent = new Intent(getActivity(), DiaryResult.class);
                        intent.putExtra("SelectedDate", diaryDate);
                        startActivity(intent);
                        cursor.close();
                    }
                });
                outCursor.close();

                //어댑터 연걸, 데이터셋 변경
                diaryListViewAdapter.setItems(items);
                diaryListViewAdapter.notifyDataSetChanged();


            }
        }
        return recordCount;
    }

}
