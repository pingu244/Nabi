package com.example.nabi.fragment.Healing;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nabi.MusicItemActivity;
import com.example.nabi.R;

import java.util.ArrayList;
import java.util.List;

public class MeditationAdapter extends RecyclerView.Adapter<MeditationAdapter.ViewHolder>{


    private List<MeditationAdapter.MeditationItem> list = new ArrayList<MeditationItem>();
    public MeditationAdapter(List<MeditationAdapter.MeditationItem> list){
        this.list=list;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public MeditationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meditation_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MeditationAdapter.MeditationItem vo = list.get(position);
        holder.tv_title.setText(vo.meditation_title);
        holder.tv_playTime.setText(vo.playTime);
        holder.img_main.setImageResource(vo.imageView);


    }

    //아이템 클릭 리스너 인터페이스
    interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }
    //리스너 객체 참조 변수
    private OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MeditationItem {

        public MeditationItem(String meditation_title, String playTime, Integer imageView){
            this.meditation_title = meditation_title;
            this.playTime = playTime;
            this.imageView = imageView;
        }
        public String getMusicCategory() {
            return meditation_title;
        }

        public void setMusicCategory(String musicCategory) {
            this.meditation_title = musicCategory;
        }

        public int getImageView() {
            return imageView;
        }

        public void setImageView(int imageView) {
            this.imageView = imageView;
        }

        public String meditation_title, playTime;
        public int imageView;

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_playTime;
        ImageView img_main;
        Intent intent;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.me_title);
            tv_title.setSingleLine(true);    // 한줄로 표시하기
            tv_title.setEllipsize(TextUtils.TruncateAt.MARQUEE); // 흐르게 만들기
            tv_title.setSelected(true);      // 선택하기
            
            tv_playTime = itemView.findViewById(R.id.me_playTime);
            img_main = itemView.findViewById(R.id.me_item_image);
            img_main.setClipToOutline(true);    // 모서리 둥글 효과 가능하게 함

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
//                    String title = (String) tv_title.getText();
                    intent = new Intent(view.getContext(), Healing_Meditation.class);
                    intent.putExtra("number", pos);
//                    intent.putExtra("title", title);
                    view.getContext().startActivity(intent);

//                    Toast.makeText(view.getContext(), "클릭 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}