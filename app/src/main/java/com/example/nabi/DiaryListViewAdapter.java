package com.example.nabi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//일기 목록 어댑터
public class DiaryListViewAdapter extends RecyclerView.Adapter<DiaryListViewAdapter.ViewHolder> {

    private ArrayList<DiaryListItem> diaryList;

    @NonNull
    @Override
    public DiaryListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diarylist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryListViewAdapter.ViewHolder holder, int position) {
        holder.onBind(diaryList.get(position));
    }

    public void setDiaryList(ArrayList<DiaryListItem> list){
        this.diaryList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.diaryDate);
            title = (TextView) itemView.findViewById(R.id.diaryTitle);

        }

        void onBind(DiaryListItem item){
            date.setText(item.getDate());
            title.setText(item.getTitle());
        }
    }
}