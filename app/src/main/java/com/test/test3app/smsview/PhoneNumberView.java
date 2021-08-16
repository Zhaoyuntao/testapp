package com.test.test3app.smsview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.test.test3app.R;
import com.test.test3app.utils.BitmapUtils;
import com.zhaoyuntao.androidutils.tools.S;
import com.zhaoyuntao.androidutils.tools.TextMeasure;

public class PhoneNumberView extends View {


    private float w_view;
    private float h_view;

    private int color_countryCode;
    private int color_phoneNumber;

    private Paint paint;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    private String countryCode;
    private String phoneNumber;

    private float textSize_max;
    private float textSize_min;
    private boolean bold;

    public PhoneNumberView(Context context) {
        super(context);
        init(null);
    }

    public PhoneNumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PhoneNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        textSize_max = BitmapUtils.sp2px(getContext(), 30);
        textSize_min = BitmapUtils.sp2px(getContext(), 17);
        Resources resources = getContext().getResources();
        color_countryCode = resources.getColor(R.color.color_countrycode);
        color_phoneNumber = resources.getColor(R.color.color_phonenumber);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PhoneNumberView);
            textSize_min = typedArray.getDimensionPixelSize(R.styleable.PhoneNumberView_number_textsize_min, (int) textSize_min);
            textSize_max = typedArray.getDimensionPixelSize(R.styleable.PhoneNumberView_number_textsize_max, (int) textSize_max);
            color_countryCode = typedArray.getColor(R.styleable.PhoneNumberView_color_country_code, color_countryCode);
            color_phoneNumber = typedArray.getColor(R.styleable.PhoneNumberView_color_phone_number, color_phoneNumber);
            bold = typedArray.getBoolean(R.styleable.PhoneNumberView_number_bold, false);
            S.s("max:" + textSize_max);
            typedArray.recycle();
        }
        paint = new Paint();
        paint.setAntiAlias(true);
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (w_view == 0 || h_view == 0) {
            w_view = getWidth();
            h_view = getHeight();
        }

        if (w_view <= 0 || h_view <= 0) {
            return;
        }
        if (TextUtils.isEmpty(countryCode)) {
            countryCode = "+0";
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = "000";
        }

        canvas.setDrawFilter(paintFlagsDrawFilter);

        String text = countryCode + phoneNumber;
        float textSize = textSize_max;
        float w_text = TextMeasure.measure(text, textSize)[0];
        float h_text = TextMeasure.measure(text, textSize)[1];
        float w_divider = (int) TextMeasure.measure("8", textSize)[0];
        float percent_text = w_text / h_text;

        float percent_view = w_view / h_view;

        if (percent_text > percent_view) {
            textSize = (w_view - w_divider) / w_text * textSize;
        } else {
            textSize = h_view;
        }

        if (textSize > textSize_max) {
            textSize = textSize_max;
        } else if (textSize < textSize_min) {
            textSize = textSize_min;
        }
        w_divider = TextMeasure.measure("8", textSize)[0];
        h_text = TextMeasure.measure("8", textSize)[1];

        float w_text_countryCode = TextMeasure.measure(countryCode, textSize)[0];
        float w_text_phoneNumber = TextMeasure.measure(phoneNumber, textSize)[0];

        float x_text_countryCode = (w_view - w_divider - w_text_countryCode - w_text_phoneNumber) / 2;
        float x_text_phoneNumber = x_text_countryCode + w_text_countryCode + w_divider;
        float y_text = (h_view - h_text) / 2 + h_text;

        paint.setTextSize(textSize);
        paint.setColor(color_countryCode);
        paint.setFakeBoldText(bold);
        canvas.drawText(countryCode, x_text_countryCode, y_text, paint);
        paint.setColor(color_phoneNumber);
        canvas.drawText(phoneNumber, x_text_phoneNumber, y_text, paint);

    }

    @Override
    public void destroyDrawingCache() {
        super.destroyDrawingCache();
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        postInvalidate();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        postInvalidate();
    }

}
