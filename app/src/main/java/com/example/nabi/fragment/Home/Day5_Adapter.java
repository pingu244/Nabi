package com.example.nabi.fragment.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nabi.R;

import java.util.ArrayList;
import java.util.List;

public class Day5_Adapter extends RecyclerView.Adapter<Day5_Adapter.ViewHolder>{

    private List<Day5_Adapter.ItemData> list = new ArrayList<Day5_Adapter.ItemData>();
    public Day5_Adapter(List<Day5_Adapter.ItemData> list){
        this.list=list;
    }

    @NonNull
    @Override
    public Day5_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day5_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Day5_Adapter.ViewHolder holder, int position) {

        Day5_Adapter.ItemData vo = list.get(position);
        holder.tv_day.setText(vo.day);
        holder.tv_temp.setText(vo.temp);
        holder.img_main.setImageResource(vo.imageView);
        holder.tv_rain.setText(vo.rainPer);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemData {

        public String day;
        public String rainPer;
        public String temp;
        public int imageView;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getRainPer1() {
            return rainPer;
        }

        public void setRainPer1(String rainPer1) {
            this.rainPer = rainPer1;
        }


        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public int getImageView() {
            return imageView;
        }

        public void setImageView(int imageView) {
            this.imageView = imageView;
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_day,tv_temp,tv_rain;
        ImageView img_main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_day = itemView.findViewById(R.id.day5_day);
            tv_temp = itemView.findViewById(R.id.day5_temp);
            tv_rain = itemView.findViewById(R.id.day5_rain);
            img_main = itemView.findViewById(R.id.day5_main);


        }
    }
}