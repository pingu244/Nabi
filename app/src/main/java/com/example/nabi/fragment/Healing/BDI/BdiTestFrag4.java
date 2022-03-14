package com.example.nabi.fragment.Healing.BDI;

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

public class BdiTestFrag4 extends Fragment {


    public static BdiTestFrag1 newInstance() {
        return new BdiTestFrag1();
    }

    RadioGroup bdi_4;
    RadioButton button1,button2,button3,button4;
    Integer score;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button fourth_next = getActivity().findViewById(R.id.fourth_next);
        fourth_next.setEnabled(false);

        bdi_4 = view.findViewById(R.id.bdi_4);

        button1 = view.findViewById(R.id.q4_score_0);
        button2 = view.findViewById(R.id.q4_score_1);
        button3 = view.findViewById(R.id.q4_score_2);
        button4 = view.findViewById(R.id.q4_score_3);


        button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button1.isChecked()){
                    fourth_next.setEnabled(true);
                }
            }
        });

        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button2.isChecked()){
                    fourth_next.setEnabled(true);
                }
            }
        });

        button3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button3.isChecked()){
                    fourth_next.setEnabled(true);
                }
            }
        });

        button4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button4.isChecked()){
                    fourth_next.setEnabled(true);
                }
            }
        });

        bdi_4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.q4_score_0:
                        score+=0;
                        break;

                    case R.id.q4_score_1:
                        score+=1;
                        break;

                    case R.id.q4_score_2:
                        score+=2;
                        break;

                    case R.id.q4_score_3:
                        score+=3;
                        break;
                }
            }
        });

        fourth_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle(); // 번들을 통해 값 전달
                bundle.putInt("score",score);//번들에 넘길 값 저장
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                BdiTestFrag5 fragment5 = new BdiTestFrag5();//프래그먼트2 선언
                fragment5.setArguments(bundle);//번들을 프래그먼트2로 보낼 준비
                transaction.replace(R.id.bdi_test_frag, fragment5);
                transaction.commit();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (getArguments() != null)
        {
            score = getArguments().getInt("score"); // 프래그먼트1에서 받아온 값 넣기


        }

        return inflater.inflate(R.layout.healing_bdi_test_4, container, false);
    }
}
