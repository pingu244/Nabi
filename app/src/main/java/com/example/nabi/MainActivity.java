package com.example.nabi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nabi.fragment.Fragment_three;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Fragment_three frag_diary;  // 세번째 탭 눌렀을때 나오는 fragment

    BottomNavigationView bottomNavigation;  // 바텀 네비게이션


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frag_diary = new Fragment_three();

        
        // bottomNavigation 탭하면 실행
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.tab1: // 홈
                                Toast.makeText(getApplicationContext(), "첫번째 탭", Toast.LENGTH_SHORT).show();

                                return true;

                            case R.id.tab2: // 힐링탭
                                Toast.makeText(getApplicationContext(), "두번째 탭", Toast.LENGTH_SHORT).show();

                                return true;

                            case R.id.tab3: // 다이어리 탭
                                Toast.makeText(getApplicationContext(), "세번째 탭", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, frag_diary).commit();

                                return true;

                            case R.id.tab4: // 보고서
                                Toast.makeText(getApplicationContext(), "네번째 탭", Toast.LENGTH_SHORT).show();

                                return true;

                        }
                        return false;
                    }

                }
        );


    }

    // bottomNavigation 선택되면 위에 함수 실행되게
    public void onTabSelected(int position) {
        if(position == 0){
            bottomNavigation.setSelectedItemId(R.id.tab1);
        } else if(position == 1){
            bottomNavigation.setSelectedItemId(R.id.tab2);
        } else if(position == 2){
            bottomNavigation.setSelectedItemId(R.id.tab3);
        } else if(position == 3){
            bottomNavigation.setSelectedItemId(R.id.tab4);
        }
    }

}