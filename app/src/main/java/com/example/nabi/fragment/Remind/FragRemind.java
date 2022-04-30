package com.example.nabi.fragment.Remind;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nabi.R;
import com.example.nabi.fragment.Diary.DiaryListItem;
import com.example.nabi.fragment.Healing.MusicListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FragRemind extends Fragment {

    View view;
    RecyclerView remindList;
    RemindAdapter remindListAdapter;
    ArrayList<RemindAdapter.RemindItem> remind_itemData;
    Spinner monthSpinner;
    Integer selectMonth = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_remind, container,false);

        remindList = view.findViewById(R.id.remind_list);
        monthSpinner = view.findViewById(R.id.remindSpinner);




        // 어댑터 연결
        remind_itemData = new ArrayList<>();
        remindListAdapter =new RemindAdapter(remind_itemData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        remindList.setLayoutManager(linearLayoutManager);
        remindList.setAdapter(remindListAdapter);



        // 스피너 연결
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_date_month, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 선택되면
                Toast.makeText(getContext(), (i+1)+"월", Toast.LENGTH_SHORT).show();
                selectMonth = i+1;
                refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않은 상태일 때
            }
        });



        Log.v("remindlist",selectMonth+"월");



        return view;
    }

    private void refresh()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("diary").document("2022").collection(selectMonth.toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //date, title 담길 배열 생성
                            ArrayList<RemindAdapter.RemindItem> items = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> mymap = document.getData();
                                int diary_mood = Integer.parseInt(mymap.get("q1_mood").toString());
                                String diary_keyword = (String) mymap.get("q3_todayKeyword");
                                int weather = Integer.parseInt(mymap.get("weather").toString());

                                Drawable drawable;

                                switch (weather)
                                {
                                    case 0:
                                        drawable = getResources().getDrawable(R.drawable.btnclear);
                                        break;
                                    case 1:
                                        drawable = getResources().getDrawable(R.drawable.btnlittlecloud);
                                        break;
                                    case 2:
                                        drawable = getResources().getDrawable(R.drawable.btncloudy);
                                        break;
                                    case 3:
                                        drawable = getResources().getDrawable(R.drawable.btnrain);
                                        break;
                                    case 4:
                                        drawable = getResources().getDrawable(R.drawable.btnsnow);
                                        break;
                                    default:
                                        drawable = getResources().getDrawable(R.drawable.btnclear);
                                }

                                items.add(new RemindAdapter.RemindItem("2022년 "+ selectMonth+"월 "+document.getId()+"일", drawable, "2022/"+selectMonth+"/"+document.getId()));
                            }
                            //어댑터 연걸, 데이터셋 변경
                            remindListAdapter.addItem(items);
                            remindListAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }
}
