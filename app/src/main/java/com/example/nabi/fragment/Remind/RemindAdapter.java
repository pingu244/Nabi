package com.example.nabi.fragment.Remind;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.example.nabi.fragment.Diary.DiaryListItem;
import com.example.nabi.fragment.Healing.MusicListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RemindAdapter extends RecyclerView.Adapter<RemindAdapter.ViewHolder> {



    private List<RemindAdapter.RemindItem> list = new ArrayList<RemindAdapter.RemindItem>();
    public RemindAdapter(List<RemindAdapter.RemindItem> list){
        this.list=list;
    }

    @Override
    public RemindAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_list_item, parent, false);

        return new RemindAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindAdapter.ViewHolder holder, int position) {
        holder.onBind(list.get(position));
        holder.remind_date.setText(list.get(position).getRemind_date());
//        holder.setLayout();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void addItem(ArrayList<RemindItem> data) {
        // 외부에서 item을 추가시킬 함수입니다.
        list = data;
    }

    public static class RemindItem implements Comparable<RemindItem> {

        public RemindItem(String remind_date, Drawable imageView, String date_dbPath, Integer date_day){
            this.remind_date = remind_date;
            this.imageView = imageView;
            this.dbPath = date_dbPath;
            this.date_day = date_day;
        }
        public String getRemind_date() {
            return remind_date;
        }

        public void setRemind_date(String remind_date) {
            this.remind_date = remind_date;
        }

        public Drawable getImageView() {
            return imageView;
        }

        public void setImageView(Drawable imageView) {
            this.imageView = imageView;
        }

        public String getdbPath(){ return dbPath;}

        public String remind_date, dbPath;
        public Drawable imageView;
        Integer date_day;

        // 정렬위한 함수
        @Override
        public int compareTo(RemindItem remindItem) {
            return this.date_day.compareTo(remindItem.date_day);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView remind_date;
        ImageView img_main;
        RecyclerView remindList;
        Intent intent;
        String resultDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            remind_date = itemView.findViewById(R.id.remind_date);
            img_main = itemView.findViewById(R.id.remind_weather);
            remindList = itemView.findViewById(R.id.remind_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    intent = new Intent(view.getContext(), Remind_result.class);
                    intent.putExtra("remindResult_date", resultDate);
                    intent.putExtra("number", pos);
                    view.getContext().startActivity(intent);
                }
            });
        }
        void onBind(RemindItem item) {
            resultDate = item.getdbPath();
            remind_date.setText(item.getRemind_date());
            img_main.setImageDrawable(item.getImageView());
        }
//        // 아이템들을 담은 리니어레이아웃을 보여주는 메서드.
//        public void setLayout(){
//            remindList.setVisibility(View.VISIBLE);
//        }
    }

}
