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
    public static BdiTestFrag21 newInstance() {
        return new BdiTestFrag21();
    }

    RadioGroup bdi_21;
    RadioButton button1,button2,button3,button4;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnComplete = getActivity().findViewById(R.id.bdi_complete);
        Button twentyOne_previous = getActivity().findViewById(R.id.twentyOne_previous);

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
//                        score+=0;
                        ((Healing_BdiTest)getActivity()).score[20] = 0;
//                        cnt_1++;
                        ((Healing_BdiTest)getActivity()).cnt[20] = 1;
                        break;

                    case R.id.q21_score_1:
//                        score+=1;
                        ((Healing_BdiTest)getActivity()).score[20] = 1;
//                        cnt_2++;
                        ((Healing_BdiTest)getActivity()).cnt[20] = 2;
                        break;

                    case R.id.q21_score_2:
//                        score+=2;
                        ((Healing_BdiTest)getActivity()).score[20] = 2;
//                        cnt_3++;
                        ((Healing_BdiTest)getActivity()).cnt[20] = 3;
                        break;

                    case R.id.q21_score_3:
//                        score+=3;
                        ((Healing_BdiTest)getActivity()).score[20] = 3;
//                        cnt_4++;
                        ((Healing_BdiTest)getActivity()).cnt[20] = 4;
                        break;
                }
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), BdiTestResult_Activity.class);
                int count_score = 0;
                int cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0;
                for(int i = 0; i<21; i++){
                    count_score += ((Healing_BdiTest)getActivity()).score[i];
                    switch (((Healing_BdiTest)getActivity()).cnt[i]){
                        case 1:
                            cnt1++; break;
                        case 2:
                            cnt2++; break;
                        case 3:
                            cnt3++; break;
                        case 4:
                            cnt4++; break;
                    }
                }

                intent.putExtra("Bdi_score",count_score);//번들에 넘길 값 저장
                intent.putExtra("Bdi_cnt_1",cnt1);
                intent.putExtra("Bdi_cnt_2",cnt2);
                intent.putExtra("Bdi_cnt_3",cnt3);
                intent.putExtra("Bdi_cnt_4",cnt4);
                startActivity(intent);
                getActivity().finish();

            }
        });

        twentyOne_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Healing_BdiTest)getActivity()).replaceFragment("page20",BdiTestFrag20.newInstance());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.healing_bdi_test_21, container, false);

    }
}
