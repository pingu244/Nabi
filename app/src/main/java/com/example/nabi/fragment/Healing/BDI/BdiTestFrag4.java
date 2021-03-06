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


    public static BdiTestFrag4 newInstance() {
        return new BdiTestFrag4();
    }

    RadioGroup bdi_4;
    RadioButton button1,button2,button3,button4;
    Integer score, cnt_1, cnt_2, cnt_3, cnt_4;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button fourth_next = getActivity().findViewById(R.id.fourth_next);
        Button fourth_previous = getActivity().findViewById(R.id.fourth_previous);

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
//                        score+=0;
                        ((Healing_BdiTest)getActivity()).score[3] = 0;
//                        cnt_1++;
                        ((Healing_BdiTest)getActivity()).cnt[3] = 1;
                        break;

                    case R.id.q4_score_1:
//                        score+=1;
                        ((Healing_BdiTest)getActivity()).score[3] = 1;
//                        cnt_2++;
                        ((Healing_BdiTest)getActivity()).cnt[3] = 2;
                        break;

                    case R.id.q4_score_2:
//                        score+=2;
                        ((Healing_BdiTest)getActivity()).score[3] = 2;
//                        cnt_3++;
                        ((Healing_BdiTest)getActivity()).cnt[3] = 3;
                        break;

                    case R.id.q4_score_3:
//                        score+=3;
                        ((Healing_BdiTest)getActivity()).score[3] = 3;
//                        cnt_4++;
                        ((Healing_BdiTest)getActivity()).cnt[3] = 4;
                        break;
                }
            }
        });

        fourth_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle(); // ????????? ?????? ??? ??????
//                bundle.putInt("score",score);//????????? ?????? ??? ??????
//                bundle.putInt("cnt_1",cnt_1);
//                bundle.putInt("cnt_2",cnt_2);
//                bundle.putInt("cnt_3",cnt_3);
//                bundle.putInt("cnt_4",cnt_4);
//
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                BdiTestFrag5 fragment5 = new BdiTestFrag5();//???????????????2 ??????
//                fragment5.setArguments(bundle);//????????? ???????????????2??? ?????? ??????
//                transaction.replace(R.id.bdi_test_frag, fragment5);
                ((Healing_BdiTest)getActivity()).replaceFragment("page5",BdiTestFrag5.newInstance());
//                transaction.commit();
            }
        });

        fourth_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Healing_BdiTest)getActivity()).replaceFragment("page3",BdiTestFrag3.newInstance());
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        if (getArguments() != null)
//        {
//            score = getArguments().getInt("score"); // ???????????????1?????? ????????? ??? ??????
//            cnt_1 = getArguments().getInt("cnt_1");
//            cnt_2 = getArguments().getInt("cnt_2");
//            cnt_3 = getArguments().getInt("cnt_3");
//            cnt_4 = getArguments().getInt("cnt_4");
//
//        }

        return inflater.inflate(R.layout.healing_bdi_test_4, container, false);
    }
}
