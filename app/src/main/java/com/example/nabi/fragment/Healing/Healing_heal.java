package com.example.nabi.fragment.Healing;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.nabi.R;
import com.example.nabi.fragment.Diary.WritingDiary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Healing_heal extends Fragment {

    @Nullable
    View view;
    TextView tv_sadTest, tv_result,tv_bdiTest, tv_bdiFigure;
    LinearLayout sad_def, sad_info_1, sad_info_2;
    String result, bdiResult;
    ImageView sadinfo1Img, sadinfo2Img;

    public static Healing_heal newInstance() {
        return new Healing_heal();
    }
    private FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        view = inflater.inflate(R.layout.healing_heal, container, false);

        tv_sadTest = view.findViewById(R.id.sad_test);
        tv_result = view.findViewById(R.id.sad_figure);
        tv_bdiTest = view.findViewById(R.id.bdi_test);
        tv_bdiFigure = view.findViewById(R.id.bdi_figure);
        sad_def = view.findViewById(R.id.sad_def);
        sad_info_1 = view.findViewById(R.id.sad_info_1);
        sad_info_2 = view.findViewById(R.id.sad_info_2);

        sadinfo1Img = view.findViewById(R.id.sad_info_1_img);
        sadinfo1Img.setClipToOutline(true);    // ????????? ?????? ?????? ???????????? ???
        sadinfo2Img = view.findViewById(R.id.sad_info_2_img);
        sadinfo2Img.setClipToOutline(true);    // ????????? ?????? ?????? ???????????? ???

        //?????? ???????????? ?????? ??? SAD ????????? ??????
        tv_sadTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Healing_SadTest.class);
                startActivity(intent);
            }
        });

        //?????? ???????????? ?????? ??? BDI ????????? ??????
        tv_bdiTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Healing_BdiTest.class);
                startActivity(intent);
            }
        });

        //SAD ??????
        sad_def.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Healing_SadDef.class);
                startActivity(intent);
            }
        });

        //SAD ???????????? 1
        sad_info_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Healing_SadInfo1.class);
                startActivity(intent);
            }
        });

        //SAD ???????????? 2
        sad_info_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Healing_SadInfo2.class);
                startActivity(intent);
            }
        });

//        // sad ????????? ????????????
//        DocumentReference docRef = db.collection("users")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .collection("healing").document("SAD Result");
//
//        // BDI ????????? ????????????
//        DocumentReference documentBDI = db.collection("users")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .collection("healing").document("BDI Result");
//
//        // document???????????? ?????????
//        Task<DocumentSnapshot> documentSnapshotTask = docRef.get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                Log.d("dataOutput", "DocumentSnapshot data: " + document.getData());
//                                Map<String, Object> mymap = document.getData();
//                                result = (String) mymap.get("sad_result");
//                                tv_result.setText(result);
//                            }
//
//
//                        }
//
//
//                    }
//                });
//
//
//        // document???????????? ?????????
//        Task<DocumentSnapshot> documentSnapshotTask2 = documentBDI.get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                Log.d("dataOutput", "DocumentSnapshot data: " + document.getData());
//                                Map<String, Object> mymap = document.getData();
//                                bdiResult = (String) mymap.get("bdi_result");
//                                tv_bdiFigure.setText(bdiResult);
//                            }
//
//
//                        }
//
//
//                    }
//                });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // sad ????????? ????????????
        DocumentReference docRef = db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("healing").document("SAD Result");

        // BDI ????????? ????????????
        DocumentReference documentBDI = db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("healing").document("BDI Result");

        // document???????????? ?????????
        Task<DocumentSnapshot> documentSnapshotTask = docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("dataOutput", "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> mymap = document.getData();
                                result = (String) mymap.get("sad_result");
                                tv_result.setText(result);
                            }


                        }


                    }
                });


        // document???????????? ?????????
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
                                tv_bdiFigure.setText(bdiResult);
                            }


                        }


                    }
                });
    }
}