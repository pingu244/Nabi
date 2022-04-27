package com.example.nabi;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabi.fragment.Diary.FragDiary;
import com.example.nabi.fragment.Healing.FragHealing;
import com.example.nabi.fragment.Home.FragHome;
import com.example.nabi.fragment.PushNotification.PreferenceHelper;
import com.example.nabi.fragment.Remind.FragRemind;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // 산책 걸음수 측정 변수
    SensorManager sm;
    Sensor sensor_step_detector;
    ProgressBar progressBar;
    TextView stepCountView, tv_distance, tv_kcal;
    public int currentSteps = 0;    // 현재 걸음 수
    int kcal = 0; double km = 0;   // 칼로리, 거리


    Fragment frag_home;
    Fragment frag_healing;
    Fragment frag_diary;
    Fragment frag_remind;

    public Integer diary_weather = null;   // home화면에서 writingDiary로 전달하기 위함

    public boolean LoginSuccess;

    BottomNavigationView bottomNavigation;

    private long mBackWait = 0;
    @RequiresApi(api = Build.VERSION_CODES.M)
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

        Intent receive_intent = getIntent();
        LoginSuccess = receive_intent.getBooleanExtra("LoginSuccess", false);

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



        // 산책 걸음수 측정
        // 활동 퍼미션 체크
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }


        // 걸음 센서 연결
        // * 옵션
        // - TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작
        // - TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
        //
        sm = (SensorManager) this.getSystemService(SENSOR_SERVICE);   // 센서 매니저 생성
        sensor_step_detector = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);  // 스템 감지 센서 등록


    }


    // 뒤로가기 두 번 누르면 종료
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - mBackWait > 2000){
            mBackWait = System.currentTimeMillis();
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else{


            // 종료
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // 산책 값 가져오기
        currentSteps = PreferenceHelper.getStep(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sm.registerListener(this, sensor_step_detector, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(this);
        // 걸음수 임시 저장
        PreferenceHelper.setStep(this, currentSteps);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // 센서 유형이 스텝감지 센서인 경우 걸음수 +1
        if(event.sensor.getType()==Sensor.TYPE_STEP_DETECTOR){
            if(event.values[0]==1.0f){
                currentSteps +=event.values[0];
                kcal = currentSteps / 30;   // 성인은 걸음 보폭이 약 60㎝로 30보를 걸으면 1㎉가 소모된다.
                km = currentSteps * 0.0006;
                try{
                    stepCountView = findViewById(R.id.tv_step);
                    progressBar = findViewById(R.id.progressbar);
                    tv_distance = findViewById(R.id.tv_distance);
                    tv_kcal = findViewById(R.id.tv_kcal);

                    stepCountView.setText(String.valueOf(currentSteps));
                    progressBar.setProgress(currentSteps);
                    tv_kcal.setText(String.valueOf(kcal));
                    tv_distance.setText(String.format("%.2f", km));

                } catch (Exception e){}
                // 걸음수 임시 저장
                PreferenceHelper.setStep(this, currentSteps);

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}