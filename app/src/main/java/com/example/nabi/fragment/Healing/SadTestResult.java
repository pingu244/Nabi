package com.example.nabi.fragment.Healing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nabi.R;

import org.w3c.dom.Text;


public class SadTestResult extends AppCompatActivity {

    ImageButton btnCancel;
    ImageView resultIcon;
    Button btnReTest;
    TextView sadMention;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sad_test_result);

        TextView result = findViewById(R.id.sad_result);
        sadMention = findViewById(R.id.sad_mention);

        btnCancel = findViewById(R.id.btnCancel);
        btnReTest = findViewById(R.id.btnRetest);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });

        btnReTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), Healing_SadTest.class);
                startActivity(intent);
            }
        });

        Intent receive_intent = getIntent();
        Integer count = receive_intent.getIntExtra("SadTestResult", 0);

        if (count>=3){
            result.setText("주의! 상담권유");
            result.setTextColor(R.color.warning);
            sadMention.setText("총 6개의 질문 중, "+count+"개의 질문에서 \"예\"를 체크하셨습니다.\n" +
                    "계절의 변화에 따른 신체 변화에 주의를 기울일 필요가 있어요.\n" +
                    "정도가 심하다면, 전문가와의 상담을 추천합니다.");
            findViewById(R.id.sad_result_icon).setBackgroundResource(R.drawable.sad_warn);
        }
        else{
            result.setText("정상");
            result.setTextColor(R.color.good);
            sadMention.setText("총 6개의 질문 중, "+count+"개의 질문에서 \"예\"를 체크하셨습니다.\n" +
                    "앞으로도 나비와 함께 힐링해봐요");
            findViewById(R.id.sad_result_icon).setBackgroundResource(R.drawable.sad_good);
        }

        }


}

