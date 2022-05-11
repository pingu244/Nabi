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

public class BdiTestFrag16 extends Fragment {

    private Integer score, cnt_1, cnt_2, cnt_3, cnt_4;
    // 프래그먼트간의 이동 위한 인스턴스 생성
    public static BdiTestFrag16 newInstance() {
        return new BdiTestFrag16();
    }

    RadioGroup bdi_16;
    RadioButton button1,button2,button3,button4;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button sixteenth_next = getActivity().findViewById(R.id.sixteenth_next);
        Button sixteenth_previous = getActivity().findViewById(R.id.sixteenth_previous);

        sixteenth_next.setEnabled(false);

        bdi_16 = view.findViewById(R.id.bdi_16);


        button1 = view.findViewById(R.id.q16_score_0);
        button2 = view.findViewById(R.id.q16_score_1);
        button3 = view.findViewById(R.id.q16_score_2);
        button4 = view.findViewById(R.id.q16_score_3);


        button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button1.isChecked()){
                    sixteenth_next.setEnabled(true);
                }
            }
        });

        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button2.isChecked()){
                    sixteenth_next.setEnabled(true);
                }
            }
        });

        button3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button3.isChecked()){
                    sixteenth_next.setEnabled(true);
                }
            }
        });

        button4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button4.isChecked()){
                    sixteenth_next.setEnabled(true);
                }
            }
        });

        bdi_16.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.q16_score_0:
//                        score+=0;
                        ((Healing_BdiTest)getActivity()).score[15] = 0;
//                        cnt_1++;
                        ((Healing_BdiTest)getActivity()).cnt[15] = 1;
                        break;

                    case R.id.q16_score_1:
//                        score+=1;
                        ((Healing_BdiTest)getActivity()).score[15] = 1;
//                        cnt_2++;
                        ((Healing_BdiTest)getActivity()).cnt[15] = 2;
                        break;

                    case R.id.q16_score_2:
//                        score+=2;
                        ((Healing_BdiTest)getActivity()).score[15] = 2;
//                        cnt_3++;
                        ((Healing_BdiTest)getActivity()).cnt[15] = 3;
                        break;

                    case R.id.q16_score_3:
//                        score+=3;
                        ((Healing_BdiTest)getActivity()).score[15] = 3;
//                        cnt_4++;
                        ((Healing_BdiTest)getActivity()).cnt[15] = 4;
                        break;
                }
            }
        });

        sixteenth_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle(); // 번들을 통해 값 전달
//                bundle.putInt("score",score);//번들에 넘길 값 저장
//                bundle.putInt("cnt_1",cnt_1);
//                bundle.putInt("cnt_2",cnt_2);
//                bundle.putInt("cnt_3",cnt_3);
//                bundle.putInt("cnt_4",cnt_4);
//
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                BdiTestFrag17 fragment17 = new BdiTestFrag17();//프래그먼트2 선언
//                fragment17.setArguments(bundle);//번들을 프래그먼트2로 보낼 준비
//                transaction.replace(R.id.bdi_test_frag, fragment17);
                ((Healing_BdiTest)getActivity()).replaceFragment("page17",BdiTestFrag17.newInstance());
//                transaction.commit();
            }
        });

        sixteenth_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Healing_BdiTest)getActivity()).replaceFragment("page15",BdiTestFrag15.newInstance());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        if (getArguments() != null)
//        {
//            score = getArguments().getInt("score"); // 프래그먼트1에서 받아온 값 넣기
//
//            cnt_1 = getArguments().getInt("cnt_1");
//            cnt_2 = getArguments().getInt("cnt_2");
//            cnt_3 = getArguments().getInt("cnt_3");
//            cnt_4 = getArguments().getInt("cnt_4");
//        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.healing_bdi_test_16, container, false);

    }
}
