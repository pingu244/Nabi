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

public class BdiTestFrag2 extends Fragment {


    public static BdiTestFrag2 newInstance() {
        return new BdiTestFrag2();
    }

    RadioGroup bdi_2;
    RadioButton button1,button2,button3,button4;
    Integer score, cnt_1, cnt_2, cnt_3, cnt_4;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button second_next = getActivity().findViewById(R.id.second_next);
        second_next.setEnabled(false);

        bdi_2 = view.findViewById(R.id.bdi_2);

        button1 = view.findViewById(R.id.q2_score_0);
        button2 = view.findViewById(R.id.q2_score_1);
        button3 = view.findViewById(R.id.q2_score_2);
        button4 = view.findViewById(R.id.q2_score_3);


        button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button1.isChecked()){
                    second_next.setEnabled(true);
                }
            }
        });

        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button2.isChecked()){
                    second_next.setEnabled(true);
                }
            }
        });

        button3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button3.isChecked()){
                    second_next.setEnabled(true);
                }
            }
        });

        button4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button4.isChecked()){
                    second_next.setEnabled(true);
                }
            }
        });

        bdi_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.q2_score_0:
                        score+=0;
                        cnt_1++;
                        break;

                    case R.id.q2_score_1:
                        score+=1;
                        cnt_2++;
                        break;

                    case R.id. q2_score_2:
                        score+=2;
                        cnt_3++;
                        break;

                    case R.id. q2_score_3:
                        score+=3;
                        cnt_4++;
                        break;
                }
            }
        });

        second_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle(); // 번들을 통해 값 전달
                bundle.putInt("score",score);//번들에 넘길 값 저장
                bundle.putInt("cnt_1",cnt_1);
                bundle.putInt("cnt_2",cnt_2);
                bundle.putInt("cnt_3",cnt_3);
                bundle.putInt("cnt_4",cnt_4);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                BdiTestFrag3 fragment2 = new BdiTestFrag3();//프래그먼트2 선언
                fragment2.setArguments(bundle);//번들을 프래그먼트2로 보낼 준비
                transaction.replace(R.id.bdi_test_frag, fragment2);
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
            cnt_1 = getArguments().getInt("cnt_1");
            cnt_2 = getArguments().getInt("cnt_2");
            cnt_3 = getArguments().getInt("cnt_3");
            cnt_4 = getArguments().getInt("cnt_4");
        }

        return inflater.inflate(R.layout.healing_bdi_test_2, container, false);
    }
}
