package com.example.nabi.fragment.Healing;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.nabi.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MusicPlayActivity extends AppCompatActivity {


    CircularProgressBar progressBar;
    CircularImageView musicPlay_img;
    TextView tv_title, tv_playingTime, tv_playTime, tv_category;
    ImageButton btnPlay, btnPlayNext, btnPlayPre, btnPrevious;
    int[] songs_sensitive, songs_happy, songs_dawn, songs_sleep, songs_exciting, songs_piano, songs_comfort, songs_asmr;
    String [] title_sensitive, title_happy, title_dawn, title_sleep, title_exciting, title_piano, title_comfort, title_asmr;
    Integer[] imgs_sensitive, imgs_happy, imgs_dawn, imgs_sleep, imgs_exciting, imgs_piano, imgs_comfort, imgs_asmr;

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
        musicPlay_img = findViewById(R.id.musicPlay_img);

        tv_title = findViewById(R.id.music_name);
        tv_title.setSingleLine(true);    // 한줄로 표시하기
        tv_title.setEllipsize(TextUtils.TruncateAt.MARQUEE); // 흐르게 만들기
        tv_title.setSelected(true);      // 선택하기


        tv_playingTime = findViewById(R.id.tv_playingTime);
        tv_playTime = findViewById(R.id.tv_playTime);
        tv_category = findViewById(R.id.music_category);

        Intent intent = getIntent(); /*데이터 수신*/
        category = intent.getExtras().getInt("category");
        final int[] pos = {intent.getExtras().getInt("position")};
        String title = intent.getExtras().getString("title");
        songs_sensitive = new int[]{R.raw.ambientdrumandbassmusic, R.raw.cycles, R.raw.gravity, R.raw.ludeilla, R.raw.ridetherunway, R.raw.you_had_to_be};//감각적인
        songs_happy = new int[]{R.raw.new_day, R.raw.sax, R.raw.photograph, R.raw.birds};//밝은
        songs_dawn = new int[]{R.raw.awake, R.raw.long_walks, R.raw.mind_and_eye_journey, R.raw.neither_sweat_nor_tears, R.raw.rain_and_tears, R.raw.sea_space, R.raw.the_bluest_star, R.raw.where_she_walks};//새벽감성
        songs_sleep = new int[]{R.raw.dreaming_blue, R.raw.meeting_again, R.raw.nidra_in_the_sky_with_ayler, R.raw.serenity, R.raw.water_lillies, R.raw.waterfall};//수면
        songs_exciting = new int[]{R.raw.passion, R.raw.rainbow}; //신나는
        songs_piano = new int[]{R.raw.fugue_lullaby, R.raw.heavenly, R.raw.lifting_dreams, R.raw.national_express, R.raw.simple_sonata, R.raw.white_river}; //피아노곡
        songs_comfort = new int[]{R.raw.a_quiet_thought, R.raw.almost_winter, R.raw.dawn, R.raw.rainbow_forest, R.raw.wedding, R.raw.sweet_as_honey, R.raw.snack_time}; //편안한
        songs_asmr = new int[]{R.raw.fire_sounds, R.raw.rain_shower, R.raw.rain_sounds_lh, R.raw.rain_short, R.raw.waterstream}; //ASMR


        title_sensitive = new String[]{"Ambient Drum and Bass Music", "cycles", "gravity", "Lude_Illa", "Ride the runway", "You_Had_To_Be"};
        title_happy = new String[]{"New Day", "Sax", "PhotoGraph", "Birds"};
        title_dawn = new String[]{"Awake", "Long_Walks","Mind_And_Eye_Journey","Neither_Sweat_Nor_Tears","Rain and Tears", "Sea_Space", "The Bluest Star", "Where She Walks"};
        title_sleep = new String[]{"Dreaming Blue", "Meeting Again" ,"Nidra_in_the_Sky_with_Alyer","Serenity","Water_Lilies","Waterfall"};
        title_exciting = new String[]{"passion", "Rainbow"};
        title_piano = new String[]{"Fugue_Lullaby", "Heavenly", "Lifting_Dreams", "National Express", "Simple Sonata","White_River"};
        title_comfort = new String[]{"A_Quiet_Thought", "Almost Winter", "dawn", "Rainbow_Forest", "wedding", "Sweet_as_Honey", "Snack_Time"};
        title_asmr = new String[]{"FIRE SOUNDS", "RAIN_SHOWER", "RAIN_SOUNDS_1HR", "RAIN_SHORT", "WATERSTREAM"};

        imgs_sensitive = new Integer[]{R.drawable.sens_drum, R.drawable.sens_cycle, R.drawable.sens_gravity, R.drawable.sens_lude, R.drawable.sens_ride, R.drawable.sens_you};
        imgs_happy = new Integer[]{R.drawable.cheer_new, R.drawable.cheer_sax, R.drawable.cheer_photo, R.drawable.cheerful_birds};
        imgs_dawn = new Integer[]{R.drawable.dawn_awake, R.drawable.dawn_longwalks, R.drawable.dawn_journey, R.drawable.dawn_neither, R.drawable.dawn_rain, R.drawable.dawn_sea, R.drawable.dawn_star, R.drawable.dawn_walk};
        imgs_sleep = new Integer[]{R.drawable.sleep_blue, R.drawable.sleep_meet, R.drawable.sleep_sky, R.drawable.sleep_serenity, R.drawable.sleep_water, R.drawable.sleep_waterfall};
        imgs_exciting = new Integer[]{R.drawable.exciting_passion, R.drawable.exciting_rainbow};
        imgs_piano = new Integer[]{R.drawable.piano_lullaby, R.drawable.piano_heaven, R.drawable.piano_dream, R.drawable.piano_express, R.drawable.piano_sonata, R.drawable.piano_river};
        imgs_comfort = new Integer[]{R.drawable.comfort_quiet, R.drawable.comfort_winter, R.drawable.comfort_dawn, R.drawable.comfort_rainbow, R.drawable.comfort_wedding, R.drawable.comfort_sweet, R.drawable.comfort_snack};
        imgs_asmr = new Integer[]{R.drawable.asmr_fire, R.drawable.asmr_rainshower, R.drawable.asmr_rainsound,  R.drawable.asmr_rain, R.drawable.asmr_water};

        //mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ambientdrumandbassmusic);// new를 쓰는 것이 아니라 플레이어 생성
        if (category==0){ //감각적인
            mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_sensitive[pos[0]]);
            tv_title.setText(title);
            tv_category.setText("감각적인");
            Glide.with(this).load(imgs_sensitive[pos[0]]).into(musicPlay_img);

        }
        else if(category==1){//행복한
            mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_happy[pos[0]]);
            tv_title.setText(title);
            tv_category.setText("밝은");
            Glide.with(this).load(imgs_happy[pos[0]]).into(musicPlay_img);

        }
        else if(category==2){//새벽감성
            mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_dawn[pos[0]]);
            tv_title.setText(title);
            tv_category.setText("새벽감성");
            Glide.with(this).load(imgs_dawn[pos[0]]).into(musicPlay_img);

        }
        else if(category==3){//수면
            mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_sleep[pos[0]]);
            tv_title.setText(title);
            tv_category.setText("수면");
            Glide.with(this).load(imgs_sleep[pos[0]]).into(musicPlay_img);

        }
        else if(category==4){//신나는
            mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_exciting[pos[0]]);
            tv_title.setText(title);
            tv_category.setText("신나는");
            Glide.with(this).load(imgs_exciting[pos[0]]).into(musicPlay_img);

        }
        else if(category==5){//피아노
            mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_piano[pos[0]]);
            tv_title.setText(title);
            tv_category.setText("잔잔한");
            Glide.with(this).load(imgs_piano[pos[0]]).into(musicPlay_img);

        }
        else if(category==6){//편안한
            mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_comfort[pos[0]]);
            tv_title.setText(title);
            tv_category.setText("편안한");
            Glide.with(this).load(imgs_comfort[pos[0]]).into(musicPlay_img);

        }
        else if(category==7){//ASMR
            mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_asmr[pos[0]]);
            tv_title.setText(title);
            tv_category.setText("ASMR");
            Glide.with(this).load(imgs_asmr[pos[0]]).into(musicPlay_img);

        }
        btnPlay.setImageResource(R.drawable.btn_pause);
        musicStart();


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

                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.btn_play);
                    //현재 상태 저장
