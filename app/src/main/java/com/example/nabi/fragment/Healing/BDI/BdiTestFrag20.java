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

public class BdiTestFrag20 extends Fragment {

    private Integer score, cnt_1, cnt_2, cnt_3, cnt_4;
    // 프래그먼트간의 이동 위한 인스턴스 생성
    public static BdiTestFrag20 newInstance() {
        return new BdiTestFrag20();
    }

    RadioGroup bdi_20;
    RadioButton button1,button2,button3,button4;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button twentieth_next = getActivity().findViewById(R.id.twentieth_next);
        Button twentieth_previous = getActivity().findViewById(R.id.twentieth_previous);

        twentieth_next.setEnabled(false);

        bdi_20 = view.findViewById(R.id.bdi_20);


        button1 = view.findViewById(R.id.q20_score_0);
        button2 = view.findViewById(R.id.q20_score_1);
        button3 = view.findViewById(R.id.q20_score_2);
        button4 = view.findViewById(R.id.q20_score_3);


        button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button1.isChecked()){
                    twentieth_next.setEnabled(true);
                }
            }
        });

        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button2.isChecked()){
                    twentieth_next.setEnabled(true);
                }
            }
        });

        button3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button3.isChecked()){
                    twentieth_next.setEnabled(true);
                }
            }
        });

        button4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button4.isChecked()){
                    twentieth_next.setEnabled(true);
                }
            }
        });

        bdi_20.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.q20_score_0:
//                        score+=0;
                        ((Healing_BdiTest)getActivity()).score[19] = 0;
//                        cnt_1++;
                        ((Healing_BdiTest)getActivity()).cnt[19] = 1;
                        break;

                    case R.id.q20_score_1:
//                        score+=1;
                        ((Healing_BdiTest)getActivity()).score[19] = 1;
//                        cnt_2++;
                        ((Healing_BdiTest)getActivity()).cnt[19] = 2;
                        break;

                    case R.id.q20_score_2:
//                        score+=2;
                        ((Healing_BdiTest)getActivity()).score[19] = 2;
//                        cnt_3++;
                        ((Healing_BdiTest)getActivity()).cnt[19] = 3;
                        break;

                    case R.id.q20_score_3:
//                        score+=3;
                        ((Healing_BdiTest)getActivity()).score[19] = 3;
//                        cnt_4++;
                        ((Healing_BdiTest)getActivity()).cnt[19] = 4;
                        break;
                }
            }
        });

        twentieth_next.setOnClickListener(new View.OnClickListener() {

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
//                BdiTestFrag21 fragment21 = new BdiTestFrag21();//프래그먼트2 선언
//                fragment21.setArguments(bundle);//번들을 프래그먼트2로 보낼 준비
//                transaction.replace(R.id.bdi_test_frag, fragment21);
                ((Healing_BdiTest)getActivity()).replaceFragment("page21",BdiTestFrag21.newInstance());
//                transaction.commit();
            }
        });

        twentieth_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Healing_BdiTest)getActivity()).replaceFragment("page19",BdiTestFrag19.newInstance());
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
//
//        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.healing_bdi_test_20, container, false);

    }
}
