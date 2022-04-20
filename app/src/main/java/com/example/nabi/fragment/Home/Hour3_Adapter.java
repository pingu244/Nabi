package com.example.nabi.fragment.Home;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nabi.R;

import java.util.ArrayList;
import java.util.List;

public class Hour3_Adapter extends RecyclerView.Adapter<Hour3_Adapter.ViewHolder> {

    private List<ItemData> list = new ArrayList<ItemData>();


    public Hour3_Adapter(List<ItemData> list){
        this.list=list;
    }

    @NonNull
    @Override
    public Hour3_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hour3_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemData vo = list.get(position);
        holder.tv_time.setText(vo.time);
        holder.tv_temp.setText(vo.temp+"°");
        holder.img_temp.setImageResource(vo.imageView);
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemData {
        public String time;
        public int temp;
        public int imageView;
        public String main;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getTemp() {
            return temp;
        }

        public void setTemp(int temp) {
            this.temp = temp;
        }

        public int getImageView() {
            return imageView;
        }

        public void setImageView(int imageView) {
            this.imageView = imageView;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_time,tv_temp;
        ImageView img_temp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_time = itemView.findViewById(R.id.hour3_item_time);
            tv_temp = itemView.findViewById(R.id.hour3_item_temp);
            img_temp = itemView.findViewById(R.id.hour3_item_image);

        }
    }
}