//                    currentPos = mediaPlayer.getCurrentPosition();

                }else{
                    isPlaying = true;


                    //저장한 위치에서 다시 시작
//                    mediaPlayer.seekTo(currentPos);
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.btn_pause);
//                    new musicThread().start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(isPlaying){  // 음악이 실행중일때 계속 돌아가게 함

                                int time = mediaPlayer.getCurrentPosition();
                                progressBar.setProgress(time);
                                SimpleDateFormat sdf = new SimpleDateFormat("m:ss");
                                Date timeInDate = new Date(time);
                                String timeInFormat = sdf.format(timeInDate);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_playingTime.setText(timeInFormat);
                                    }
                                });
                                if(progressBar.getProgress()==progressBar.getProgressMax()){
                                    mediaPlayer.stop();
                                    isPlaying = false;
                                }
                            }
                        }
                    }).start();
                }
            }
        });

        //이전 곡 재생
        btnPlayPre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // 1초 전 상태에서 이전 버튼 누르면 앞 음악으로,
                // 1초 지나고 이전 버튼 누르면 맨 처음으로
                if(mediaPlayer.getCurrentPosition() > 1000){
                    isPlaying = true;
                    btnPlay.setImageResource(R.drawable.btn_pause);
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(isPlaying){  // 음악이 실행중일때 계속 돌아가게 함

                                int time = mediaPlayer.getCurrentPosition();
                                progressBar.setProgress(time);
                                SimpleDateFormat sdf = new SimpleDateFormat("m:ss");
                                Date timeInDate = new Date(time);
                                String timeInFormat = sdf.format(timeInDate);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_playingTime.setText(timeInFormat);
                                    }
                                });
                                if(progressBar.getProgress()==progressBar.getProgressMax()){
                                    mediaPlayer.stop();
                                    isPlaying = false;
                                }
                            }
                        }
                    }).start();

                }
                else{
                    if (isPlaying) {
                        mediaPlayer.stop();
                        isPlaying = false;
                        mediaPlayer.release();
                    }
                    if (category==0){ //감각적인
                        pos[0]--;

                        if(pos[0]<0){
                            pos[0]=songs_sensitive.length-1;
                        }
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_sensitive[pos[0]]);
                        tv_title.setText(title_sensitive[pos[0]]);
                        tv_category.setText("감각적인");
                        Glide.with(getApplicationContext()).load(imgs_sensitive[pos[0]]).into(musicPlay_img);

                        musicStart();


                    }
                    else if(category==1){//행복한
                        pos[0]--;

                        if(pos[0]<0){
                            pos[0]=songs_happy.length-1;
                        }
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_happy[pos[0]]);
                        tv_title.setText(title_happy[pos[0]]);
                        tv_category.setText("밝은");
                        Glide.with(getApplicationContext()).load(imgs_happy[pos[0]]).into(musicPlay_img);
                        musicStart();
                    }
                    else if(category==2){//새벽감성
                        pos[0]--;

                        if(pos[0]<0){
                            pos[0]=songs_dawn.length-1;
                        }
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_dawn[pos[0]]);
                        tv_title.setText(title_dawn[pos[0]]);
                        tv_category.setText("새벽감성");
                        Glide.with(getApplicationContext()).load(imgs_dawn[pos[0]]).into(musicPlay_img);
                        musicStart();

                    }
                    else if(category==3){//수면

                        pos[0]--;

                        if(pos[0]<0){
                            pos[0]=songs_sleep.length-1;
                        }
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_sleep[pos[0]]);
                        tv_title.setText(title_sleep[pos[0]]);
                        tv_category.setText("수면");
                        Glide.with(getApplicationContext()).load(imgs_sleep[pos[0]]).into(musicPlay_img);
                        musicStart();

                    }
                    else if(category==4){//신나는
                        pos[0]--;

                        if(pos[0]<0){
                            pos[0]=songs_exciting.length-1;
                        }
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_exciting[pos[0]]);
                        tv_title.setText(title_exciting[pos[0]]);
                        tv_category.setText("신나는");
                        Glide.with(getApplicationContext()).load(imgs_exciting[pos[0]]).into(musicPlay_img);
                        musicStart();

                    }
                    else if(category==5){//피아노
                        pos[0]--;

                        if(pos[0]<0){
                            pos[0]=songs_piano.length-1;
                        }
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_piano[pos[0]]);
                        tv_title.setText(title_piano[pos[0]]);
                        tv_category.setText("잔잔한");
                        Glide.with(getApplicationContext()).load(imgs_piano[pos[0]]).into(musicPlay_img);
                        musicStart();
                    }
                    else if(category==6) {//편안한
                        pos[0]--;

                        if (pos[0]<0) {
                            pos[0] = songs_comfort.length-1;
                        }
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_comfort[pos[0]]);
                        tv_title.setText(title_comfort[pos[0]]);
                        tv_category.setText("편안한");
                        Glide.with(getApplicationContext()).load(imgs_comfort[pos[0]]).into(musicPlay_img);
                        musicStart();

                    }
                    else if(category==7){//ASMR
                        pos[0]--;

                        if(pos[0]<0){
                            pos[0]=songs_asmr.length-1;
                        }
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_asmr[pos[0]]);
                        tv_title.setText(title_asmr[pos[0]]);
                        tv_category.setText("ASMR");
                        Glide.with(getApplicationContext()).load(imgs_asmr[pos[0]]).into(musicPlay_img);
                        musicStart();

                    }
                }

            }
        });

        //다음 곡 재생
        btnPlayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying){
                    mediaPlayer.stop();
                    isPlaying = false;
                    mediaPlayer.release();

                }

                if (category==0){ //감각적인
                    pos[0]++;

                    if(pos[0]==songs_sensitive.length){
                                pos[0]=0;
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_sensitive[pos[0]]);
                    tv_title.setText(title_sensitive[pos[0]]);
                    tv_category.setText("감각적인");
                    Glide.with(getApplicationContext()).load(imgs_sensitive[pos[0]]).into(musicPlay_img);

                    musicStart();


                }else if(category==1){//행복한
                    pos[0]++;

                    if(pos[0]==songs_happy.length){
                        pos[0]=0;
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_happy[pos[0]]);
                    tv_title.setText(title_happy[pos[0]]);
                    tv_category.setText("밝은");
                    Glide.with(getApplicationContext()).load(imgs_happy[pos[0]]).into(musicPlay_img);
                    musicStart();
                }
                else if(category==2){//새벽감성
                    pos[0]++;

                    if(pos[0]==songs_dawn.length){
                        pos[0]=0;
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_dawn[pos[0]]);
                    tv_title.setText(title_dawn[pos[0]]);
                    tv_category.setText("새벽감성");
                    Glide.with(getApplicationContext()).load(imgs_dawn[pos[0]]).into(musicPlay_img);
                    musicStart();

                }else if(category==3){//수면

                    pos[0]++;

                    if(pos[0]==songs_sleep.length){
                        pos[0]=0;
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_sleep[pos[0]]);
                    tv_title.setText(title_sleep[pos[0]]);
                    tv_category.setText("수면");
                    Glide.with(getApplicationContext()).load(imgs_sleep[pos[0]]).into(musicPlay_img);
                    musicStart();

                }else if(category==4){//신나는
                    pos[0]++;

                    if(pos[0]==songs_exciting.length){
                        pos[0]=0;
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_exciting[pos[0]]);
                    tv_title.setText(title_exciting[pos[0]]);
                    tv_category.setText("신나는");
                    Glide.with(getApplicationContext()).load(imgs_exciting[pos[0]]).into(musicPlay_img);
                    musicStart();

                }else if(category==5){//피아노
                    pos[0]++;

                    if(pos[0]==songs_piano.length){
                        pos[0]=0;
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_piano[pos[0]]);
                    tv_title.setText(title_piano[pos[0]]);
                    tv_category.setText("잔잔한");
                    Glide.with(getApplicationContext()).load(imgs_piano[pos[0]]).into(musicPlay_img);
                    musicStart();
                }else if(category==6) {//편안한
                    pos[0]++;

                    if (pos[0] == songs_comfort.length) {
                        pos[0] = 0;
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_comfort[pos[0]]);
                    tv_title.setText(title_comfort[pos[0]]);
                    tv_category.setText("편안한");
                    Glide.with(getApplicationContext()).load(imgs_comfort[pos[0]]).into(musicPlay_img);
                    musicStart();

                }else if(category==7){//ASMR
                    pos[0]++;

                    if(pos[0]==songs_asmr.length){
                        pos[0]=0;
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), songs_asmr[pos[0]]);
                    tv_title.setText(title_asmr[pos[0]]);
                    tv_category.setText("ASMR");
                    Glide.with(getApplicationContext()).load(imgs_asmr[pos[0]]).into(musicPlay_img);
                    musicStart();

                }


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
        btnPlay.setImageResource(R.drawable.btn_pause);

