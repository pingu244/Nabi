package com.example.nabi.fragment.Healing;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nabi.R;

public class Healing_Meditation extends AppCompatActivity {

    TextView tv_title, tv_mention1, tv_mention2, btnPrevious, tv_playingTime, tv_playTime;
    Button btnQuit;
    ImageView imageView;
    int[] meditation_music;

    MediaPlayer mediaPlayer;
    boolean isPlaying = false;

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
                finish();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        isPlaying = false;
        mediaPlayer.stop();
        mediaPlayer.release();
        handler2.removeMessages(0);

    }

    public void musicStart(){

        isPlaying = true;
        new meditationThread().start();
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

                tv_playingTime.setText(minute+":"+second);
            }


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
