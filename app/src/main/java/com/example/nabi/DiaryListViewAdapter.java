package com.example.nabi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//일기 목록 어댑터
public class DiaryListViewAdapter extends RecyclerView.Adapter<DiaryListViewAdapter.ViewHolder> {

    private ArrayList<DiaryListItem> diaryList = new ArrayList<DiaryListItem>();

    @NonNull
    @Override
    public DiaryListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diarylist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryListViewAdapter.ViewHolder holder, int position) {
        holder.onBind(diaryList.get(position));
        holder.title.setText(diaryList.get(position).getTitle());
        holder.date.setText(diaryList.get(position).getDate());

        holder.setLayout();
    }


    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public void setItems(ArrayList<DiaryListItem> items) {
        this.diaryList = items;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutDiaryList;
        TextView date;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutDiaryList = itemView.findViewById(R.id.layoutDiaryList);
            date = (TextView) itemView.findViewById(R.id.diaryDate);
            title = (TextView) itemView.findViewById(R.id.diaryTitle);

        }

        void onBind(DiaryListItem item){
            date.setText(item.getDate());
            title.setText(item.getTitle());
        }

        // 아이템들을 담은 리니어레이아웃을 보여주는 메서드.
        public void setLayout(){
            layoutDiaryList.setVisibility(View.VISIBLE);
        }
    }
}