//        new musicThread().start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isPlaying){  // 음악이 실행중일때 계속 돌아가게 함

                    int time = mediaPlayer.getCurrentPosition();
                    progressBar.setProgress(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("m:ss");
                    Date timeInDate = new Date(time);
                    String timeInFormat = sdf.format(timeInDate);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_playingTime.setText(timeInFormat);
                        }
                    });
                    if(progressBar.getProgress()==progressBar.getProgressMax()){
                        mediaPlayer.stop();
                        isPlaying = false;
                    }
                }
            }
        }).start();

        mediaPlayer.start();
        progressBar.setProgress(0);
        progressBar.setProgressMax(mediaPlayer.getDuration());

        int time = mediaPlayer.getDuration();
        SimpleDateFormat sdf = new SimpleDateFormat("m:ss");
        Date timeInDate = new Date(time);
        String timeInFormat = sdf.format(timeInDate);

        tv_playTime.setText(timeInFormat);
    }

    class musicThread extends Thread{

        @Override
        public void run() {
            while(isPlaying){  // 음악이 실행중일때 계속 돌아가게 함

                int time = mediaPlayer.getCurrentPosition();
                progressBar.setProgress(time);
                SimpleDateFormat sdf = new SimpleDateFormat("m:ss");
                Date timeInDate = new Date(time);
                String timeInFormat = sdf.format(timeInDate);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_playingTime.setText(timeInFormat);
                    }
                });
                if(progressBar.getProgress()==progressBar.getProgressMax()){
                    mediaPlayer.stop();
                    isPlaying = false;
                    btnPlay.setImageResource(R.drawable.btn_play);
                }
            }
        }


    }

}
