package com.example.nabi;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nabi.fragment.Diary.FragDiary;
import com.example.nabi.fragment.Healing.FragHealing;
import com.example.nabi.fragment.Home.FragHome;
import com.example.nabi.fragment.Remind.FragRemind;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    Fragment frag_home;
    Fragment frag_healing;
    Fragment frag_diary;
    Fragment frag_remind;

    BottomNavigationView bottomNavigation;

    public static DiaryDataBase diaryDataBase = null;
    private long mBackWait = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigationView);

        frag_home = new FragHome();
        frag_healing = new FragHealing();
        frag_diary = new FragDiary();
        frag_remind = new FragRemind();

        openDatabase();

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

    public void openDatabase() {
        // open database
        if (diaryDataBase != null) {
            diaryDataBase.close();
            diaryDataBase = null;
        }

        diaryDataBase = diaryDataBase.getInstance(this);
        boolean isOpen = diaryDataBase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (diaryDataBase != null) {
            diaryDataBase.close();
            diaryDataBase = null;
        }
    }

    // 뒤로가기 두 번 누르면 종료
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - mBackWait > 2000){
            mBackWait = System.currentTimeMillis();
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else{
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
    }
}