package com.example.nabi.fragment.Healing;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nabi.R;
import com.example.nabi.fragment.Diary.DiaryResult;
import com.example.nabi.fragment.Diary.WritingDiary;
import com.example.nabi.fragment.Diary.WritingFrag1;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Healing_SadTest extends AppCompatActivity {


    int countSum = 0;
    int count[];
    RadioButton btnYes,btnNo;
    TextView tv_sad;
    LinearLayout sadLayout;
    Button btnComplete;

    RadioGroup rg,rg2,rg3,rg4,rg5,rg6;

    boolean goToResultCount[];    // 얘가 전부 true가 되면 완료 버튼 활성화

    private FirebaseFirestore db;
    DatabaseReference mDBReference = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healing_sad_test);

        count = new int[]{0,0,0,0,0,0};

        ImageButton btnCancel = findViewById(R.id.btnCancel);

        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        tv_sad = findViewById(R.id.sad_figure);
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.healing_heal, (ViewGroup) findViewById(R.id.sadLayout));
        tv_sad = layout.findViewById(R.id.sad_figure);

        btnComplete = findViewById(R.id.btnComplete);
        btnComplete.setEnabled(false); // 처음에는 비활성화
        goToResultCount = new boolean[]{false, false, false, false, false, false};

        db = FirebaseFirestore.getInstance();

        rg = findViewById(R.id.radio);
        rg2 = findViewById(R.id.radio2);
        rg3 = findViewById(R.id.radio3);
        rg4 = findViewById(R.id.radio4);
        rg5 = findViewById(R.id.radio5);
        rg6 = findViewById(R.id.radio6);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnYes:
                        count[0] = 1;
                        break;
                    case R.id.btnNo:
                        count[0] = 0;
                        break;
                }
                goToResultCount[0] = true;
                enableCompletBtn(goToResultCount);
            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnYes2:
                        count[1] = 1;
                        break;
                    case R.id.btnNo2:
                        count[1] = 0;
                        break;
                }
                goToResultCount[1] = true;
                enableCompletBtn(goToResultCount);
            }
        });
        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnYes3:
                        count[2] = 1;
                        break;
                    case R.id.btnNo3:
                        count[2] = 0;
                        break;
                }
                goToResultCount[2] = true;
                enableCompletBtn(goToResultCount);
            }
        });
        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnYes4:
                        count[3] = 1;
                        break;
                    case R.id.btnNo4:
                        count[3] = 0;
                        break;
                }
                goToResultCount[3] = true;
                enableCompletBtn(goToResultCount);
            }
        });
        rg5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnYes5:
                        count[4] = 1;
                        break;
                    case R.id.btnNo5:
                        count[4] = 0;
                        break;
                }
                goToResultCount[4] = true;
                enableCompletBtn(goToResultCount);
            }
        });
        rg6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnYes6:
                        count[5] = 1;
                        break;
                    case R.id.btnNo6:
                        count[5] = 0;
                        break;
                }
                goToResultCount[5] = true;
                enableCompletBtn(goToResultCount);
            }
        });



        btnComplete.setOnClickListener(new View.OnClickListener() {
            String result="";
            @Override
            public void onClick(View v) {

                Map<String, Object> hashMap = new HashMap<>();
                for(int i = 0; i<6; i++)
                    countSum += count[i];

                if(countSum>=3) {
                    result = "주의";
                }else{
                    result = "정상";}

                hashMap.put("sad_result", result);


                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("healing").document("SAD Result").set(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("dataPut", "DocumentSnapshot successfully written!");
                                    goToResult();
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("dataPut", "Error writing document", e);
                            }
                        });


            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }


    public void goToResult()
    {
        Intent intent = new Intent(this, SadTestResult.class);

        intent.putExtra("SadTestResult", countSum);
        startActivity(intent);
        finish();
    }

    // 배열이 전부 true인지 확인하고 완료버튼 활성화
    private void enableCompletBtn(boolean[] check){
        boolean result = true;
        for(int i = 0; i<check.length; i++) {
            if (!check[i])
                result = false;
        }
        if(result){
            btnComplete.setEnabled(true);
            btnComplete.setTextColor(Color.BLACK);
        }
    }

}
