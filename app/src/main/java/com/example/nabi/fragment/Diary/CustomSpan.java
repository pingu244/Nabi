package com.example.nabi.fragment.Diary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.LineBackgroundSpan;

import com.example.nabi.R;

public class CustomSpan implements LineBackgroundSpan{

    private Bitmap image1;
    private Integer day;
    boolean today;
    Integer mood;
    Context context;

    public CustomSpan(Context context, Integer day, boolean today, Integer mood) {
        this.context = context;
        this.day = day;
        this.today = today;
        this.mood = mood;
    }


    @Override
    public void drawBackground(Canvas canvas, Paint paint,
                               int left, int right, int top, int baseline, int bottom,
                               CharSequence charSequence,
                               int start, int end, int lineNum) {

        switch (mood)
        {
            case 0:
                image1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.moodtracker0); break;
            case 1:
                image1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.moodtracker1); break;
            case 2:
                image1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.moodtracker2); break;
            case 3:
                image1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.moodtracker3); break;
            case 4:
                image1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.moodtracker4); break;
            case 5:
                image1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.moodtracker5); break;
        }


        canvas.drawBitmap(image1, right/2-10, bottom/2-10, null);

    }
}
