package com.test.test3app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.textview.BubbleView;
import com.test.test3app.textview.ConversationTextView;

public class MainActivity_990_cell extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_990_cell);

        BubbleView bubbleView = findViewById(R.id.bubble1);
        bubbleView.setTailDirection(BubbleView.TailDirection.LEFT);
        BubbleView bubbleView2 = findViewById(R.id.bubble2);
        bubbleView2.setTailDirection(BubbleView.TailDirection.LEFT_HIDE);

        BubbleView bubbleView3 = findViewById(R.id.bubble3);
        bubbleView3.setTailDirection(BubbleView.TailDirection.RIGHT);
        BubbleView bubbleView4 = findViewById(R.id.bubble4);
        bubbleView4.setTailDirection(BubbleView.TailDirection.RIGHT_HIDE);

        BubbleView bubbleView5 = findViewById(R.id.bubble5);
        bubbleView5.setTailDirection(BubbleView.TailDirection.RIGHT);

        ConversationTextView conversationTextView1 = findViewById(R.id.ztextview1);
        ConversationTextView conversationTextView2 = findViewById(R.id.ztextview2);
        ConversationTextView conversationTextView3 = findViewById(R.id.ztextview3);
        ConversationTextView conversationTextView4 = findViewById(R.id.ztextview4);

        TextView textView1 = new TextView(activity());
        TextView textView2 = new TextView(activity());
        TextView textView3 = new TextView(activity());
        TextView textView4 = new TextView(activity());
        TextView textView_t1 = new TextView(activity());
        TextView textView_t2 = new TextView(activity());
        TextView textView_t3 = new TextView(activity());
        TextView textView_t4 = new TextView(activity());
        textView_t1.setText("23:34 pm");
        textView_t2.setText("23:34 pm");
        textView_t3.setText("23:34 pm");
        textView_t4.setText("23:34 pm");
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        textView3.setTextColor(Color.BLACK);
        textView4.setTextColor(Color.BLACK);
        textView_t1.setTextColor(Color.BLACK);
        textView_t2.setTextColor(Color.BLACK);
        textView_t3.setTextColor(Color.BLACK);
        textView_t4.setTextColor(Color.BLACK);

        conversationTextView1.setContentView(textView1);
        conversationTextView2.setContentView(textView2);
        conversationTextView3.setContentView(textView3);
        conversationTextView4.setContentView(textView4);

        conversationTextView1.setTailView(textView_t1);
        conversationTextView2.setTailView(textView_t2);
        conversationTextView3.setTailView(textView_t3);
        conversationTextView4.setTailView(textView_t4);


        EditText editText = findViewById(R.id.edit);
        editText.setTextColor(Color.BLACK);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textView1.setText(editable);
                textView2.setText(editable);
                textView3.setText(editable);
                textView4.setText(editable);
            }
        });
    }
}