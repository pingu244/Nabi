package com.example.nabi.fragment.Healing.BDI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nabi.R;
import com.example.nabi.fragment.Healing.FragHealing;
import com.example.nabi.fragment.Healing.SadTestResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BdiTestResult extends Fragment {

    Integer score, cnt_1, cnt_2, cnt_3, cnt_4;
    View view;
    TextView tv_bdiResult, tv_bdiText, tv_count1, tv_count2, tv_count3, tv_count4;
    Button btnRetest;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.healing_bdi_result, container,false);

        tv_bdiResult = view.findViewById(R.id.tv_bdiResult);
        tv_bdiText = view.findViewById(R.id.tv_bdiText);
        btnRetest = view.findViewById(R.id.btnRetest);
        tv_count1 = view.findViewById(R.id.cnt_1);
        tv_count2 = view.findViewById(R.id.cnt_2);
        tv_count3 = view.findViewById(R.id.cnt_3);
        tv_count4 = view.findViewById(R.id.cnt_4);

        db = FirebaseFirestore.getInstance();
        Map<String, Object> hashMap = new HashMap<>();




        if (getArguments() != null)
        {
            score = getArguments().getInt("score");
            cnt_1 = getArguments().getInt("cnt_1");
            cnt_2 = getArguments().getInt("cnt_2");
            cnt_3 = getArguments().getInt("cnt_3");
            cnt_4 = getArguments().getInt("cnt_4");
        }

        tv_count1.setText("1번 문항 : "+cnt_1+"개");
        tv_count2.setText("2번 문항 : "+cnt_2+"개");
        tv_count3.setText("3번 문항 : "+cnt_3+"개");
        tv_count4.setText("4번 문항 : "+cnt_4+"개");

        if(score<=9){
            tv_bdiResult.setText("우울하지 않은 상태");
            tv_bdiText.setText("현재 우울하지 않은 상태에요. \n" +
                    "앞으로 규칙적인 생활을 이어가보는 건 어떤가요?\n" +
                    "규칙적인 생활은 우울감을 예방하는 데 효과적이랍니다.");

            hashMap.put("bdi_result", "우울하지 않은 상태");
        }else if (score<=15){
            tv_bdiResult.setText("가벼운 우울 상태");
            tv_bdiText.setText("현재 가벼운 우울감이 있어요.\n" +
                    "가벼운 우울증은 일상생활 속에서 자연스레 치료할 수 있어요.\n" +
                    "친구들과 많이 만나고, 여가활동을 활발하게 하는 것이 우울감 극복에 도움을 줄 수 있습니다.\n");
            hashMap.put("bdi_result", "가벼운 우울 상태");
        }else if (score<=23){
            tv_bdiResult.setText("중한 우울 상태");
            tv_bdiText.setText("주의를 요하는 우울감이 나타나고 있어요.\n" +
                    "더 심해지기 전에 전문가와 상담을 해보는 것을 권장합니다.");
            hashMap.put("bdi_result", "중한 우울 상태");
        }else{
            tv_bdiResult.setText("심한 우울 상태");
            tv_bdiText.setText("우울감이 일상생활에 크게 영향을 미치고 있어요.\n" +
                    "상담, 약물 치료 등 전문적인 처방을 받아야 합니다."
            );
            hashMap.put("bdi_result", "심한 우울 상태");
        }

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("healing").document("BDI Result").set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("dataPut", "DocumentSnapshot successfully written!");

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dataPut", "Error writing document", e);
                    }
                });

        btnRetest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(BdiTestResult.this).commit();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                BdiTestFrag1 frag1 = new BdiTestFrag1();//프래그먼트2 선언
                transaction.add(R.id.bdi_test_frag, frag1 );
                transaction.commit();
            }
        });


        return view;
    }
}
