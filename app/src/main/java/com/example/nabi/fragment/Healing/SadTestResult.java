package com.example.nabi.fragment.Healing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nabi.R;



public class SadTestResult extends AppCompatActivity {

    ImageButton btnCancel;
    Button btnReTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sad_test_result);

        TextView result = findViewById(R.id.sad_result);
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
        }
        else{
            result.setText("정상");
        }
    }
}
