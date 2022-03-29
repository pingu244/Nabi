package com.example.nabi.fragment.Diary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nabi.DiaryDataBase;
import com.example.nabi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

// 흐린날 날씨 목록 Fragment
public class DiaryList_Clear extends Fragment {
    @Nullable
    View view;
    RecyclerView diaryListView;
    DiaryListViewAdapter diaryListViewAdapter;
    ArrayList<DiaryListItem> diaryListItems = new ArrayList<DiaryListItem>();
    String selectMonth = "1";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.diarylist_adapter, container,false);
        initUI(view);
        getActivity().findViewById(R.id.diaryBg).setBackgroundResource(R.drawable.bg_clear);

        if (getArguments() != null)
            selectMonth = getArguments().getString("selectedMonth"); // 프래그먼트1에서 받아온 값 넣기

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
//        String sql = "select reporting_date, diary_keyword, diary_mood from diary_post where diary_weather = 0"; //맑은 날 일기 선택

//        DiaryDataBase db = DiaryDataBase.getInstance(getContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 파이어스토어 경로지정 + weather가 0인 문서들 가져오기
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("diary").document("2022").collection(selectMonth)
                .whereEqualTo("weather", 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //date, title 담길 배열 생성
                            ArrayList<DiaryListItem> items = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("dfdf", document.getId() + " => " + document.getData());
                                Map<String, Object> mymap = document.getData();
                                int diary_mood = Integer.parseInt(mymap.get("q1_mood").toString());
                                String diary_keyword = (String) mymap.get("q3_todayKeyword");

                                items.add(new DiaryListItem(document.getId(), diary_keyword, diary_mood));
                            }

                            // 리스트 클릭하면 그 결과화면 나오는 것
                            // 분명 더 좋은 방법이 있을텐데 나는 너무 노가다
                            diaryListViewAdapter.setOnItemClickListener(new DiaryListViewAdapter.OnItemClickListener()
                            {
                                public void onItemClick(View v, int pos)
                                {
                                    db.collection("users")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .collection("diary").document("2022").collection(selectMonth)
                                            .whereEqualTo("weather", 0)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    int i = 0;
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult())
                                                        {
                                                            if(pos==i)
                                                            {
                                                                Intent intent = new Intent(getActivity(), DiaryResult.class);
                                                                intent.putExtra("SelectedDate", "2022/"+selectMonth+"/"+document.getId());
                                                                startActivity(intent);
                                                            }
                                                            i++;
                                                        }
                                                    }

                                                    }
                                                });


                                }
                            });

                            //어댑터 연걸, 데이터셋 변경
                            diaryListViewAdapter.setItems(items);
                            diaryListViewAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("dfdf", "Error getting documents: ", task.getException());
                        }
                    }
                });

//        if (db != null){
//            // cursor에 rawQuery문 저장
//            Cursor outCursor = db.rawQuery(sql);
//
//            if(outCursor!=null){
//                recordCount = outCursor.getCount();
//
//                //date, title 담길 배열 생성
//                ArrayList<DiaryListItem> items = new ArrayList<>();
//
//                //하나하나 추가
//                for(int i=0;i<recordCount;i++){
//                    outCursor.moveToNext();
//
//                    String diaryDate = outCursor.getString(0);
//                    String keyword = outCursor.getString(1);
//                    Integer mood = outCursor.getInt(2);
//
//                    String[] date_array = diaryDate.split("/");
//                    String date_day = "";
//                    if(date_array.length>1)
//                        date_day = date_array[2];
//                    items.add(new DiaryListItem(date_day, keyword, mood));
//
//                }
//
//                // 리스트 클릭하면 그 결과화면 나오는 것
//                diaryListViewAdapter.setOnItemClickListener(new DiaryListViewAdapter.OnItemClickListener()
//                {
//                    public void onItemClick(View v, int pos)
//                    {
//                        Cursor cursor = db.rawQuery("select reporting_date from diary_post where diary_weather = 0");
//                        cursor.moveToPosition(pos);
//                        String diaryDate = cursor.getString(0);
//                        Intent intent = new Intent(getActivity(), DiaryResult.class);
//                        intent.putExtra("SelectedDate", diaryDate);
//                        startActivity(intent);
//                        cursor.close();
//                    }
//                });
//                outCursor.close();
//
//                //어댑터 연걸, 데이터셋 변경
//                diaryListViewAdapter.setItems(items);
//                diaryListViewAdapter.notifyDataSetChanged();
//
//
//            }
//        }
//        return recordCount;
    }

}
