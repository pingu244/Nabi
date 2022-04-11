package com.example.nabi.fragment.Healing;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nabi.MusicItemActivity;
import com.example.nabi.R;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{

    private List<MusicAdapter.MusicItem> list = new ArrayList<MusicAdapter.MusicItem>();
    public MusicAdapter(List<MusicAdapter.MusicItem> list){
        this.list=list;
    }
    Context context;
    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MusicAdapter.MusicItem vo = list.get(position);
        holder.tv_name.setText(vo.musicTitle);
        holder.img_main.setImageResource(vo.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"클릭",Toast.LENGTH_SHORT);
                Intent intent = new Intent(context, MusicPlayActivity.class);

                //String name = list.get(getAdapterPosition()).getMusicTitle();
                intent.putExtra("title", vo.getMusicTitle());

                context.startActivity(intent);

            }
        });
    }
    //아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener{
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
    public MusicItem getItem(int position){return list.get(position);}
    public static class MusicItem {

        public MusicItem(String musicTitle, Integer imageView){
            this.musicTitle = musicTitle;
            this.imageView = imageView;
        }



        public int getImageView() {
            return imageView;
        }

        public void setImageView(int imageView) {
            this.imageView = imageView;
        }

        public String getMusicTitle() {
            return musicTitle;
        }

        public void setMusicTitle(String musicTitle) {
            this.musicTitle = musicTitle;
        }

        public String musicTitle;
        public int imageView;

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        ImageView img_main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.music_title);
            img_main = itemView.findViewById(R.id.music_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                int pos = getAdapterPosition();


                @Override
                public void onClick(View view) {

                    if (pos!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            mListener.onItemClick (view,pos);
                        }
                    }
                }
            });
        }
    }
}