package com.example.nabi.fragment.Healing;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nabi.MainActivity;
import com.example.nabi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Map;

public class Healing_therapy extends Fragment {

    CircularProgressBar progressBar;

    TextView stepCountView, stepGoalView, tv_distance, tv_kcal;
    private FirebaseFirestore db;
    String bdiResult;


    // 현재 걸음 수
    int currentSteps = 0;
    @Nullable
    View view;

    MusicListAdapter musicListAdapter;
    ArrayList<MusicListAdapter.MusicCategory> music_itemData;
    RecyclerView music_recycler;

    MeditationAdapter meditationAdapter;
    ArrayList<MeditationAdapter.MeditationItem> meditationItems;
    RecyclerView meditation_recycler;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.healing_therapy, container, false);

        db = FirebaseFirestore.getInstance();


        stepCountView = view.findViewById(R.id.tv_step);
        stepGoalView = view.findViewById(R.id.tv_goalStep);
        tv_distance = view.findViewById(R.id.tv_distance);
        tv_kcal = view.findViewById(R.id.tv_kcal);
        progressBar = view.findViewById(R.id.progressbar);
        meditation_recycler = view.findViewById(R.id.recycler_meditation);

        music_recycler = view.findViewById(R.id.recycler_music);

        //음악 리스트 어댑터 연결
        music_itemData = new ArrayList<>();
        musicListAdapter =new MusicListAdapter(music_itemData);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        music_recycler.setLayoutManager(layoutManager);
        music_recycler.setAdapter(musicListAdapter);

        //음악 카테고리 목록
        music_itemData.add(new MusicListAdapter.MusicCategory("감각적인",  R.drawable.mpic_sensitive));
        music_itemData.add(new MusicListAdapter.MusicCategory("밝은", R.drawable.mpic_bright));
        music_itemData.add(new MusicListAdapter.MusicCategory("새벽 감성", R.drawable.mpic_dawn));
        music_itemData.add(new MusicListAdapter.MusicCategory("수면", R.drawable.mpic_sleep));
        music_itemData.add(new MusicListAdapter.MusicCategory("신나는", R.drawable.mpic_exciting));
        music_itemData.add(new MusicListAdapter.MusicCategory("잔잔한 피아노", R.drawable.mpic_piano));
        music_itemData.add(new MusicListAdapter.MusicCategory("편안한", R.drawable.mpic_comfort));
        music_itemData.add(new MusicListAdapter.MusicCategory("ASMR", R.drawable.mpic_asmr));


        //명상 어댑터 연결
        meditationItems = new ArrayList<>();
        meditationAdapter = new MeditationAdapter(meditationItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        meditation_recycler.setLayoutManager(linearLayoutManager);
        meditation_recycler.setAdapter(meditationAdapter);


        //명상 음악 목록
        meditationItems.add(new MeditationAdapter.MeditationItem("Monumental Journey","7:45",R.drawable.mdpic_1_1));
        meditationItems.add(new MeditationAdapter.MeditationItem("Spenta Mainyu","2:49",R.drawable.mdpic_2_2));
        meditationItems.add(new MeditationAdapter.MeditationItem("Spirit of Fire","10:08",R.drawable.mdpic_3));
        meditationItems.add(new MeditationAdapter.MeditationItem("The Sleeping Prophet","7:43",R.drawable.mdpic_4_4));
        meditationItems.add(new MeditationAdapter.MeditationItem("Venkatesananda","10:10",R.drawable.mdpic_5));

        // BDI 결과값 가져오기
        DocumentReference documentBDI = db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("healing").document("BDI Result");

        // document가져오는 리스너
        Task<DocumentSnapshot> documentSnapshotTask2 = documentBDI.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("dataOutput", "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> mymap = document.getData();
                                bdiResult = (String) mymap.get("bdi_result");

                                //BDI 검사 결과에 따라 목표 걸음 수 다름

                                if (bdiResult.equals("우울하지 않은 상태")) {
                                    stepGoalView.setText(" / 3000");
                                    progressBar.setProgressMax(3000);
                                } else if (bdiResult.equals("가벼운 우울 상태")) {
                                    stepGoalView.setText(" / 4000");
                                    progressBar.setProgressMax(4000);
                                } else if (bdiResult.equals("중한 우울 상태")) {
                                    stepGoalView.setText(" / 5000");
                                    progressBar.setProgressMax(5000);
                                } else if (bdiResult.equals("심한 우울 상태")) {
                                    stepGoalView.setText(" / 6000");
                                    progressBar.setProgressMax(6000);
                                }
                            }


                        }


                    }
                });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 산책 값 가져오기
        currentSteps = ((MainActivity)getActivity()).currentSteps;
        stepCountView.setText(String.valueOf(currentSteps));

        progressBar.setProgress(currentSteps);

        tv_kcal.setText(String.valueOf(currentSteps/30));
        tv_distance.setText(String.format("%.2f", currentSteps * 0.0006));

    }




}




