package com.example.nabi.fragment.Healing.BDI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nabi.R;
import com.example.nabi.fragment.Healing.Healing_BdiTest;
import com.example.nabi.fragment.Healing.Healing_SadTest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BdiTestResult_Activity extends AppCompatActivity {
    Integer score, cnt_1, cnt_2, cnt_3, cnt_4;
    TextView tv_bdiResult, tv_bdiText, tv_count1, tv_count2, tv_count3, tv_count4, tv_score;
    Button btnRetest, btnExit;
    ImageView resultIcon,bdiResult_btnCancel;
    private FirebaseFirestore db;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdi_test_result_);

        tv_bdiResult = findViewById(R.id.tv_bdiResult);
        tv_bdiText = findViewById(R.id.tv_bdiText);
        btnRetest = findViewById(R.id.btnRetest);
        btnExit = findViewById(R.id.btnQuit);

        tv_count1 = findViewById(R.id.cnt_1);
        tv_count2 = findViewById(R.id.cnt_2);
        tv_count3 = findViewById(R.id.cnt_3);
        tv_count4 = findViewById(R.id.cnt_4);
        tv_score = findViewById(R.id.bdi_score);
        progressBar = findViewById(R.id.bdi_progress);

        resultIcon = findViewById(R.id.bdi_result_icon);
        bdiResult_btnCancel = findViewById(R.id.bdiResult_btnCancel);

        db = FirebaseFirestore.getInstance();
        Map<String, Object> hashMap = new HashMap<>();


        Intent receive_intent = getIntent();
        score = receive_intent.getIntExtra("Bdi_score", 0);
        cnt_1 = receive_intent.getIntExtra("Bdi_cnt_1", 0);
        cnt_2 = receive_intent.getIntExtra("Bdi_cnt_2", 0);
        cnt_3 = receive_intent.getIntExtra("Bdi_cnt_3", 0);
        cnt_4 = receive_intent.getIntExtra("Bdi_cnt_4", 0);


        tv_count1.setText("1??? ?????? : "+cnt_1+"???");
        tv_count2.setText("2??? ?????? : "+cnt_2+"???");
        tv_count3.setText("3??? ?????? : "+cnt_3+"???");
        tv_count4.setText("4??? ?????? : "+cnt_4+"???");
        tv_score.setText(score+" /");

        progressBar.setProgress(score);
        if(score>15)
            progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.bdi_result_progressbar2));

        tv_bdiResult.setTextColor(Color.parseColor("#FF454870"));

        if(score<=9){
            tv_bdiResult.setText("???????????? ?????? ??????");
            tv_bdiText.setText("?????? ???????????? ?????? ????????????. \n" +
                    "????????? ???????????? ????????? ??????????????? ??? ?????????????\n" +
                    "???????????? ????????? ???????????? ???????????? ??? ?????????????????????.");
            hashMap.put("bdi_result", "???????????? ?????? ??????");
            resultIcon.setBackgroundResource(R.drawable.bdi_r_ic_1);


        }else if (score<=15){
            tv_bdiResult.setText("????????? ?????? ??????");
            tv_bdiText.setText("?????? ????????? ???????????? ?????????.\n" +
                    "????????? ???????????? ???????????? ????????? ???????????? ????????? ??? ?????????.\n" +
                    "???????????? ?????? ?????????, ??????????????? ???????????? ?????? ?????? ????????? ????????? ????????? ??? ??? ????????????.\n");
            hashMap.put("bdi_result", "????????? ?????? ??????");
            resultIcon.setBackgroundResource(R.drawable.bdi_r_ic_2);

        }else if (score<=23){
            tv_bdiResult.setText("?????? ?????? ??????");
            tv_bdiText.setText("????????? ????????? ???????????? ???????????? ?????????.\n" +
                    "??? ???????????? ?????? ???????????? ????????? ????????? ?????? ???????????????.");
            hashMap.put("bdi_result", "?????? ?????? ??????");
            resultIcon.setBackgroundResource(R.drawable.bdi_r_ic_3);

        }else{
            tv_bdiResult.setText("?????? ?????? ??????");
            tv_bdiText.setText("???????????? ??????????????? ?????? ????????? ????????? ?????????.\n" +
                    "??????, ?????? ?????? ??? ???????????? ????????? ????????? ?????????."
            );
            hashMap.put("bdi_result", "?????? ?????? ??????");
            resultIcon.setBackgroundResource(R.drawable.bdi_r_ic_4);
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
                finish();
                Intent intent = new Intent(getApplicationContext(), Healing_BdiTest.class);
                startActivity(intent);

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bdiResult_btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}