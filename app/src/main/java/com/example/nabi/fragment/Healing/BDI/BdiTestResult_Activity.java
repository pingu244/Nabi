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


        tv_count1.setText("1번 문항 : "+cnt_1+"개");
        tv_count2.setText("2번 문항 : "+cnt_2+"개");
        tv_count3.setText("3번 문항 : "+cnt_3+"개");
        tv_count4.setText("4번 문항 : "+cnt_4+"개");
        tv_score.setText(score+" /");

        progressBar.setProgress(score);
        if(score>15)
            //progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.bdi_result_progressbar2));

        tv_bdiResult.setTextColor(Color.parseColor("#FF454870"));

        if(score<=9){
            tv_bdiResult.setText("우울하지 않은 상태");
            tv_bdiText.setText("현재 우울하지 않은 상태에요. \n" +
                    "앞으로 규칙적인 생활을 이어가보는 건 어떤가요?\n" +
                    "규칙적인 생활은 우울감을 예방하는 데 효과적이랍니다.");
            hashMap.put("bdi_result", "우울하지 않은 상태");
            resultIcon.setBackgroundResource(R.drawable.bdi_r_ic_1);


        }else if (score<=15){
            tv_bdiResult.setText("가벼운 우울 상태");
            tv_bdiText.setText("현재 가벼운 우울감이 있어요.\n" +
                    "가벼운 우울증은 일상생활 속에서 자연스레 치료할 수 있어요.\n" +
                    "친구들과 많이 만나고, 여가활동을 활발하게 하는 것이 우울감 극복에 도움을 줄 수 있습니다.\n");
            hashMap.put("bdi_result", "가벼운 우울 상태");
            resultIcon.setBackgroundResource(R.drawable.bdi_r_ic_2);

        }else if (score<=23){
            tv_bdiResult.setText("중한 우울 상태");
            tv_bdiText.setText("주의를 요하는 우울감이 나타나고 있어요.\n" +
                    "더 심해지기 전에 전문가와 상담을 해보는 것을 권장합니다.");
            hashMap.put("bdi_result", "중한 우울 상태");
            resultIcon.setBackgroundResource(R.drawable.bdi_r_ic_3);

        }else{
            tv_bdiResult.setText("심한 우울 상태");
            tv_bdiText.setText("우울감이 일상생활에 크게 영향을 미치고 있어요.\n" +
                    "상담, 약물 치료 등 전문적인 처방을 받아야 합니다."
            );
            hashMap.put("bdi_result", "심한 우울 상태");
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