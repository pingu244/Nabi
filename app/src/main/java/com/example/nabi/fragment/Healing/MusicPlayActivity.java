package com.example.nabi.fragment.Healing;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nabi.R;

public class MusicPlayActivity extends AppCompatActivity {


    ProgressBar progressBar;
    TextView btnPrevious;
    ImageButton btnPlay, btnPlayNext, btnPlayPre;
    int[] songs_sensitive, songs_happy, songs_dawn, songs_sleep, songs_exciting, songs_piano, songs_comfort, songs_asmr;
    MediaPlayer mediaPlayer;
    int index = 0;

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

        songs_sensitive = new int[]{R.raw.ambientdrumandbassmusic, R.raw.cycles, R.raw.gravity, R.raw.ludeilla, R.raw.ridetherunway, R.raw.you_had_to_be};//감각적인
        songs_happy = new int[]{R.raw.birds, R.raw.new_day, R.raw.photograph, R.raw.sax};//밝은
        songs_dawn = new int[]{R.raw.where_she_walks, R.raw.the_bluest_star, R.raw.sea_space, R.raw.neither_sweat_nor_tears, R.raw.rain_and_tears, R.raw.mind_and_eye_journey, R.raw.long_walks, R.raw.awake};//새벽감성
        songs_sleep = new int[]{R.raw.waterfall, R.raw.water_lillies, R.raw.serenity, R.raw.nidra_in_the_sky_with_ayler, R.raw.dreaming_blue, R.raw.meeting_again};//수면
        songs_exciting = new int[]{R.raw.passion, R.raw.rainbow}; //신나는
        songs_piano = new int[]{R.raw.white_river, R.raw.simple_sonata, R.raw.national_express, R.raw.lifting_dreams, R.raw.heavenly, R.raw.fugue_lullaby}; //피아노곡
        songs_comfort = new int[]{R.raw.wedding, R.raw.sweet_as_honey, R.raw.snack_time, R.raw.rainbow_forest, R.raw.dawn, R.raw.andrew_applepie, R.raw.a_quiet_thought}; //편안한
        songs_asmr = new int[]{R.raw.waterstream, R.raw.rain_short, R.raw.rain_sounds_lh, R.raw.rain_shower, R.raw.fire_sounds}; //ASMR

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ambientdrumandbassmusic);// new를 쓰는 것이 아니라 플레이어 생성
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    //btnPlay.setText("START");

                } else {
                    //getApplicationContext() 현재 액티비티 정보얻어오기
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_sensitive[index]);
                    mediaPlayer.start();
                    //btnPlay.setText("STOP");
                }
            }
        });

        btnPlayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    index += 1;
                    if (index >= songs_sensitive.length) {
                        index = 0;
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_sensitive[index]);
                    mediaPlayer.start();
                } else {
                    Toast.makeText(getApplicationContext(), "재생중이 아닙니다", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnPlayPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    index -= 1;
                    if (index < 0) {
                        index = songs_sensitive.length - 1;
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_sensitive[index]);
                    mediaPlayer.start();
                } else {
                    Toast.makeText(getApplicationContext(), "재생중이 아닙니다", Toast.LENGTH_LONG).show();
                }


            }
        });




        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
    }


}
