package com.example.nabi.fragment.Diary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.style.LineBackgroundSpan;

import androidx.annotation.NonNull;

public class EveryDaySpan implements LineBackgroundSpan {

    private Integer day;
    boolean today;

    EveryDaySpan(Context context, int day, boolean today){
        this.day = day;
        this.today = today;

    }


    @Override
    public void drawBackground(Canvas canvas, Paint paint,
                               int left, int right, int top, int baseline, int bottom,
                               CharSequence charSequence,
                               int start, int end, int lineNum) {


        // 오늘이라면 날짜 네모칸+하얀색 글자
        if(today){
            Paint mpaint= new Paint();
            mpaint.setColor(Color.rgb(250,133,116));
            mpaint.setStyle(Paint.Style.FILL);
            Paint paint2= new Paint();
            paint2.setColor(Color.WHITE);
            paint2.setTextSize(35);  //set text size
            paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            float w = paint2.measureText(String.valueOf(day))/2;
            float textSize = paint2.getTextSize();


            paint2.setTextAlign(Paint.Align.CENTER);
            int x = left+35;
            int y = top+10;
            canvas.drawRoundRect(new RectF(x-w-5, y-textSize-4, x+w+5, y+8), 10, 10, mpaint);
            canvas.drawText(String.valueOf(day), x,y ,paint2);
        }
        else if(day != null){
            // 오늘 아니면 네모칸 없고 회색 글자
            Paint paint2= new Paint();
            paint2.setColor(Color.rgb(137,137,137)); // 회색
            paint2.setTextSize(35);  //set text size

            paint2.setTextAlign(Paint.Align.CENTER);
            int x = left+35;
            int y = top+10;
            canvas.drawText(String.valueOf(day), x,y ,paint2);
        }

    }
}
