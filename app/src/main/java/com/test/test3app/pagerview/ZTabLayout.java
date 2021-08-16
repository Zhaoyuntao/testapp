package com.test.test3app.pagerview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.test.test3app.R;
import com.test.test3app.utils.BitmapUtils;
import com.zhaoyuntao.androidutils.tools.B;
import com.zhaoyuntao.androidutils.tools.TextMeasure;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 2020-03-16
 * description:
 */
public class ZTabLayout extends View {
    private List<Item> items = new ArrayList<>();
    private int positionNow;
    private int colorOfTab;
    private int colorOfText;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private int widthOfItem;
    private ZPagerView zPagerView;
    private int heightOfScrollBar;
    private boolean fullTab;

    public ZTabLayout(Context context) {
        super(context);
        init(null);
    }

    public ZTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ZTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public ZTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ZTabLayout);
            colorOfTab = typedArray.getColor(R.styleable.ZTabLayout_ztab_color, Color.BLACK);
            colorOfText = typedArray.getColor(R.styleable.ZTabLayout_ztab_textcolor, Color.BLACK);
            fullTab = typedArray.getBoolean(R.styleable.ZTabLayout_ztab_fulltab, false);
            typedArray.recycle();
        } else {
            colorOfTab = Color.BLACK;
            colorOfText = Color.BLACK;
        }
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        heightOfScrollBar = BitmapUtils.dip2px(getContext(), 2);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (fullTab) {
            heightOfScrollBar = height;
        }
        int count = items.size();
        Resources resources = getResources();
        if (width == 0 || height == 0 || count == 0 || resources == null) {
            return;
        }
        canvas.setDrawFilter(paintFlagsDrawFilter);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.setDrawFilter(new DrawFilter());
        widthOfItem = width / count;
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item == null || TextUtils.isEmpty(item.getText())) {
                continue;
            }
            float textSize = B.dip2px(getContext(), item.getTextSizeDp());
//            paint.setColor(item.hasColorRes() ? resources.getColor(item.getColorNormalResId()) : item.getColorNormal());
            paint.setColor(colorOfText);
            paint.setTextSize(textSize);
            paint.setTypeface(item.getTypeface());

            float[] tmp = TextMeasure.measure(item.getText(), textSize);
            float wOfText = tmp[0];
            float hOfText = tmp[1];
            float xOfText = i * widthOfItem + (widthOfItem - wOfText) / 2;
            float yOfText = (height - hOfText) / 2 + hOfText;
            canvas.drawText(item.getText(), xOfText, yOfText, paint);
        }

        canvas.save();
        paint.setColor(colorOfTab);
        RectF rect = new RectF();
        rect.set(positionNow, getHeight() - getPaddingTop() - heightOfScrollBar, positionNow + widthOfItem, getHeight() - getPaddingBottom());
        canvas.drawRect(rect, paint);
        Path path = new Path();
        path.addRect(rect, Path.Direction.CCW);
        canvas.clipPath(path);
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item == null || TextUtils.isEmpty(item.getText())) {
                continue;
            }
            float textSize = B.dip2px(getContext(), item.getTextSizeDp());
            paint.setColor(item.hasColorRes() ? resources.getColor(item.getColorSelectedResId()) : item.getColorSelected());
            paint.setTextSize(textSize);
            paint.setTypeface(item.getTypeface());

            float[] tmp = TextMeasure.measure(item.getText(), textSize);
            float wOfText = tmp[0];
            float hOfText = tmp[1];
            float xOfText = i * widthOfItem + (widthOfItem - wOfText) / 2;
            float yOfText = (height - hOfText) / 2 + hOfText;
            canvas.drawText(item.getText(), xOfText, yOfText, paint);
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (widthOfItem == 0) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                int position = (int) (x / widthOfItem);
                if (position < items.size()) {
                    if (this.zPagerView != null) {
                        this.zPagerView.setCurrentItem(position, true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void connect(ZPagerView zPagerView) {
        this.zPagerView = zPagerView;
        zPagerView.addOnScrollListener(new ZPagerView.OnScrollListener() {
            @Override
            public void onScrollChange(ViewPager scrollView, int x, int y, int oldx, int oldy) {
                if (getCount() == 0) {
                    return;
                }
                setPositionNow(x / getCount());
            }
        });
    }

    private void setPositionNow(int positionNow) {
        this.positionNow = positionNow;
        postInvalidate();
    }

    public void addItem(Item item) {
        this.items.add(item);
        postInvalidate();
    }

    public int getCount() {
        return items.size();
    }

    public void clearItem() {
        this.items.clear();
        postInvalidate();
    }

    public static class Item {
        private String text;
        private float textSizeDp = 22;
        private int colorNormal = Color.BLACK;
        private int colorSelected = Color.WHITE;
        private boolean hasColorResNormal;
        private boolean hasColorResSelected;
        private int colorNormalResId;
        private int colorSelectedResId;
        private Typeface typeface = Typeface.DEFAULT_BOLD;

        public int getColorNormal() {
            return colorNormal;
        }

        public void setColorNormal(int colorNormal) {
            this.colorNormal = colorNormal;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public float getTextSizeDp() {
            return textSizeDp;
        }

        public void setTextSizeDp(float textSizeDp) {
            this.textSizeDp = textSizeDp;
        }

        public int getColorSelected() {
            return colorSelected;
        }

        public void setColorSelected(int colorSelected) {
            this.colorSelected = colorSelected;
        }

        public int getColorNormalResId() {
            return colorNormalResId;
        }

        public void setColorNormalResId(int colorNormalResId) {
            this.colorNormalResId = colorNormalResId;
            hasColorResNormal = true;
        }

        public int getColorSelectedResId() {
            return colorSelectedResId;
        }

        public void setColorSelectedResId(int colorSelectedResId) {
            this.colorSelectedResId = colorSelectedResId;
            hasColorResSelected = true;
        }

        public boolean hasColorRes() {
            return hasColorResNormal;
        }

        public boolean hasColorRes2() {
            return hasColorResSelected;
        }

        public Typeface getTypeface() {
            return typeface;
        }

        public void setTypeface(Typeface typeface) {
            this.typeface = typeface;
        }
    }
}
