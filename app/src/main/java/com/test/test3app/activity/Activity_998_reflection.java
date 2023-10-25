package com.test.test3app.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.test.test3app.R;

import java.lang.reflect.Constructor;

import im.thebot.chat.api.chat.message.TextMessageForUI;


public class Activity_998_reflection extends FragmentActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_998_reflection);

        TextView launchView = findViewById(R.id.text_constructor_1);
        TextView useNewInstanceView = findViewById(R.id.text_constructor_2);
        TextView useReflectionView = findViewById(R.id.text_constructor_3);

        launchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    long time = SystemClock.elapsedRealtime();
                    TextMessageForUI c = null;
                    for (int i = 0; i < 1000; i++) {
                        Constructor<TextMessageForUI> constructor = TextMessageForUI.class.getConstructor();
                        constructor.setAccessible(true);
                        c = constructor.newInstance();
                    }
                    long duration = SystemClock.elapsedRealtime() - time;
                    launchView.setText(duration + " ms " +
                            "\n" + c);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        });

        int count = 10000;
        useNewInstanceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    long time = SystemClock.elapsedRealtimeNanos();
                    TextMessageForUI c = null;
                    for (int i = 0; i < count; i++) {
                        c = new TextMessageForUI();
                    }
                    long duration = SystemClock.elapsedRealtimeNanos() - time;
                    useNewInstanceView.setText(duration + " ns "
                            + "\n" + c
                            + "\naverage cost:" + (duration / count)+" ns");
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        });
        useReflectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    long time = SystemClock.elapsedRealtimeNanos();
                    TextMessageForUI c = null;
                    Constructor<TextMessageForUI> constructor = TextMessageForUI.class.getConstructor();
                    constructor.setAccessible(true);
                    for (int i = 0; i < count; i++) {
                        c = constructor.newInstance();
                    }
                    long duration = SystemClock.elapsedRealtimeNanos() - time;
                    useReflectionView.setText(duration + "ns " +
                            "\n" + c
                            + "\naverage cost:" + (duration / count)+" ns");
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
