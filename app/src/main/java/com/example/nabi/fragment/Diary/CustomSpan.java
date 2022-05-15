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

    public static final float DEFAULT_RADIUS = 3;
    private final float radius;
    private final int color;
    private Bitmap image1;
    BitmapDrawable bitmapDrawable;
    //    private final Drawable drawable;
    private Integer day;
    boolean today;
    Integer mood;
    Context context;

    /**
     * Create a span to draw a dot using default radius and color
     *
     * @see #CustomSpan(float, int)
     * @see #DEFAULT_RADIUS
     */
    public CustomSpan(Context context, Integer day, boolean today, Integer mood) {
//        this.drawable = drawable;
        this.radius = DEFAULT_RADIUS;
        this.color = 0;
        this.context = context;
        this.day = day;
        this.today = today;
        this.mood = mood;
    }

    /**
     * Create a span to draw a dot using a specified color
     *
     * @param color color of the dot
     * @see #CustomSpan(float, int)
     * @see #DEFAULT_RADIUS
     */
    public CustomSpan(int color) {
        this.radius = DEFAULT_RADIUS;
        this.color = color;
    }

    /**
     * Create a span to draw a dot using a specified radius
     *
     * @param radius radius for the dot
     * @see #CustomSpan(float, int)
     */
    public CustomSpan(float radius) {
        this.radius = radius;
        this.color = 0;
    }

    /**
     * Create a span to draw a dot using a specified radius and color
     *
     * @param radius radius for the dot
     * @param color color of the dot
     */
    public CustomSpan(float radius, int color) {
        this.radius = radius;
        this.color = color;
    }


    @Override
    public void drawBackground(Canvas canvas, Paint paint,
                               int left, int right, int top, int baseline, int bottom,
                               CharSequence charSequence,
                               int start, int end, int lineNum) {
//        int oldColor = paint.getColor();
        if (color != 0) {
            paint.setColor(color);
        }
//        canvas.drawCircle((left + right) / 2, bottom + radius, radius, paint);

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
