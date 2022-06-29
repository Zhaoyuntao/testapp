package com.test.test3app.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.test.test3app.R;


public class Activity_8_unknown extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity8_permission);
        button = findViewById(R.id.permission);
        final TextView mTextView = findViewById(R.id.s1);
        EditText emojiconEditText = findViewById(R.id.s2);
    }
}
