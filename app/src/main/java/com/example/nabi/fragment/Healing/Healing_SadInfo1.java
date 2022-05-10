package com.example.nabi.fragment.Healing;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.nabi.R;

public class Healing_SadInfo1 extends AppCompatActivity {
    ImageView img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healing_sad_info_1);

        img = findViewById(R.id.sadInfo1_img);

        Glide.with(this).load(R.drawable.sadinfo1_exercise).into(img);

        findViewById(R.id.sadInfo1_btnPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
