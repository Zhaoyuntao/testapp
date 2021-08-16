package com.test.test3app.huaweimeeting.conference.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.test.test3app.R;
import com.test.test3app.expandrecyclerview.ExpandableViewHolder;

public class ConferenceHolder extends ExpandableViewHolder {
    private TextView textView1;
    private TextView textView2;
    private TextView title;
    private Button button;

    public ConferenceHolder(@NonNull View itemView) {
        super(itemView);
        textView1 = itemView.findViewById(R.id.text1);
        textView2 = itemView.findViewById(R.id.text2);
        button = itemView.findViewById(R.id.button);
        title = itemView.findViewById(R.id.title);
    }

    public void setTextView1(String text) {
        if (textView1 == null) {
            return;
        }
        textView1.setText(text);
    }

    public void setTextView2(String text) {
        if (textView2 == null) {
            return;
        }
        textView2.setText(text);
    }

    public void setOnButtonClickListener(View.OnClickListener onClickListener) {
        if (button == null) {
            return;
        }
        button.setOnClickListener(onClickListener);
    }

    public void hideButton() {
        if (button == null) {
            return;
        }
        button.setVisibility(View.GONE);
    }

    public void disableButton() {
        if (button == null) {
            return;
        }
        button.setEnabled(false);
    }

    public void setTitle(String title) {
        if (title == null) {
            return;
        }
        this.title.setText(title);
    }
}
