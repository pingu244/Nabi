package com.example.nabi.fragment.Healing.BDI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nabi.R;
import com.example.nabi.fragment.Healing.Healing_BdiTest;
import com.example.nabi.fragment.Healing.SadTestResult;

import java.util.HashMap;
import java.util.Map;

public class BdiTestFrag21 extends Fragment {

    private Integer score, cnt_1, cnt_2, cnt_3, cnt_4;
    // 프래그먼트간의 이동 위한 인스턴스 생성
    public static BdiTestFrag8 newInstance() {
        return new BdiTestFrag8();
    }

    RadioGroup bdi_21;
    RadioButton button1,button2,button3,button4;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnComplete = getActivity().findViewById(R.id.bdi_complete);
        btnComplete.setEnabled(false);

        bdi_21 = view.findViewById(R.id.bdi_21);


        button1 = view.findViewById(R.id.q21_score_0);
        button2 = view.findViewById(R.id.q21_score_1);
        button3 = view.findViewById(R.id.q21_score_2);
        button4 = view.findViewById(R.id.q21_score_3);


        button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button1.isChecked()){
                    btnComplete.setEnabled(true);
                }
            }
        });

        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button2.isChecked()){
                    btnComplete.setEnabled(true);
                }
            }
        });

        button3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button3.isChecked()){
                    btnComplete.setEnabled(true);
                }
            }
        });

        button4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button4.isChecked()){
                    btnComplete.setEnabled(true);
                }
            }
        });

        bdi_21.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.q21_score_0:
                        score+=0;
                        cnt_1++;
                        break;

                    case R.id.q21_score_1:
                        score+=1;
                        cnt_2++;
                        break;

                    case R.id.q21_score_2:
                        score+=2;
                        cnt_3++;
                        break;

                    case R.id.q21_score_3:
                        score+=3;
                        cnt_4++;
                        break;
                }
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle(); // 번들을 통해 값 전달
//                bundle.putInt("score",score);//번들에 넘길 값 저장
//                bundle.putInt("cnt_1",cnt_1);
//                bundle.putInt("cnt_2",cnt_2);
//                bundle.putInt("cnt_3",cnt_3);
//                bundle.putInt("cnt_4",cnt_4);

                Intent intent = new Intent(getActivity(), BdiTestResult_Activity.class);

//                intent.putExtra("BdiTestResult", bundle);
                intent.putExtra("Bdi_score",score);//번들에 넘길 값 저장
                intent.putExtra("Bdi_cnt_1",cnt_1);
                intent.putExtra("Bdi_cnt_2",cnt_2);
                intent.putExtra("Bdi_cnt_3",cnt_3);
                intent.putExtra("Bdi_cnt_4",cnt_4);
                startActivity(intent);
                getActivity().finish();

//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                BdiTestResult frag_result = new BdiTestResult();//프래그먼트2 선언
//                frag_result.setArguments(bundle);//번들을 프래그먼트2로 보낼 준비
//                transaction.replace(R.id.bdi_test_frag, frag_result);
//                transaction.commit();


            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null)
        {
            score = getArguments().getInt("score"); // 프래그먼트1에서 받아온 값 넣기
            cnt_1 = getArguments().getInt("cnt_1");
            cnt_2 = getArguments().getInt("cnt_2");
            cnt_3 = getArguments().getInt("cnt_3");
            cnt_4 = getArguments().getInt("cnt_4");

        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.healing_bdi_test_21, container, false);

    }
}
