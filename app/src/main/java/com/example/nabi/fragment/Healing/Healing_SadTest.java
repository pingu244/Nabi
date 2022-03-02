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

    private int count = 0;
    RadioButton btnYes,btnNo;
    TextView tv_sad;
    LinearLayout sadLayout;
    Button btnComplete;

    RadioGroup rg,rg2,rg3,rg4,rg5,rg6;

    private FirebaseFirestore db;
    DatabaseReference mDBReference = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healing_sad_test);

        ImageButton btnCancel = findViewById(R.id.btnCancel);

        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        tv_sad = findViewById(R.id.sad_figure);
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.healing_heal, (ViewGroup) findViewById(R.id.sadLayout));
        tv_sad = layout.findViewById(R.id.sad_figure);

        btnComplete = findViewById(R.id.btnComplete);

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
                        count+=1;
                        break;
                }
            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnYes2:
                        count+=1;
                        break;
                }
            }
        });
        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnYes3:
                        count+=1;
                        break;
                }
            }
        });
        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnYes4:
                        count+=1;
                        break;
                }
            }
        });
        rg5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnYes5:
                        count+=1;
                        break;
                }
            }
        });
        rg6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnYes6:
                        count+=1;
                        break;
                }
            }
        });


        btnComplete.setOnClickListener(new View.OnClickListener() {
            String result="";
            @Override
            public void onClick(View v) {

                Map<String, Object> hashMap = new HashMap<>();

                if(count>=3) {
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

        intent.putExtra("SadTestResult", count);
        startActivity(intent);
        finish();


    }
}
