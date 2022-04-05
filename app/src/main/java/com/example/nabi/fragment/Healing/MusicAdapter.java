package com.example.nabi.fragment.Healing;

import android.content.Intent;
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

    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MusicAdapter.MusicItem vo = list.get(position);
        holder.tv_name.setText(vo.musicTitle);
        holder.img_main.setImageResource(vo.imageView);


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

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
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}