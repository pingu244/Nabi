package com.example.nabi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nabi.fragment.Fragment_three;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Fragment_three frag_diary;

    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frag_diary = new Fragment_three();



        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.tab1:
                                Toast.makeText(getApplicationContext(), "첫번째 탭", Toast.LENGTH_SHORT).show();

                                return true;

                            case R.id.tab2:
                                Toast.makeText(getApplicationContext(), "두번째 탭", Toast.LENGTH_SHORT).show();

                                return true;

                            case R.id.tab3:
                                Toast.makeText(getApplicationContext(), "세번째 탭", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, frag_diary).commit();

                                return true;

                            case R.id.tab4:
                                Toast.makeText(getApplicationContext(), "네번째 탭", Toast.LENGTH_SHORT).show();

                                return true;

                        }
                        return false;
                    }

                }
        );


    }

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