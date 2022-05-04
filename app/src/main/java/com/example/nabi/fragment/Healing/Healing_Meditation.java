package com.example.nabi.fragment.Healing;

import static android.icu.text.UnicodeSet.CASE;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nabi.R;
import com.example.nabi.fragment.PushNotification.PreferenceHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Healing_Meditation extends AppCompatActivity {

    final int MESSAGE_ID_PLAYING = 1;

    TextView tv_title, tv_mention1, tv_mention2, btnPrevious, tv_playingTime, tv_playTime;
    Button btnQuit;
    ImageView imageView;
    int[] meditation_music;
    String[] meditation_mention;

    MediaPlayer mediaPlayer;
    boolean isPlaying = false;

    Integer meditation_time;
    FirebaseFirestore db;
    java.util.Calendar cal = java.util.Calendar.getInstance();
    int cYEAR = cal.get(java.util.Calendar.YEAR);
    int cMonth = cal.get(java.util.Calendar.MONTH);
    int cDay = cal.get(java.util.Calendar.DATE);
    String YMD = (cYEAR+"/"+(cMonth+1)+"/"+cDay);
    Integer meditation_timeBefore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healing_meditation);

        tv_title = findViewById(R.id.meditation_name);
        tv_mention1 = findViewById(R.id.tv_mention1);
        tv_mention2 = findViewById(R.id.tv_mention2);

        imageView = findViewById(R.id.img_meditation);
        btnQuit = findViewById(R.id.btn_quit);
        btnPrevious = findViewById(R.id.btnPrevious);
        tv_playingTime = findViewById(R.id.tv_playingTime);
        tv_playTime = findViewById(R.id.tv_playTime);

        meditation_music = new int[] {R.raw.monumental_journey,R.raw.spenta_mainyu, R.raw.spirit_of_fire, R.raw.the_sleeping_prophet, R.raw.venkatesananda };

        meditation_mention = new String[]{"숨을 깊이 들이쉬고 내쉽니다.", "호흡을 깊이 하면서","몸과 마음을 편하게 하고 긴장을 푸세요.", "사랑과 자비로 가득찬 자신의 모습을 떠올립니다.",
        "입가엔 미소를 가슴엔 평화를 담고서 이렇게 되뇌입니다.", "나는 하나 밖에 없는 귀한 나에게 자비의 마음을 보냅니다."};



        Intent intent = getIntent(); /*데이터 수신*/
        Integer pos = intent.getExtras().getInt("number");
        String title = intent.getExtras().getString("title");
        tv_title.setText(title);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), meditation_music[pos]);

        musicStart();


        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //음악 종료 코드
                isPlaying = false;
                mediaPlayer.stop();
                mediaPlayer.release();
                handler2.removeMessages(0);
                finish();

            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //음악종료 코드
                isPlaying = false;
                mediaPlayer.stop();
                mediaPlayer.release();
                handler2.removeMessages(0);
                quitDBsave();
                finish();

            }
        });


        db = FirebaseFirestore.getInstance();


        // 명상시간 DB저장
        String savedYMD = PreferenceHelper.getDate(this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        int compare = 0;
        try {
            Date savedDate = new Date(dateFormat.parse(savedYMD).getTime());
            Date today = new Date(dateFormat.parse(YMD).getTime());
            compare = savedDate.compareTo(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer time = PreferenceHelper.getMeditate(this);
        // savedDate가 today보다 이전이다. (true)
        if(compare<0){
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("meditate", time);

            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("diary").document(savedYMD).set(hashMap, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("dataPut", "DocumentSnapshot successfully written!");
                            PreferenceHelper.setMeditate(getApplicationContext(), 0);
                        }

                    });
        }
        // 명상시간 DB저장 끝



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        isPlaying = false;
        mediaPlayer.stop();
        mediaPlayer.release();
        handler2.removeMessages(0);

        quitDBsave();


    }

    public void musicStart(){

        meditation_timeBefore = PreferenceHelper.getMeditate(this);
        isPlaying = true;
        new meditationThread().start();
        new mentionThread().start();
        mediaPlayer.start();
        int minute, second, time;
        time = mediaPlayer.getDuration();
        minute = (time / (1000*60)) % 60;
        second = (time / 1000) % 60;

        tv_playTime.setText("/"+minute+":"+second);


    }

    class meditationThread extends Thread{

        @Override
        public void run() {
            while(isPlaying){  // 음악이 실행중일때 계속 돌아가게 함

                try{
                    Thread.sleep(1000);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                handler2.sendEmptyMessage(1);
            }
        }


    }
    class mentionThread extends Thread{

        @Override
        public void run() {
            while(isPlaying){  // 음악이 실행중일때 계속 돌아가게 함

                try{
                    Thread.sleep(1000);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }


    }

    final Handler handler = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (isPlaying) {
                int i = -1;

                if (i == meditation_mention.length - 1) {
                    i = 0;
                } else {
                    i++;
                }

                tv_mention1.setText(meditation_mention[i]);
                tv_mention2.setText(meditation_mention[i + 1]);
            }
        }

    };


    final Handler handler2 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            //밀리초 시간 변환
            int minute, second, time;

            if(isPlaying){
                time = mediaPlayer.getCurrentPosition();

                minute = (time / (1000*60)) % 60;
                second = (time / 1000) % 60;


                meditation_time = time;
                PreferenceHelper.setMeditate(getApplicationContext(), meditation_timeBefore + meditation_time);

                tv_playingTime.setText(minute+":"+second);







            }


        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void quitDBsave(){
        db = FirebaseFirestore.getInstance();
        Log.d("명상 시간", String.valueOf(meditation_time));
        {

            Integer time = PreferenceHelper.getMeditate(this);
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("meditate", time);

            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("diary").document(YMD).set(hashMap, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("명상 시간", time+"밀리초");
                        }

                    });

        }
    }
}
