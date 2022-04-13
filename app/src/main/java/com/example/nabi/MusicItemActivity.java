package com.example.nabi;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nabi.fragment.Healing.MusicAdapter;
import com.example.nabi.fragment.Healing.MusicListAdapter;
import com.example.nabi.fragment.Healing.MusicPlayActivity;

import java.util.ArrayList;

public class MusicItemActivity extends AppCompatActivity {

    TextView tv_previous;
    Intent intent;
    Integer number;
    String title;
    MusicAdapter musicAdapter;
    ArrayList<MusicAdapter.MusicItem> musicItems;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicitem);

        tv_previous = findViewById(R.id.btnPrevious);

        tv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.music_list_view);

        //음악 리스트 어댑터 연결
        musicItems = new ArrayList<>();
        musicAdapter =new MusicAdapter(musicItems);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(musicAdapter);

        intent = getIntent();
        number = intent.getIntExtra("number", -1);
        title = intent.getStringExtra("title");

            switch (number){
                case 0: //감각적인
                    musicItems.add(new MusicAdapter.MusicItem("Ambient Drum and Bass Music",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("cycles",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("gravity",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Lude_Illa",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Ride the runway",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("You_Had_To_Be",  R.drawable.ic_baseline_ac_unit_24));
                    break;

                case 1: //밝은
                    musicItems.add(new MusicAdapter.MusicItem("New Day",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Sax",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("PhotoGraph",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Birds",  R.drawable.ic_baseline_ac_unit_24));
                    break;

                case 2: //새벽감성
                    musicItems.add(new MusicAdapter.MusicItem("Awake",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Long_Walks",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Mind_And_Eye_Journey",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Neither_Sweat_Nor_Tears",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Rain and Tears",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Sea_Space",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("The Bluest Star",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Where She Walks",  R.drawable.ic_baseline_ac_unit_24));
                    break;

                case 3: //수면
                    musicItems.add(new MusicAdapter.MusicItem("Dreaming Blue",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Meeting Again",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Nidra_in_the_Sky_with_Alyer",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Serenity",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Water_Lilies",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Waterfall",  R.drawable.ic_baseline_ac_unit_24));
                    break;

                case 4: //신나는
                    musicItems.add(new MusicAdapter.MusicItem("passion",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Rainbow",  R.drawable.ic_baseline_ac_unit_24));
                    break;

                case 5: //잔잔한 피아노곡
                    musicItems.add(new MusicAdapter.MusicItem("Fugue_Lullaby",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Heavenly",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Lifting_Dreams",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("National Express",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Simple Sonata",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("White_River",  R.drawable.ic_baseline_ac_unit_24));
                    break;

                case 6: //편안한
                    musicItems.add(new MusicAdapter.MusicItem("A_Quiet_Thought",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Almost Winter",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("dawn",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Rainbow_Forest",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("wedding",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Sweet_as_Honey",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("Snack_Time",  R.drawable.ic_baseline_ac_unit_24));
                    break;

                case 7: //ASMR
                    musicItems.add(new MusicAdapter.MusicItem("FIRE SOUNDS",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("RAIN_SHOWER",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("RAIN_SOUNDS_1HR",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("RAIN_SHORT",  R.drawable.ic_baseline_ac_unit_24));
                    musicItems.add(new MusicAdapter.MusicItem("WATERSTREAM",  R.drawable.ic_baseline_ac_unit_24));
                    break;
            }


    }
}
