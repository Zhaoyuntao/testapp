package com.test.test3app.qmoji;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhaoyuntao.androidutils.tools.B;


/**
 * created by zhaoyuntao
 * on 2020-02-09
 * description:
 */
public class QmojiSelectView extends FrameLayout {
    RecyclerView qmojiView;
    QmojiAdapter qmojiAdapter;

    public QmojiSelectView(@NonNull Context context) {
        super(context);
        init();
    }

    public QmojiSelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QmojiSelectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public QmojiSelectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setClickable(true);

        qmojiAdapter = new QmojiAdapter();

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, B.dip2px(getContext(), 60));
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.setMargins(0, 0, 0, B.dip2px(getContext(), 109));

        qmojiView = new RecyclerView(getContext());
        qmojiView.setBackgroundColor(Color.argb(50, 0, 0, 0));
        qmojiView.setAdapter(qmojiAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        qmojiView.setLayoutManager(manager);
        qmojiView.setLayoutParams(layoutParams);
        addView(qmojiView);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(GONE);
            }
        });
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() == visibility) {
            return;
        }
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(200);
            alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            startAnimation(alphaAnimation);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(100);
            alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            startAnimation(alphaAnimation);
        }
    }

    public void addQmojiItem(int index, int drawableId) {
        qmojiAdapter.addQmojiItem(index, drawableId);
    }

    public void clearQmojiItem() {
        qmojiAdapter.clearQmojiItems();
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        qmojiAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int index) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(index);
                }
                setVisibility(GONE);
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(int index);
    }
}
