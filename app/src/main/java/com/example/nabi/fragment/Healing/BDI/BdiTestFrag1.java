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
public class BdiTestFrag1 extends Fragment {


    public Integer score = 0;
    public Integer cnt_1 = 0;
    public Integer cnt_2 = 0;
    public Integer cnt_3 = 0;
    public Integer cnt_4 = 0;

    public static BdiTestFrag1 newInstance() {
        return new BdiTestFrag1();
    }

    RadioGroup bdi_1;
    RadioButton button1,button2,button3,button4;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button first_next = getActivity().findViewById(R.id.first_next);
        first_next.setEnabled(false);

        bdi_1 = view.findViewById(R.id.bdi_1);

        button1 = view.findViewById(R.id.score_0);
        button2 = view.findViewById(R.id.score_1);
        button3 = view.findViewById(R.id.score_2);
        button4 = view.findViewById(R.id.score_3);


        button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button1.isChecked()){
                    first_next.setEnabled(true);
                }
            }
        });

        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button2.isChecked()){
                    first_next.setEnabled(true);
                }
            }
        });

        button3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button3.isChecked()){
                    first_next.setEnabled(true);
                }
            }
        });

        button4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (button4.isChecked()){
                    first_next.setEnabled(true);
                }
            }
        });

        bdi_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.score_0:
                        score+=0;
                        cnt_1++;
                        break;

                    case R.id.score_1:
                        score+=1;
                        cnt_2++;
                        break;

                    case R.id.score_2:
                        score+=2;
                        cnt_3++;
                        break;

                    case R.id.score_3:
                        score+=3;
                        cnt_4++;
                        break;
                }
            }
        });

        first_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle(); // 번들을 통해 값 전달
                bundle.putInt("score",score);//번들에 넘길 값 저장
                bundle.putInt("cnt_1",cnt_1);
                bundle.putInt("cnt_2",cnt_2);
                bundle.putInt("cnt_3",cnt_3);
                bundle.putInt("cnt_4",cnt_4);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                BdiTestFrag2 fragment2 = new BdiTestFrag2();//프래그먼트2 선언
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
        return inflater.inflate(R.layout.healing_bdi_test_1, container, false);
    }
}
