package com.test.test3app.pay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.test.test3app.R;

/**
 * created by zhaoyuntao
 * on 2019-12-18
 * description:
 */
public class PaymentCell extends LinearLayout {

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_NUMBER = 1;

    private TextView mTitleView;
    private View mContentViewNumberType;
    private View mContentViewTextType;
    private LinearLayout mDetailContainer;
    private TextView mViewDetails;
    private TextView mUnit;
    private TextView mNumber;
    private TextView mHeadText;
    private TextView mTimeView;
    private View mBody;

    public PaymentCell(Context context) {
        super(context);
        init();
    }

    public PaymentCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaymentCell(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PaymentCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(R.layout.layout_payment_cell, this, true);
        mTitleView = findViewById(R.id.title);
        mDetailContainer = findViewById(R.id.detail);
        mContentViewNumberType = findViewById(R.id.content_type_number);
        mContentViewTextType = findViewById(R.id.content_type_text);
        mContentViewTextType.setVisibility(GONE);
        mViewDetails = findViewById(R.id.view_detail);
        mNumber = findViewById(R.id.numbers);
        mUnit = findViewById(R.id.unit);
        mHeadText = findViewById(R.id.head_text);
        mTimeView = findViewById(R.id.time_view);
        mBody = findViewById(R.id.body);
        setContentType(TYPE_NUMBER);
    }

    public void addDetail(String title, String description) {
        if (mDetailContainer != null) {
            View itemOfDetail = LayoutInflater.from(getContext()).inflate(R.layout.layout_payment_cell_detail, this, false);
            TextView titleView = itemOfDetail.findViewById(R.id.title);
            titleView.setText(title);
            TextView descriptionView = itemOfDetail.findViewById(R.id.description);
            descriptionView.setText(description);
            mDetailContainer.addView(itemOfDetail);
        }
    }

    public void setTitle(String title) {
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    public void setContentType(final int type) {
        switch (type) {
            case TYPE_NUMBER:
                if (mContentViewNumberType != null) {
                    mContentViewNumberType.setVisibility(VISIBLE);
                }
                if (mContentViewTextType != null) {
                    mContentViewTextType.setVisibility(GONE);
                }
                break;
            case TYPE_TEXT:
                if (mContentViewNumberType != null) {
                    mContentViewNumberType.setVisibility(GONE);
                }
                if (mContentViewTextType != null) {
                    mContentViewTextType.setVisibility(VISIBLE);
                }
                break;
        }
    }

    public void setHeadNumber(String number) {
        if (mNumber != null) {
            mNumber.setText(number);
        }
    }

    public void setHeadUnit(String unit) {
        if (mUnit != null) {
            mUnit.setText(unit);
        }
    }

    public void setHeadText(String text) {
        if (mHeadText != null) {
            mHeadText.setText(text);
        }
    }

    public void setTime(String time) {
        if (mTimeView != null) {
            mTimeView.setText(time);
        }
    }

    public void setOnViewDetailClickListener(OnClickListener onViewDetailClickListener) {
        if (mViewDetails != null) {
            mViewDetails.setOnClickListener(onViewDetailClickListener);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        if (mBody != null) {
            mBody.setOnClickListener(onClickListener);
        }
    }
}
