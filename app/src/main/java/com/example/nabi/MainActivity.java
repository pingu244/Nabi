package com.example.nabi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nabi.fragment.FragDiary;
import com.example.nabi.fragment.FragHealing;
import com.example.nabi.fragment.FragHome;
import com.example.nabi.fragment.FragRemind;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    Fragment frag_home;
    Fragment frag_healing;
    Fragment frag_diary;
    Fragment frag_remind;

    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigationView);

        frag_home = new FragHome();
        frag_healing = new FragHealing();
        frag_diary = new FragDiary();
        frag_remind = new FragRemind();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag_home).commitAllowingStateLoss();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag_home).commitAllowingStateLoss();
                        return true;

                    case R.id.healing:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag_healing).commitAllowingStateLoss();
                        return true;


                    case R.id.diary:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag_diary).commitAllowingStateLoss();
                        return true;


                    case R.id.remind:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag_remind).commitAllowingStateLoss();
                        return true;
                }
                return true;
            }
        });
    }

}