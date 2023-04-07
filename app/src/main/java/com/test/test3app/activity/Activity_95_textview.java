package com.test.test3app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.test.test3app.R;

import base.ui.BaseActivity;
import im.thebot.chat.ui.view.BubbleHorizontalLinearLayout;
import im.turbo.baseui.utils.UiUtils;

public class Activity_95_textview extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_95_textview);

        BubbleHorizontalLinearLayout linearLayout1 = findViewById(R.id.bubble_layout_1);
        BubbleHorizontalLinearLayout linearLayout2 = findViewById(R.id.bubble_layout_2);
        BubbleHorizontalLinearLayout linearLayout3 = findViewById(R.id.bubble_layout_3);
        BubbleHorizontalLinearLayout linearLayout4 = findViewById(R.id.bubble_layout_4);
        BubbleHorizontalLinearLayout linearLayout5 = findViewById(R.id.bubble_layout_5);
        BubbleHorizontalLinearLayout linearLayout6 = findViewById(R.id.bubble_layout_6);

        TextView textView1 = create(linearLayout1, "1", UiUtils.dipToPx(50),
                new BubbleHorizontalLinearLayout.BubbleHorizontalLayoutParams(0, true));
        TextView textView2 = create(linearLayout2, "2",
                new BubbleHorizontalLinearLayout.BubbleHorizontalLayoutParams(0, false));
        TextView textView3 = create(linearLayout3, "3",
                new BubbleHorizontalLinearLayout.BubbleHorizontalLayoutParams(UiUtils.dipToPx(500), true));
        TextView textView4 = create(linearLayout4, "4",
                new BubbleHorizontalLinearLayout.BubbleHorizontalLayoutParams(UiUtils.dipToPx(500), false));
        TextView textView5 = create(linearLayout5, "5",
                new BubbleHorizontalLinearLayout.BubbleHorizontalLayoutParams(UiUtils.dipToPx(200), true));
        TextView textView6 = create(linearLayout6, "6",
                new BubbleHorizontalLinearLayout.BubbleHorizontalLayoutParams(UiUtils.dipToPx(200), false));

        EditText editText = findViewById(R.id.edit_text_hello);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textView1.setText(s);
                textView2.setText(s);
                textView3.setText(s);
                textView4.setText(s);
                textView5.setText(s);
                textView6.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setText("absaj");
    }

    private TextView create(ViewGroup parent, String tag, BubbleHorizontalLinearLayout.BubbleHorizontalLayoutParams layoutParams) {
        return create(parent, tag, UiUtils.dipToPx(20), layoutParams);
    }

    private TextView create(ViewGroup parent, String tag, int imageSize, BubbleHorizontalLinearLayout.BubbleHorizontalLayoutParams layoutParams) {
        ImageView imageView1 = new ImageView(parent.getContext());
        imageView1.setLayoutParams(new LinearLayout.LayoutParams(imageSize, imageSize));
        imageView1.setImageResource(R.drawable.icon_love);
        imageView1.setBackgroundColor(Color.WHITE);
        parent.addView(imageView1);

        AppCompatTextView textView = new AppCompatTextView(activity());
        textView.setTextSize(18);
        textView.setTextColor(Color.WHITE);
        textView.setSingleLine(true);
        textView.setBackgroundColor(Color.RED);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTag(tag);
        int padding = UiUtils.dipToPx(2);
        textView.setPaddingRelative(padding, padding, padding, padding);
        layoutParams.setMarginStart(padding);
        layoutParams.setMarginEnd(padding);
        layoutParams.topMargin = UiUtils.dipToPx(1);
        layoutParams.bottomMargin = UiUtils.dipToPx(1);
        textView.setLayoutParams(layoutParams);
        parent.addView(textView);

        ImageView imageView2 = new ImageView(parent.getContext());
        imageView2.setLayoutParams(new LinearLayout.LayoutParams(imageSize, imageSize));
        imageView2.setImageResource(R.drawable.icon_love);
        imageView2.setBackgroundColor(Color.WHITE);
        parent.addView(imageView2);
        return textView;
    }
}
