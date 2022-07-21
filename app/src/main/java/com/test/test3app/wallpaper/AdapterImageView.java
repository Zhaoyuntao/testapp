package com.test.test3app.wallpaper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * created by zhaoyuntao
 * on 2019-10-23
 * description:
 * the main part of audio record view, include a clickable circle and a lock area.
 */
public class AdapterImageView extends AppCompatImageView {


    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private int width;
    private int height;

    public AdapterImageView(Context context) {
        super(context);
        init();
    }

    public AdapterImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdapterImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        setScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = Math.max(width, getMeasuredWidth());
        height = Math.max(height, getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        canvas.drawColor(Color.BLUE);
        if (drawable == null || width <= 0 || height <= 0) {
            super.onDraw(canvas);
            return;
        }
        int widthOfBitmap = drawable.getIntrinsicWidth();
        int heightOfBitmap = drawable.getIntrinsicHeight();
        if (widthOfBitmap <= 0 || heightOfBitmap <= 0) {
            return;
        }
        float drawableRatio = (float) widthOfBitmap / heightOfBitmap;
        float canvasRatio = width / (float) height;
        int widthBounds, heightBounds;
        if (canvasRatio > drawableRatio) {
            widthBounds = width;
            heightBounds = (int) (widthBounds / drawableRatio);
        } else {
            heightBounds = height;
            widthBounds = (int) (heightBounds * drawableRatio);
        }
        int left = (width - widthBounds) / 2;
        int right = left + widthBounds;
        int top = 0;
        int bottom = top + heightBounds;
        drawable.setBounds(left, top, right, bottom);
        canvas.setDrawFilter(paintFlagsDrawFilter);
        drawable.draw(canvas);
    }
}
