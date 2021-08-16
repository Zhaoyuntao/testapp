package com.test.test3app.wallpaper;

import android.content.Context;
import android.graphics.Canvas;
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


    private Paint paint;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private float originPercent;
    private int originWidth;
    private int originHeight;
    private int originY;

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
        //paint and canvas
        paint = new Paint();
        paint.setAntiAlias(true);
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        setScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        Object drawableLast = getTag();
        if (drawableLast != drawable) {
            originWidth = 0;
            originHeight = 0;
            originPercent = 0;
            setTag(drawable);
        }
        int widthCanvas = getWidth();
        int heightCanvas = getHeight();
        if (originWidth == 0) {
            originWidth = getWidth();
            originHeight = getHeight();
            int widthOfBitmap = drawable.getIntrinsicWidth();
            int heightOfBitmap = drawable.getIntrinsicHeight();
            originPercent = (float) widthOfBitmap / heightOfBitmap;
            float canvasPercent = (float) originWidth / originHeight;
            if (canvasPercent > originPercent) {
                originHeight = (int) (originWidth / originPercent);
            } else {
                originWidth = (int) (originHeight * originPercent);
            }
            originY = (heightCanvas - originHeight) / 2;
        }
        if (originWidth == 0 || originHeight == 0) {
            return;
        }

        int left = (widthCanvas - originWidth) / 2;
        int right = left + originWidth;
        int top = originY;
        int bottom = top + originHeight;
        drawable.setBounds(left, top, right, bottom);
        canvas.setDrawFilter(paintFlagsDrawFilter);
        drawable.draw(canvas);
    }
}
