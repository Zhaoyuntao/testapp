package com.test.test3app.huaweimeeting.contact.adapter;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.test.test3app.R;
import com.test.test3app.expandrecyclerview.ExpandableViewHolder;

/**
 * created by zhaoyuntao
 * on 2020-03-17
 * description:
 */
public class ContactHolder extends ExpandableViewHolder {
    private TextView textView;
    private ImageView imageView;
    private ImageButton call;
    private ImageButton video;
    private AppCompatCheckBox appCompatCheckBox;

    public ContactHolder(final View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.title);
        imageView = itemView.findViewById(R.id.imageview);
        call = itemView.findViewById(R.id.call);
        video = itemView.findViewById(R.id.video);
        appCompatCheckBox = itemView.findViewById(R.id.checkbox);
    }

    @Override
    protected void onExpand() {
        if (imageView == null) {
            return;
        }
        imageView.setRotation(-180);
    }

    @Override
    protected void onShrink() {
        if (imageView == null) {
            return;
        }
        imageView.setRotation(0);
    }

    public void setText(String text) {
        if (textView == null) {
            return;
        }
        textView.setText(text);
    }

    public void setOnCallClickListener(View.OnClickListener onCallClickListener) {
        if (call == null) {
            return;
        }
        call.setOnClickListener(onCallClickListener);
    }

    public void setOnVideoClickListener(View.OnClickListener onVideoClickListener) {
        if (video == null) {
            return;
        }
        video.setOnClickListener(onVideoClickListener);
    }

    public void setOnCheckChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        if (appCompatCheckBox == null) {
            return;
        }
        appCompatCheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
    }
}