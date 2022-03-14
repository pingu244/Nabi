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

public class BdiTestFrag11 extends Fragment {

    private Integer score;
    // 프래그먼트간의 이동 위한 인스턴스 생성
    public static BdiTestFrag8 newInstance() {
        return new BdiTestFrag8();
    }

    RadioGroup bdi_11;
    RadioButton button1,button2,button3,button4;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button eleventh_next = getActivity().findViewById(R.id.eleventh_next);
        eleventh_next.setEnabled(false);

        bdi_11 = view.findViewById(R.id.bdi_11);


        button1 = view.findViewById(R.id.q11_score_0);
        button2 = view.findViewById(R.id.q11_score_1);
        button3 = view.findViewById(R.id.q11_score_2);
        button4 = view.findViewById(R.id.q11_score_3);


        button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button1.isChecked()){
                    eleventh_next.setEnabled(true);
                }
            }
        });

        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button2.isChecked()){
                    eleventh_next.setEnabled(true);
                }
            }
        });

        button3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button3.isChecked()){
                    eleventh_next.setEnabled(true);
                }
            }
        });

        button4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button4.isChecked()){
                    eleventh_next.setEnabled(true);
                }
            }
        });

        bdi_11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.q11_score_0:
                        score+=0;
                        break;

                    case R.id.q11_score_1:
                        score+=1;
                        break;

                    case R.id.q11_score_2:
                        score+=2;
                        break;

                    case R.id.q11_score_3:
                        score+=3;
                        break;
                }
            }
        });

        eleventh_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle(); // 번들을 통해 값 전달
                bundle.putInt("score",score);//번들에 넘길 값 저장
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                BdiTestFrag12 fragment12 = new BdiTestFrag12();//프래그먼트2 선언
                fragment12.setArguments(bundle);//번들을 프래그먼트2로 보낼 준비
                transaction.replace(R.id.bdi_test_frag, fragment12);
                transaction.commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null)
        {
            score = getArguments().getInt("score"); // 프래그먼트1에서 받아온 값 넣기


        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.healing_bdi_test_11, container, false);




    }
}
