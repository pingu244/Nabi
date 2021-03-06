package com.example.nabi.fragment.Healing;

import static android.icu.text.UnicodeSet.CASE;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

    TextView tv_title, tv_mention1, tv_mention2, tv_playingTime, tv_playTime;
    ImageButton meditation_btn, preBtn, nextBtn;
    ImageView imageView, btnPrevious;
    int[] meditation_music;
    String[] meditation_mention, meditation_title;
    int[] meditation_img;

    MediaPlayer mediaPlayer;
    ProgressBar progressbar;
    boolean isPlaying = false;
    boolean mentionStart = true;

    Integer meditation_time;
    FirebaseFirestore db;
    java.util.Calendar cal = java.util.Calendar.getInstance();
    int cYEAR = cal.get(java.util.Calendar.YEAR);
    int cMonth = cal.get(java.util.Calendar.MONTH);
    int cDay = cal.get(java.util.Calendar.DATE);
    String YMD = (cYEAR+"/"+(cMonth+1)+"/"+cDay);
    Integer meditation_timeBefore;
    Integer pos;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healing_meditation);

        tv_title = findViewById(R.id.meditation_name);
        tv_mention1 = findViewById(R.id.tv_mention1);
        tv_mention2 = findViewById(R.id.tv_mention2);

        imageView = findViewById(R.id.img_meditation);
        imageView.setClipToOutline(true);    // 모서리 둥글 효과 가능하게 함

        meditation_btn = findViewById(R.id.meditation_play);
        preBtn = findViewById(R.id.meditation_pre);
        nextBtn = findViewById(R.id.meditation_next);

        btnPrevious = findViewById(R.id.btnPrevious);
        tv_playingTime = findViewById(R.id.tv_playingTime);
        tv_playTime = findViewById(R.id.tv_playTime);
        progressbar = findViewById(R.id.meditation_progress);

        meditation_music = new int[] {R.raw.monumental_journey,R.raw.spenta_mainyu, R.raw.spirit_of_fire, R.raw.the_sleeping_prophet, R.raw.venkatesananda };

        meditation_mention = new String[]{"숨을 깊이 들이쉬고 내쉽니다.", "호흡을 깊이 하면서",
                "몸과 마음을 편하게 하고 긴장을 푸세요.", "사랑과 자비로 가득찬\n자신의 모습을 떠올립니다.",
        "입가엔 미소를 가슴엔 평화를 담고서\n이렇게 되뇌입니다.", "나는 하나밖에 없는 귀한 나에게\n자비의 마음을 보냅니다."};

        meditation_title = new String[]{"Monumental Journey","Spenta Mainyu", "Spirit of Fire", "The Sleeping Prophet", "Venkatesananda"};

        meditation_img = new int[] {R.drawable.mdpic_1, R.drawable.mdpic_2, R.drawable.mdpic_3, R.drawable.mdpic_4, R.drawable.mdpic_5};

        Intent intent = getIntent(); /*데이터 수신*/
        pos = intent.getExtras().getInt("number");
//        String title = intent.getExtras().getString("title");
        tv_title.setText(meditation_title[pos]);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), meditation_music[pos]);
        imageView.setImageResource(meditation_img[pos]);

        meditation_btn.setBackgroundResource(R.drawable.btn_pause);
        musicStart();
        new mentionThread().start();


        // 나가기 버튼
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //음악 종료 코드
                isPlaying = false;
                mediaPlayer.stop();
                mediaPlayer.release();

                pushMeditateTime();   // 나갈때 명상시간 파베에

                finish();
            }
        });

        // 재생버튼
        meditation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isPlaying){//정지
                    isPlaying = false;
                    meditation_btn.setBackgroundResource(R.drawable.btn_play);
                    mediaPlayer.pause();
                }else{//재생
                    isPlaying = true;
                    meditation_btn.setBackgroundResource(R.drawable.btn_pause);

                    mediaPlayer.start();
                    new meditationThread().start();

                }

            }
        });


        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //음악 종료 코드
                isPlaying = false;
                mediaPlayer.stop();
                mediaPlayer.release();
                progressbar.setProgress(0);

                pos--;
                if(pos==-1)
                    pos=4;
                tv_title.setText(meditation_title[pos]);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), meditation_music[pos]);
                imageView.setImageResource(meditation_img[pos]);

                meditation_btn.setBackgroundResource(R.drawable.btn_pause);
                musicStart();
            }
        });

        // 다음 음원 재생 버튼
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //음악 종료 코드
                isPlaying = false;
                mediaPlayer.stop();
                mediaPlayer.release();
                progressbar.setProgress(0);




                pos++;
                if(pos==5)
                    pos=0;
                tv_title.setText(meditation_title[pos]);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), meditation_music[pos]);
                imageView.setImageResource(meditation_img[pos]);

                meditation_btn.setBackgroundResource(R.drawable.btn_pause);
                musicStart();
            }
        });





    }

    // 뒤로가기
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        isPlaying = false;
        mediaPlayer.stop();
        mediaPlayer.release();

        pushMeditateTime();   // 나갈때 명상시간 파베에
    }

    public void musicStart(){

        meditation_timeBefore = PreferenceHelper.getMeditate(this);
        isPlaying = true;
        new meditationThread().start();
        mediaPlayer.start();

        int time = mediaPlayer.getDuration();
        SimpleDateFormat sdf = new SimpleDateFormat("m:ss");
        Date timeInDate = new Date(time);
        String timeInFormat = sdf.format(timeInDate);

        tv_playTime.setText(timeInFormat);
        progressbar.setMax(time);
    }

    class meditationThread extends Thread{
        @Override
        public void run() {
            while(isPlaying){  // 음악이 실행중일때 계속 돌아가게 함

                int time = mediaPlayer.getCurrentPosition();
                SimpleDateFormat sdf = new SimpleDateFormat("m:ss");
                Date timeInDate = new Date(time);
                String timeInFormat = sdf.format(timeInDate);

                meditation_time = time;
                PreferenceHelper.setMeditate(getApplicationContext(), meditation_timeBefore + meditation_time);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_playingTime.setText(timeInFormat);
                        progressbar.setProgress(time);
                    }
                });
            }
        }
    }


    class mentionThread extends Thread{
        @Override
        public void run() {
            int i = 0;
            while(mentionStart){  // 음악이 실행중일때 계속 멘트에 변화
                if(isPlaying){
                    int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_mention1.setText(meditation_mention[finalI]);
                            if(finalI == meditation_mention.length-1)
                                tv_mention2.setText(meditation_mention[0]);
                            else
                                tv_mention2.setText(meditation_mention[finalI + 1]);
                        }
                    });
                    if (i == meditation_mention.length - 1) {
                        i = 0;
                    } else {
                        i++;
                    }
                    try{
                        Thread.sleep(20000);    // 20초

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private void pushMeditateTime() {
        db = FirebaseFirestore.getInstance();
        Log.d("명상 시간", String.valueOf(meditation_time));
        Integer time = PreferenceHelper.getMeditate(this);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("meditate", time);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("diary").document(YMD).set(hashMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
    }
}
