package com.example.nabi.fragment.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.nabi.EditPasswordFrag;
import com.example.nabi.GloomyWeatherFrag;
import com.example.nabi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Mypage extends AppCompatActivity {

    ImageButton back;
    TextView email;
    RelativeLayout nickname, password, weather, logout, delete;
    Switch alarm;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        firebaseAuth =  FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        back = findViewById(R.id.mypage_back);
        email = findViewById(R.id.mypage_emailText);
        nickname = findViewById(R.id.mypage_nickname);
        password = findViewById(R.id.mypage_changePassword);
        weather = findViewById(R.id.mypage_weather);
        logout = findViewById(R.id.mypage_logout);
        delete = findViewById(R.id.mypage_delete);
        alarm = findViewById(R.id.mypage_alarmSwitch);

        // 뒤로가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 이메일 표시
        email.setText(firebaseAuth.getCurrentUser().getEmail());

        // 닉네임 바꾸기
        nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mypage_nicknameFragment n = new Mypage_nicknameFragment();
                n.show(getSupportFragmentManager(), "nicknameChange");
            }
        });

        // 비밀번호 변경
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditPasswordFrag p = new EditPasswordFrag();
                p.show(getSupportFragmentManager(), "EditPassword");
            }
        });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GloomyWeatherFrag g = new GloomyWeatherFrag();
                g.show(getSupportFragmentManager(),"setGloomyWeather");
            }
        });

        // 로그아웃 dialogFragment
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mypage_logoutFragment m = new Mypage_logoutFragment();
                m.show(getSupportFragmentManager(), "logoutFragment");
            }
        });

        // 계정 삭제 dialogFragment
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mypage_deleteFragment d = new Mypage_deleteFragment();
                d.show(getSupportFragmentManager(), "deleteFragment");
            }
        });





    }
}