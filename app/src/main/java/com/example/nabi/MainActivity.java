package com.example.nabi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.nabi.fragment.Diary.FragDiary;
import com.example.nabi.fragment.Diary.WritingDiary;
import com.example.nabi.fragment.Healing.FragHealing;
import com.example.nabi.fragment.Home.FragHome;
import com.example.nabi.fragment.PushNotification.PreferenceHelper;
import com.example.nabi.fragment.Remind.FragRemind;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.reflect.KFunction;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private final int ID_HOME = 1;
    private final int ID_HEALING = 2;
    private final int ID_DIARY = 3;
    private final int ID_REMIND = 4;

    // 산책 걸음수 측정 변수
    SensorManager sm;
    Sensor sensor_step_detector;
    CircularProgressBar progressBar;
    TextView stepCountView, tv_distance, tv_kcal;
    public int currentSteps = 0;    // 현재 걸음 수
    int kcal = 0; double km = 0;   // 칼로리, 거리

    java.util.Calendar cal = java.util.Calendar.getInstance();
    int cYEAR = cal.get(java.util.Calendar.YEAR);
    int cMonth = cal.get(java.util.Calendar.MONTH);
    int cDay = cal.get(java.util.Calendar.DATE);
    String YMD = (cYEAR+"/"+(cMonth+1)+"/"+cDay);


    Fragment frag_home;
    Fragment frag_healing;
    Fragment frag_diary;
    Fragment frag_remind;

    public Integer diary_weather = null;   // home화면에서 writingDiary로 전달하기 위함

    public boolean LoginSuccess;

    BottomNavigationView bottomNavigation;
//    MeowBottomNavigation bottomNavigation;


    private long mBackWait = 0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigationView);
//        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_baseline_cloud_24));
//        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HEALING, R.drawable.ic_baseline_pause_24));
//        bottomNavigation.add(new MeowBottomNavigation.Model(ID_DIARY, R.drawable.ic_baseline_wb_sunny_24));
//        bottomNavigation.add(new MeowBottomNavigation.Model(ID_REMIND, R.drawable.ic_baseline_opacity_24));

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
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragDiary()).commitAllowingStateLoss();
                        return true;


                    case R.id.remind:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag_remind).commitAllowingStateLoss();
                        return true;
                }
                return true;
            }
        });

//        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
//            @Override
//            public void onClickItem(MeowBottomNavigation.Model item) {
//                Toast.makeText(MainActivity.this, "click item: " + item.getId(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
//            @Override
//            public void onReselectItem(MeowBottomNavigation.Model item) {
//                Toast.makeText(MainActivity.this, "reselect item: " + item.getId(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
//            @Override
//            public void onShowItem(MeowBottomNavigation.Model item) {
//                Fragment fragment;
//                switch (item.getId()){
//                    case ID_HOME:
//                        fragment = frag_home;
//                        break;
//
//                    case ID_HEALING:
//                        fragment = frag_healing;
//                        break;
//
//
//                    case ID_DIARY:
//                        fragment = frag_diary;
//                        break;
//
//
//                    case ID_REMIND:
//                        fragment = frag_remind;
//                        break;
//                    default:
//                        throw new IllegalStateException("Unexpected value: " + item.getId());
//                }
//                loadFragment(fragment);
//            }
//        });
//        bottomNavigation.show(ID_HOME, true);

        pushWalkDataPush();

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

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment, null).commit();
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

        pushWalkDataPush();

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


        pushWalkDataPush();

//        // 걸음수 임시 저장
//        PreferenceHelper.setStep(this, currentSteps);
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

    // 걸음수 DB저장
    private void pushWalkDataPush() {

        String savedYMD = null;
        try{
            savedYMD = PreferenceHelper.getDate(this);
        } catch (Exception e){}

        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();

        //yyyy/MM/dd 포맷 설정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/M/d");

        //compareTo메서드를 통한 날짜비교
        int compare = 0;
        try {
            Date savedDate = dateFormat.parse(savedYMD);
            cal.setTime(savedDate);

            Date today = dateFormat.parse(YMD);
            cal2.setTime(today);

            compare = cal.compareTo(cal2);

            Log.v("pushWalkDataPush",savedDate+" / "+today+" / "+compare);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // 산책 값 가져오기
        int steps = PreferenceHelper.getStep(this);

        // savedDate가 today보다 이전이다. (true)
        if(compare==-1){
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("walk", steps);

            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("diary").document(savedYMD).set(hashMap, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("dataPut", "DocumentSnapshot successfully written!");
                            PreferenceHelper.setStep(getApplicationContext(), 0);
                            PreferenceHelper.setDate(getApplicationContext(), YMD);
                            PreferenceHelper.setMeditate(getApplicationContext(), 0);
                        }

                    });
        }
        else{
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("walk", steps);

            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("diary").document(YMD).set(hashMap, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            PreferenceHelper.setDate(getApplicationContext(), YMD);
                        }

                    });
        }

        // 산책 값 가져오기
        currentSteps = PreferenceHelper.getStep(this);

    }

}