package com.example.nabi.fragment.Healing;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nabi.R;

public class MusicPlayActivity extends AppCompatActivity {


    ProgressBar progressBar;
    TextView btnPrevious, tv_title, tv_playingTime, tv_playTime, tv_category;
    ImageButton btnPlay, btnPlayNext, btnPlayPre;
    int[] songs_sensitive, songs_happy, songs_dawn, songs_sleep, songs_exciting, songs_piano, songs_comfort, songs_asmr;
    MediaPlayer mediaPlayer;
    int currentPos, category;

    boolean isPlaying = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healing_music_play);

        //제목 intent 전달 받은 값 -> 그 음악 틀기. 이전음악, 다음 음악, 일시정지, 재시작, 스레드 연결
        progressBar = findViewById(R.id.music_progressbar);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnPlay = findViewById(R.id.btn_music_play);
        btnPlayNext = findViewById(R.id.btn_music_next);
        btnPlayPre = findViewById(R.id.btn_music_pre);

        tv_title = findViewById(R.id.music_name);
        tv_playingTime = findViewById(R.id.tv_playingTime);
        tv_playTime = findViewById(R.id.tv_playTime);
        tv_category = findViewById(R.id.music_category);

        Intent intent = getIntent(); /*데이터 수신*/
        category = intent.getExtras().getInt("category");
        final int[] pos = {intent.getExtras().getInt("position")};
        String title = intent.getExtras().getString("title");
        songs_sensitive = new int[]{R.raw.ambientdrumandbassmusic, R.raw.cycles, R.raw.gravity, R.raw.ludeilla, R.raw.ridetherunway, R.raw.you_had_to_be};//감각적인
        songs_happy = new int[]{R.raw.birds, R.raw.new_day, R.raw.photograph, R.raw.sax};//밝은
        songs_dawn = new int[]{R.raw.where_she_walks, R.raw.the_bluest_star, R.raw.sea_space, R.raw.neither_sweat_nor_tears, R.raw.rain_and_tears, R.raw.mind_and_eye_journey, R.raw.long_walks, R.raw.awake};//새벽감성
        songs_sleep = new int[]{R.raw.waterfall, R.raw.water_lillies, R.raw.serenity, R.raw.nidra_in_the_sky_with_ayler, R.raw.dreaming_blue, R.raw.meeting_again};//수면
        songs_exciting = new int[]{R.raw.passion, R.raw.rainbow}; //신나는
        songs_piano = new int[]{R.raw.white_river, R.raw.simple_sonata, R.raw.national_express, R.raw.lifting_dreams, R.raw.heavenly, R.raw.fugue_lullaby}; //피아노곡
        songs_comfort = new int[]{R.raw.wedding, R.raw.sweet_as_honey, R.raw.snack_time, R.raw.rainbow_forest, R.raw.dawn, R.raw.andrew_applepie, R.raw.a_quiet_thought}; //편안한
        songs_asmr = new int[]{R.raw.waterstream, R.raw.rain_short, R.raw.rain_sounds_lh, R.raw.rain_shower, R.raw.fire_sounds}; //ASMR

        //mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ambientdrumandbassmusic);// new를 쓰는 것이 아니라 플레이어 생성
        if (category==0){ //감각적인
            for(int i = pos[0]; i<songs_sensitive.length; i++){
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_sensitive[i]);
                tv_title.setText(title);
                tv_category.setText("감각적인");
            }
        }else if(category==1){//행복한
            for(int i = pos[0]; i<songs_happy.length; i++){
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_happy[i]);
                tv_title.setText(title);
                tv_category.setText("행복한");
            }
        }else if(category==2){//새벽감성
            for(int i = pos[0]; i<songs_dawn.length; i++){
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_dawn[i]);
                tv_title.setText(title);
                tv_category.setText("새벽감성");
            }
        }else if(category==3){//수면
            for(int i = pos[0]; i<songs_sleep.length; i++){
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_sleep[i]);
                tv_title.setText(title);
                tv_category.setText("수면");
            }
        }else if(category==4){//신나는
            for(int i = pos[0]; i<songs_exciting.length; i++){
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_exciting[i]);
                tv_title.setText(title);
                tv_category.setText("신나는");
            }
        }else if(category==5){//피아노
            for(int i = pos[0]; i<songs_piano.length; i++){
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_piano[i]);
                tv_title.setText(title);
                tv_category.setText("피아노 음악");
            }
        }else if(category==6){//편안한
            for(int i = pos[0]; i<songs_comfort.length; i++){
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_comfort[i]);
                tv_title.setText(title);
                tv_category.setText("편안한");
            }
        }else if(category==7){//ASMR
            for(int i = pos[0]; i<songs_asmr.length; i++){
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_asmr[i]);
                tv_title.setText(title);
                tv_category.setText("ASMR");
            }
        }

        musicStart();
        btnPlay.setImageResource(R.drawable.ic_baseline_pause_24);

        btnPrevious.setOnClickListener(new View.OnClickListener() { //나가기
            @Override
            public void onClick(View view) {
                isPlaying = false;
                mediaPlayer.stop();
                mediaPlayer.release();
                finish();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    isPlaying = false;
                    btnPlay.setImageResource(R.drawable.mcv_action_next);
                    mediaPlayer.pause();

                    //현재 상태 저장
                    currentPos = mediaPlayer.getCurrentPosition();

                }else{
                    isPlaying = true;
                    btnPlay.setImageResource(R.drawable.ic_baseline_pause_24);

                    //저장한 위치에서 다시 시작
                    mediaPlayer.seekTo(currentPos);
                    mediaPlayer.start();
                    new musicThread().start();

                }
            }
        });

        //이전 곡 재생
        btnPlayPre.setOnClickListener(new View.OnClickListener() {

            int index = 0;
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    //getMusicInfo();
                    index-=1;
                    if(index<0){
                        if(category==0){
                            index = songs_sensitive.length-1;
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_sensitive[index]);
                        }else if(category==1){
                            index = songs_happy.length-1;
                        }else if(category==2){
                            index = songs_dawn.length-1;
                        }else if (category==3){
                            index = songs_sleep.length-1;
                        }else if(category==4){
                            index = songs_exciting.length-1;
                        }else if(category==5){
                            index = songs_piano.length-1;
                        }else if(category==6){
                            index = songs_comfort.length-1;
                        }else if(category==7){
                            index = songs_asmr.length-1;
                        }

                    }

                }
            }

        });

        //다음 곡 재생
        btnPlayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        isPlaying = false;
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public void musicStart() {
        isPlaying = true;
        btnPlay.setImageResource(R.drawable.ic_baseline_pause_24);
        new musicThread().start();
        mediaPlayer.start();
        progressBar.setProgress(0);
        progressBar.setMax(mediaPlayer.getDuration());

        //tv_playTime.setText("/" + mediaPlayer.getDuration()/60);

        int minute, second, time;
        time = mediaPlayer.getDuration();
        minute = (time / (1000*60)) % 60;
        second = (time / 1000) % 60;

        tv_playTime.setText("/"+minute+":"+second);
    }

    class musicThread extends Thread{

        @Override
        public void run() {
            while(isPlaying){  // 음악이 실행중일때 계속 돌아가게 함

                progressBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.sendEmptyMessage(1);
                if(progressBar.getProgress()==progressBar.getMax()){
                    mediaPlayer.stop();
                    isPlaying = false;
                }
            }
        }

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                //밀리초 시간 변환
                int minute, second, time;

                time = mediaPlayer.getCurrentPosition();
                minute = (time / (1000*60)) % 60;
                second = (time / 1000) % 60;

                tv_playingTime.setText(minute+":"+second);

            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
