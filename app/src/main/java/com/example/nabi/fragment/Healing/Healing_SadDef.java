package com.example.nabi.fragment.Healing;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.nabi.R;

public class Healing_SadDef extends AppCompatActivity {

    ImageView img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healing_sad_def);

        img = findViewById(R.id.sadDef_img);

        Glide.with(this).load(R.drawable.sad_def_img).into(img);

        findViewById(R.id.sadDef_btnPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
